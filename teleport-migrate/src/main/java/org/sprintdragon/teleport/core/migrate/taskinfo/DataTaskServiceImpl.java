package org.sprintdragon.teleport.core.migrate.taskinfo;

import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Service;
import org.sprintdragon.teleport.core.domain.IMigrateTask;
import org.sprintdragon.teleport.core.domain.TaskStatusEnum;

import java.util.Collection;
import java.util.Map;
import java.util.TreeSet;
import java.util.concurrent.BlockingDeque;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.LinkedBlockingDeque;
import java.util.concurrent.atomic.AtomicInteger;

/**
 *
 */
@Service
@Scope("prototype")
public class DataTaskServiceImpl<T extends IMigrateTask> implements DataTaskService<T> {

    private AtomicInteger atomicInteger = new AtomicInteger();
    private BlockingDeque<T> taskQueue = new LinkedBlockingDeque<T>();
    private Map<Integer, T> taskVoMap = new ConcurrentHashMap<Integer, T>();

    @Override
    public Collection<T> getAllDataTaskVo() {
        return new TreeSet<T>(taskVoMap.values());
    }

    @Override
    public T take() throws InterruptedException {
        return taskQueue.take();
    }

    @Override
    public void addTask(T task) {
        if (task == null) {
            return;
        }
        if (task.getId() == null) {
            task.setId(atomicInteger.incrementAndGet());
        }
        taskVoMap.put(task.getId(), task);
        taskQueue.add(task);
    }


    @Override
    public void remove(Integer id) {
        T task = taskVoMap.get(id);
        if (task != null) {
            if (task.getStatus() == TaskStatusEnum.CANCELED || task.getStatus() == TaskStatusEnum.DONE) {
                taskVoMap.remove(id);
            }
            if (task.getStatus() == TaskStatusEnum.PENDING) {
                taskQueue.remove(task);
                task.setStatus(TaskStatusEnum.CANCELED);
            }
            if (task.getStatus() == TaskStatusEnum.DOING) {
                task.setRun(false);
            }
        }
    }

    @Override
    public T getTaskById(Integer id) {
        return taskVoMap.get(id);
    }
}
