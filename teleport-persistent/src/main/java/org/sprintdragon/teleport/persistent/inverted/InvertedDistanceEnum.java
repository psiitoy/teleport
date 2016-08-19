package org.sprintdragon.teleport.persistent.inverted;

/**
 * 倒库执行区间
 */
public enum InvertedDistanceEnum {
    /**
     * 1天
     */
    DAY(1),
    /**
     * 7天
     */
    WEEK(7);

    private int days;

    InvertedDistanceEnum(int mongoOrder) {
        this.days = days;
    }

    public int getDays() {
        return days;
    }
}