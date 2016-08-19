package org.sprintdragon.teleport.persistent.attribute;

import java.util.Date;

/**
 * Created by wangdi on 16-2-26.
 */
public enum BasicAttributeEnum {
    /**
     * 大于
     */
    ID("id", Long.class),
    MODIFIED("modified", Date.class),
    CREATED("created", Date.class);

    private String name;
    private Class<?> type;

    BasicAttributeEnum(String name, Class<?> type) {
        this.name = name;
        this.type = type;
    }

    public String getName() {
        return name;
    }

    public Class<?> getType() {
        return type;
    }

    public static String[] getAllBasicAttributes() {
        return new String[]{
                ID.getName(), MODIFIED.getName(), CREATED.getName()
        };
    }
}
