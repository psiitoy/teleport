package org.sprintdragon.teleport.core.migrate.receiver;

import org.apache.commons.lang3.tuple.ImmutablePair;
import org.apache.commons.lang3.tuple.Pair;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.sprintdragon.teleport.common.utils.CopyPropertyUtils;
import org.sprintdragon.teleport.core.constants.ReactorConstants;
import org.sprintdragon.teleport.core.domain.IMigrateTask;
import org.sprintdragon.teleport.core.domain.TaskStatResult;
import org.sprintdragon.teleport.core.migrate.IMigrateProcessor;
import org.sprintdragon.teleport.core.migrate.IPersistentManager;
import org.sprintdragon.teleport.persistent.dao.EntityDao;
import org.sprintdragon.teleport.persistent.page.PaginatedList;
import org.sprintdragon.teleport.persistent.query.DefaultBaseQuery;
import org.sprintdragon.teleport.persistent.query.Query;
import reactor.bus.Event;
import reactor.bus.EventBus;
import reactor.fn.Consumer;

import javax.annotation.PostConstruct;
import javax.annotation.Resource;

import static reactor.bus.selector.Selectors.$;

/**
 * Created by wangdi on 16-8-15.
 */
@Service
public class QueryReceiver implements Consumer<Event<Pair<Integer, Integer>>> {

    private final Logger logger = LoggerFactory.getLogger(QueryReceiver.class);

    @Resource
    private IPersistentManager entityDaoManager;

    @Autowired
    private IMigrateProcessor migrateService;

    @Resource
    private EventBus consumerEventBus;
    @Resource
    private EventBus producerEventBus;

    @PostConstruct
    public void init() {
        producerEventBus.on($(ReactorConstants.MIGRATE_QUERY_BY_PAGE_AND_SPLIT_TASK), this);
    }

    @Override
    public void accept(Event<Pair<Integer, Integer>> pairEvent) {
        Integer taskId = pairEvent.getData().getLeft();
        final IMigrateTask task = migrateService.getTaskById(taskId);
        final TaskStatResult result = task.getResult();
        Query query = task.getQuery();
        int pageNo = pairEvent.getData().getRight();
        DefaultBaseQuery cpQuery = CopyPropertyUtils.copyPropertiesAndInstance(query, DefaultBaseQuery.class);
        cpQuery.setPageNo(pageNo);
        cpQuery.setIndex(null);

        EntityDao entityDao = entityDaoManager.findEntityDaoByKey(task.getFromDbKey());
        if (!task.isRun()) {
            logger.info("isRun:false task:{}", task);
            return;
        }
        PaginatedList list;
        try {
            list = entityDao.findByPage(cpQuery);
        } catch (Exception e) {
            logger.error("pairEvent=", e);
            result.errorIncrby();
            return;
        }
//            result.selectIncrby(list.size());
        for (Object obj : list) {
            consumerEventBus.notify(ReactorConstants.MIGRATE_UPSERT, Event.wrap(ImmutablePair.of(task.getId(), obj)));
        }
    }
}
