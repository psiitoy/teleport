package org.sprintdragon.teleport.persistent.service;


import org.sprintdragon.teleport.persistent.page.PaginatedList;
import org.sprintdragon.teleport.persistent.query.Query;

import java.util.List;
import java.util.Map;

/**
 * Created by wangdi on 14-12-10.
 */
public interface EntityService<T> {

    /**
     * 依据主键获取记录
     *
     * @param ID
     * @return
     * @throws Exception
     */
    public T get(long ID) throws Exception;

    /**
     * 保存实体
     *
     * @param t
     * @throws org.springframework.dao.DataAccessException
     */
    public T insert(T t) throws Exception;

    /**
     * 更新实体
     *
     * @param t
     * @throws org.springframework.dao.DataAccessException
     */
    public int update(T t) throws Exception;

    /**
     * 删除实体
     *
     * @param t
     * @throws org.springframework.dao.DataAccessException
     */
    public int delete(T t) throws Exception;

    /**
     * 根据指定的条件查询记录
     *
     * @param t
     * @return
     * @throws org.springframework.dao.DataAccessException
     */
    public List<T> findBy(T t) throws Exception;

    /**
     * 根据指定的条件分页查询记录
     *
     * @param query
     * @throws org.springframework.dao.DataAccessException
     */
    public PaginatedList findByPage(Query query) throws Exception;

    public int count(T t) throws Exception;

    /**
     * 根据指定的条件分页查询记录 模糊查询
     *
     * @param paginatedList
     * @param queryMap
     * @throws org.springframework.dao.DataAccessException
     */
    public PaginatedList findByPageLike(PaginatedList paginatedList, Map<String, String> queryMap) throws Exception;
}
