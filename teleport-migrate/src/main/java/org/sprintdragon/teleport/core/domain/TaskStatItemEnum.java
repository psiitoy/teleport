package org.sprintdragon.teleport.core.domain;

/**
 * 数据库执行任务统计
 *
 */
public enum TaskStatItemEnum {
    SELECT,
    SELECT_ERROR,
    SELECT_MISS,

    INSERT,
    INSERT_DUPLICATE_KEY_ERROR,
    INSERT_ERROR,

    UPDATE,
    UPDATE_ERROR,
    UPDATE_MISS,

    DELETE,
    DELETE_MISS,

    ERROR;

    public int getOrdinal() {
        return this.ordinal();
    }

}
