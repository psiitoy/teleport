package org.sprintdragon.teleport.core.domain;

import org.sprintdragon.teleport.persistent.query.Query;

import java.util.Date;
import java.util.concurrent.atomic.AtomicLong;

/**
 *
 */
public class MigrateTask implements Comparable<MigrateTask>, IMigrateTask {
    private Integer id;
    private Date currentStart;
    private Integer count;
    private AtomicLong already = new AtomicLong();
    private String type;
    private volatile boolean run = true;

    private TaskStatusEnum status = TaskStatusEnum.PENDING;

    private TaskStatResult result = new TaskStatResult();
    private Query query;
    private String fromDbKey;
    private String toDbKey;

    @Override
    public String toString() {
        return "MigrateTask{" +
                "id=" + id +
                ", currentStart=" + currentStart +
                ", count=" + count +
                ", already=" + already +
                ", type='" + type + '\'' +
                ", run=" + run +
                ", status=" + status +
                ", result=" + result +
                ", query=" + query +
                ", fromDbKey='" + fromDbKey + '\'' +
                ", toDbKey='" + toDbKey + '\'' +
                '}';
    }

    @Override
    public Integer getId() {
        return id;
    }

    @Override
    public void setId(Integer id) {
        this.id = id;
    }

    @Override
    public TaskStatusEnum getStatus() {
        return status;
    }

    @Override
    public void setStatus(TaskStatusEnum status) {
        this.status = status;
    }

    public TaskStatResult getResult() {
        return result;
    }

    public void setResult(TaskStatResult result) {
        this.result = result;
    }

    public void setCurrentStart(Date currentStart) {
        if (currentStart == null) {
            this.currentStart = null;
        } else {
            this.currentStart = (Date) currentStart.clone();
        }
    }

    public Date getCurrentStart() {
        if (currentStart == null) {
            return null;
        }
        return (Date) currentStart.clone();
    }

    public AtomicLong getAlready() {
        return already;
    }

    public void setAlready(AtomicLong already) {
        this.already = already;
    }

    @Override
    public void incrAlready(long num) {
        already.addAndGet(num);
    }

    public Integer getCount() {
        return count;
    }

    public void setCount(Integer count) {
        this.count = count;
    }

    public boolean isRun() {
        return run;
    }

    @Override
    public void setRun(boolean run) {
        this.run = run;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    @Override
    public Query getQuery() {
        return query;
    }

    public void setQuery(Query query) {
        this.query = query;
    }

    public String getFromDbKey() {
        return fromDbKey;
    }

    public void setFromDbKey(String fromDbKey) {
        this.fromDbKey = fromDbKey;
    }

    public String getToDbKey() {
        return toDbKey;
    }

    public void setToDbKey(String toDbKey) {
        this.toDbKey = toDbKey;
    }

    @Override
    public int compareTo(MigrateTask o) {
        return (this.id == null ? 0 : this.id) - (o.id == null ? 0 : o.id);
    }

}
