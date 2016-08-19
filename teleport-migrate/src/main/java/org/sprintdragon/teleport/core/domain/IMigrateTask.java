package org.sprintdragon.teleport.core.domain;

import org.sprintdragon.teleport.persistent.query.Query;

import java.util.Date;

/**
 * 合并任务(JVM)
 */
public interface IMigrateTask {
    Integer getId();

    void setId(Integer id);

    TaskStatusEnum getStatus();

    void setStatus(TaskStatusEnum status);

    void setCurrentStart(Date currentStart);

    void setRun(boolean run);

    boolean isRun();

    TaskStatResult getResult();

    void setCount(Integer count);

    void incrAlready(long num);

    Query getQuery();

    String getFromDbKey();

    String getToDbKey();
}
