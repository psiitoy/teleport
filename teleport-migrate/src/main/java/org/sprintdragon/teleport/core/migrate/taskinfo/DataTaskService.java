package org.sprintdragon.teleport.core.migrate.taskinfo;

import org.sprintdragon.teleport.core.domain.IMigrateTask;

import java.util.Collection;

/**
 * 任务处理器
 *
 */
public interface DataTaskService<T extends IMigrateTask> {
    T take() throws InterruptedException;

    Collection<T> getAllDataTaskVo();

    void addTask(T task);

    void remove(Integer id);

    T getTaskById(Integer id);
}
