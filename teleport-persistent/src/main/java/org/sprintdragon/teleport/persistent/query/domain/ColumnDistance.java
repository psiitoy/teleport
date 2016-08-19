package org.sprintdragon.teleport.persistent.query.domain;

import java.io.Serializable;

/**
 * 时间区间
 * Created by wangdi on 16-2-19.
 */
public final class ColumnDistance implements Serializable {
    private String fieldName;
    private Object columnFrom;
    //是否包含(greaterThanEquals，默认true)
    private boolean columnFromContain;
    private Object columnTo;
    //是否包含(greaterThanEquals，默认true)
    private boolean columnToContain;

    public ColumnDistance(String fieldName, Object columnFrom, Object columnTo) {
        this.fieldName = fieldName;
        this.columnFrom = columnFrom;
        this.columnTo = columnTo;
        columnFromContain = true;
        columnToContain = true;
    }

    public String getFieldName() {
        return fieldName;
    }

    public void setFieldName(String fieldName) {
        this.fieldName = fieldName;
    }

    public Object getColumnFrom() {
        return columnFrom;
    }

    public void setColumnFrom(Object columnFrom) {
        this.columnFrom = columnFrom;
    }

    public Object getColumnTo() {
        return columnTo;
    }

    public void setColumnTo(Object columnTo) {
        this.columnTo = columnTo;
    }

    public boolean isColumnFromContain() {
        return columnFromContain;
    }

    public void setColumnFromContain(boolean columnFromContain) {
        this.columnFromContain = columnFromContain;
    }

    public boolean isColumnToContain() {
        return columnToContain;
    }

    public void setColumnToContain(boolean columnToContain) {
        this.columnToContain = columnToContain;
    }
}