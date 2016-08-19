package org.sprintdragon.teleport.persistent.query;

/**
 * 基本分页查询
 * Created by wangdi on 15-1-9.
 */
public class BaseQuery implements Query {

    private Integer index;
    private Integer pageSize;
    private Integer pageNo;

    public Integer getIndex() {
        return index;
    }

    public void setIndex(Integer index) {
        this.index = index;
    }

    public Integer getPageSize() {
        return pageSize;
    }

    public void setPageSize(Integer pageSize) {
        this.pageSize = pageSize;
    }

    @Override
    public Integer getPageNo() {
        return pageNo;
    }

    @Override
    public void setPageNo(Integer pageNo) {
        this.pageNo = pageNo;
    }

}
