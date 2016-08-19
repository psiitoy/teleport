package org.sprintdragon.teleport.core.migrate;

import org.sprintdragon.teleport.core.domain.IMigrateTask;

import java.util.Collection;

/**
 * 任务处理器
 */
public interface IMigrateProcessor {
    void addTask(IMigrateTask task);

    void migrate(IMigrateTask iMigrateTask);

    Collection<IMigrateTask> getAllDataTaskVo();

    IMigrateTask getTaskById(Integer id);

    void remove(Integer id);

}
