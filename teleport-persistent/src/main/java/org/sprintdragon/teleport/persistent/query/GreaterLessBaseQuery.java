package org.sprintdragon.teleport.persistent.query;

import java.util.Date;

/**
 * 区间查询 基类 需要实现BasePersistenceBean
 * 继承排序查找
 * Created by wangdi on 15-1-9.
 */
public class GreaterLessBaseQuery extends OrderByBaseQuery {

    private Date createdSymbolGt;
    private Date createdSymbolGte;
    private Date createdSymbolLt;
    private Date createdSymbolLte;
    private Date modifiedSymbolGt;
    private Date modifiedSymbolGte;
    private Date modifiedSymbolLt;
    private Date modifiedSymbolLte;

    public Date getCreatedSymbolGt() {
        return createdSymbolGt;
    }

    public void setCreatedSymbolGt(Date createdSymbolGt) {
        this.createdSymbolGt = createdSymbolGt;
    }

    public Date getCreatedSymbolGte() {
        return createdSymbolGte;
    }

    public void setCreatedSymbolGte(Date createdSymbolGte) {
        this.createdSymbolGte = createdSymbolGte;
    }

    public Date getCreatedSymbolLt() {
        return createdSymbolLt;
    }

    public void setCreatedSymbolLt(Date createdSymbolLt) {
        this.createdSymbolLt = createdSymbolLt;
    }

    public Date getCreatedSymbolLte() {
        return createdSymbolLte;
    }

    public void setCreatedSymbolLte(Date createdSymbolLte) {
        this.createdSymbolLte = createdSymbolLte;
    }

    public Date getModifiedSymbolGt() {
        return modifiedSymbolGt;
    }

    public void setModifiedSymbolGt(Date modifiedSymbolGt) {
        this.modifiedSymbolGt = modifiedSymbolGt;
    }

    public Date getModifiedSymbolGte() {
        return modifiedSymbolGte;
    }

    public void setModifiedSymbolGte(Date modifiedSymbolGte) {
        this.modifiedSymbolGte = modifiedSymbolGte;
    }

    public Date getModifiedSymbolLt() {
        return modifiedSymbolLt;
    }

    public void setModifiedSymbolLt(Date modifiedSymbolLt) {
        this.modifiedSymbolLt = modifiedSymbolLt;
    }

    public Date getModifiedSymbolLte() {
        return modifiedSymbolLte;
    }

    public void setModifiedSymbolLte(Date modifiedSymbolLte) {
        this.modifiedSymbolLte = modifiedSymbolLte;
    }
}
