package org.sprintdragon.teleport.core.migrate.receiver;

import org.apache.commons.lang3.tuple.Pair;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.sprintdragon.teleport.core.constants.ReactorConstants;
import org.sprintdragon.teleport.core.domain.IMigrateTask;
import org.sprintdragon.teleport.core.domain.TaskStatResult;
import org.sprintdragon.teleport.core.migrate.ICheckMigrate;
import org.sprintdragon.teleport.core.migrate.IMigrateProcessor;
import org.sprintdragon.teleport.core.migrate.IPersistentManager;
import org.sprintdragon.teleport.persistent.dao.EntityDao;
import org.sprintdragon.teleport.persistent.exception.EntityDaoException;
import reactor.bus.Event;
import reactor.bus.EventBus;
import reactor.fn.Consumer;

import javax.annotation.PostConstruct;
import javax.annotation.Resource;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.AtomicLong;

import static reactor.bus.selector.Selectors.$;

/**
 * Created by wangdi on 16-8-15.
 */
@Service
public class UpsertReceiver implements Consumer<Event<Pair<Integer, Object>>> {

    private final Logger logger = LoggerFactory.getLogger(UpsertReceiver.class);

    @Resource
    private IPersistentManager entityDaoManager;

    @Autowired
    private IMigrateProcessor migrateProcessor;

    @Resource
    private EventBus consumerEventBus;

    AtomicInteger seq = new AtomicInteger();
    AtomicLong costTime = new AtomicLong();

    {
        new Thread() {
            @Override
            public void run() {
                while (true) {
                    if (logger != null && seq != null) {
                        logger.info("====== 每分钟入库数量：" + seq.get() + ",costTime=" + costTime.get());
                        costTime.set(0);
                        seq.set(0);
                    }
                    try {
                        Thread.sleep(6 * 10000);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
            }
        }.start();
    }

    @PostConstruct
    public void init() {
        consumerEventBus.on($(ReactorConstants.MIGRATE_UPSERT), this);
    }

    boolean onlyInsert = false;

    @Override
    public void accept(Event<Pair<Integer, Object>> pairEvent) {
        Integer taskId = pairEvent.getData().getLeft();
        final IMigrateTask task = migrateProcessor.getTaskById(taskId);
        final TaskStatResult result = task.getResult();
        Object from = pairEvent.getData().getRight();
        EntityDao entityDao = entityDaoManager.findEntityDaoByKey(task.getToDbKey());
        ICheckMigrate checkMigrate = entityDaoManager.findCheckMigrateByKey(task.getFromDbKey(), task.getToDbKey());
        try {
            Object to = checkMigrate.objTransform(from);

            /**
             * 仅仅插入
             */
            if (onlyInsert) {
                try {
                    long now = System.currentTimeMillis();
                    entityDao.insert(to);
                    task.incrAlready(1);
                    seq.incrementAndGet();
                    result.insertIncrby();
                    costTime.getAndAdd(System.currentTimeMillis() - now);
                } catch (Exception e) {
                    result.insertErrorIncrby();
                    logger.error("insert error pairEvent:{}", pairEvent, e);
                }
                return;
            }


            Object have = null;
            try {
                have = entityDao.findBySingle(checkMigrate.getQuerySingleDomain(to));
                result.selectIncrby(1);
            } catch (EntityDaoException e) {
                result.selectErrorIncrby();
                logger.error("select error pairEvent:{}", pairEvent, e);
            }
            if (have != null) {
                if (!have.equals(to)) {
                    try {
                        entityDao.update(to);
                        result.updateIncrby();
                        task.incrAlready(1);
                        seq.incrementAndGet();
                    } catch (Exception e) {
                        result.updateErrorIncrby();
                        logger.error("update error pairEvent:{}", pairEvent, e);
                    }
                } else {
                    result.updateMissIncrby();
                }
            } else {
                result.selectMissIncrby();
                try {
                    entityDao.insert(to);
                    task.incrAlready(1);
                    seq.incrementAndGet();
                    result.insertIncrby();
                } catch (Exception e) {
                    result.insertErrorIncrby();
                    logger.error("insert error pairEvent:{}", pairEvent, e);
                }
            }
        } catch (Exception e) {
            result.errorIncrby();
            logger.error("pairEvent:{}", pairEvent, e);
        }
    }
}
