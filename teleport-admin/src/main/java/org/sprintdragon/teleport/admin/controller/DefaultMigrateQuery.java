package org.sprintdragon.teleport.admin.controller;


import org.sprintdragon.teleport.persistent.query.DefaultBaseQuery;

/**
 * Created by wangdi on 16-8-16.
 */
public class DefaultMigrateQuery extends DefaultBaseQuery {

    private String type;

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

}
