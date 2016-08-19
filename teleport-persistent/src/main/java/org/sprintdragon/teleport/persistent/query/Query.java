package org.sprintdragon.teleport.persistent.query;

/**
 * 带分页查询基本组件
 * Created by wangdi on 14-12-10.
 */
public interface Query {
    //首页从1开始(兼容0)
    public static final int FIRST_PAGE = 1;

    /**
     * 页编号
     *
     * @return row
     */
    public Integer getIndex();

    public void setIndex(Integer index);

    /**
     * 页大小
     *
     * @return row
     */
    public Integer getPageSize();

    public void setPageSize(Integer pageSize);

    public Integer getPageNo();

    public void setPageNo(Integer pageNo);

}
