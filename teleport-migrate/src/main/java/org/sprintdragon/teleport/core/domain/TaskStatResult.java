package org.sprintdragon.teleport.core.domain;


import java.util.concurrent.atomic.AtomicLong;

/**
 * 数据库执行任务统计
 */
public class TaskStatResult {

    private AtomicLong[] count = new AtomicLong[TaskStatItemEnum.values().length];

    {
        for (int i = 0; i < this.count.length; i++) {
            count[i] = new AtomicLong();
        }
    }


    public void incrby(TaskStatItemEnum itemEnum) {
        incrby(itemEnum, 1);
    }

    public void incrby(TaskStatItemEnum itemEnum, long increment) {
        count[itemEnum.ordinal()].addAndGet(increment);
    }

    public AtomicLong[] getCount() {
        return count;
    }

    public void setCount(AtomicLong[] count) {
        this.count = count;
    }

    public void resetCount(TaskStatItemEnum itemEnum, long data) {
        count[itemEnum.ordinal()].set(data);
    }

    public void selectIncrby(long increment) {
        incrby(TaskStatItemEnum.SELECT, increment);
    }

    public void insertIncrby() {
        incrby(TaskStatItemEnum.INSERT);
    }

    public void updateIncrby() {
        incrby(TaskStatItemEnum.UPDATE);
    }

    public void errorIncrby() {
        incrby(TaskStatItemEnum.ERROR);
    }

    public void updateErrorIncrby() {
        incrby(TaskStatItemEnum.UPDATE_ERROR);
    }

    public void insertErrorIncrby() {
        incrby(TaskStatItemEnum.INSERT_ERROR);
    }

    public void selectMissIncrby() {
        incrby(TaskStatItemEnum.SELECT_MISS);
    }

    public void selectErrorIncrby() {
        incrby(TaskStatItemEnum.SELECT_ERROR);
    }

    public TaskStatResult updateMissIncrby() {
        incrby(TaskStatItemEnum.UPDATE_MISS);
        return this;
    }
}
