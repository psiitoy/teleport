package org.sprintdragon.teleport.core.migrate;

import org.apache.commons.lang3.tuple.ImmutablePair;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.sprintdragon.teleport.core.constants.ReactorConstants;
import org.sprintdragon.teleport.core.domain.IMigrateTask;
import org.sprintdragon.teleport.core.domain.TaskStatusEnum;
import org.sprintdragon.teleport.core.migrate.taskinfo.DataTaskService;
import org.sprintdragon.teleport.persistent.dao.EntityDao;
import org.sprintdragon.teleport.persistent.page.PaginatedList;
import org.sprintdragon.teleport.persistent.query.Query;
import reactor.bus.Event;
import reactor.bus.EventBus;

import javax.annotation.PostConstruct;
import javax.annotation.Resource;
import java.util.Collection;
import java.util.Date;

/**
 * 任务体
 */
@Service
public class MigrateProcessor extends Thread implements IMigrateProcessor {

    private Logger logger = LoggerFactory.getLogger(IMigrateProcessor.class);

    @Resource
    private IPersistentManager entityDaoManager;

    @Autowired
    private DataTaskService<IMigrateTask> dataTaskService;

    @Resource
    private EventBus producerEventBus;

    @PostConstruct
    public void init() {
//        consumerEventBus.on($("/remark/migrate"), serviceMessageReceiver);
        this.setDaemon(true);
        this.start();
        logger.info("migrate thread start success");
    }

    @Override
    public void addTask(IMigrateTask task) {
        if (task != null) {
            dataTaskService.addTask(task);
        }
    }

    @Override
    public IMigrateTask getTaskById(Integer id) {
        return dataTaskService.getTaskById(id);
    }

    @Override
    public void remove(Integer id) {
        dataTaskService.remove(id);
    }

    @Override
    public void migrate(IMigrateTask iMigrateTask) {
        iMigrateTask.setCurrentStart(new Date());
        logger.info("iMigrateTask:{}", iMigrateTask);
        EntityDao entityDao = entityDaoManager.findEntityDaoByKey(iMigrateTask.getFromDbKey());
        Query query = iMigrateTask.getQuery();
        PaginatedList list = entityDao.findByPage(query);
        iMigrateTask.setCount(list.getTotalItem());
        for (int i = 1; i <= list.getTotalPage(); i++) {
            producerEventBus.notify(ReactorConstants.MIGRATE_QUERY_BY_PAGE_AND_SPLIT_TASK, Event.wrap(ImmutablePair.of(iMigrateTask.getId(), i)));
        }
    }

    @Override
    public Collection<IMigrateTask> getAllDataTaskVo() {
        return dataTaskService.getAllDataTaskVo();
    }

    @Override
    public void run() {
        //noinspection InfiniteLoopStatement
        while (true) {
            try {
                IMigrateTask task = dataTaskService.take();
                try {
                    task.setStatus(TaskStatusEnum.DOING);
                    migrate(task);
                    task.setStatus(TaskStatusEnum.DONE);
                } catch (Exception e) {
                    logger.error("", e);
                    task.setStatus(TaskStatusEnum.CANCELED);
                }
            } catch (Exception e) {
                logger.error("", e);
            }
        }
    }

}
