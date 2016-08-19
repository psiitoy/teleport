package org.sprintdragon.teleport.persistent.query.domain;

import org.apache.commons.lang.StringUtils;

import java.io.Serializable;

/**
 * Created by wangdi on 16-2-19.
 */
public final class ColumnOrder implements Serializable {
    public static final char SEPARATOR = ',';
    private static final long serialVersionUID = 1L;
    private OrderByDescEnum orderEnum;
    private String[] columnNames;

    public ColumnOrder(OrderByDescEnum orderEnum, String... columnNames) {
        this.orderEnum = orderEnum;
        this.columnNames = columnNames;
    }

    public OrderByDescEnum getOrderEnum() {
        return orderEnum;
    }

    public String[] getColumnNames() {
        if (columnNames == null) {
            return null;
        }
        return columnNames.clone();
    }

    @Override
    public String toString() {
        if (orderEnum == null) {
            throw new IllegalArgumentException("orderEnum must not null");
        }
        if (columnNames == null || columnNames.length < 1) {
            throw new IllegalArgumentException("at least one columnName");
        }
        String order = StringUtils.join(columnNames, " " + orderEnum.name() + SEPARATOR);
        return order + " " + orderEnum.name();
    }
}