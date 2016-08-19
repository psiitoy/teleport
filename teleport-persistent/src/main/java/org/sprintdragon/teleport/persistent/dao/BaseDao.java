package org.sprintdragon.teleport.persistent.dao;

import org.apache.commons.beanutils.BeanUtils;
import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.dao.DataAccessException;
import org.springframework.orm.ibatis.SqlMapClientTemplate;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * Created by wangdi on 14-12-9.
 */
public class BaseDao extends SqlMapClientTemplate {
    public final static Logger log = LoggerFactory.getLogger(BaseDao.class);

    /**
     * 主要用来避免1+n的查询。如在查询学生列表时，要得到学生的班主任信息。<br/>
     * 如果采用left join的方式，必然会导致从db到web传输数据量大。班主任被反复join过来。<br/>
     * 如果拿到学生列表后，再去一个个查询班主任信息。必然会导致1+n的查询<br/>
     * 这里的思想是：先查学生列表。然后再把学生信息中的班主任的id拼装成字符串。以in的方式查询(当然有更高效的方式)<br/>
     * 得到班主任列表后，再一个个塞回学生的list<br/>
     * <p/>
     * 查询集合，并且设置集合中的属性对象<br/>
     *
     * @param statementName      查询的集合名称
     * @param getFieldName       读取的ID
     * @param setFieldname       需要设置的对象
     * @param mergeFieldKeyName  父集合的对应属性
     * @param mergeStatementName 查询父集合的sqlmap的id
     * @return 带父属性的集合
     * @throws org.springframework.dao.DataAccessException when
     */
    public List queryForListMerge(String statementName, String getFieldName, String setFieldname, String mergeFieldKeyName, String mergeStatementName) throws DataAccessException {
        return queryForListMerge(statementName, null, getFieldName, setFieldname, mergeFieldKeyName, mergeStatementName);
    }


    /**
     * @see #queryForListMerge(String, Object, String, String, String, String)
     */
    public List queryForListMerge(String statementName, Object parameterObject, String getFieldName, String setFieldname, String mergeFieldKeyName, String mergeStatementName) throws DataAccessException {
        List list = queryForList(statementName, parameterObject); //查询子集合
        merge(list, getFieldName, setFieldname, mergeFieldKeyName, mergeStatementName);

        return list;
    }

    /**
     * @see #queryForListMerge(String, Object, String, String, String, String)
     */
    public void merge(List list, String getFieldName, String setFieldname, String mergeFieldKeyName, String mergeStatementName) {
        Set<String> values = new HashSet<String>(); //放的是查询到的集合的相关类的
        try {
            //集合类
            for (Object o : list) {
                String property = null;
                try {
                    property = BeanUtils.getProperty(o, getFieldName);
                    if (!StringUtils.isEmpty(property)) {
                        values.add(property);
                    }
                } catch (Exception e) {
                    log.debug("getProperty error! " + getFieldName);
                }
            }
            //查询到的父不为空
            if (!values.isEmpty()) {
                StringBuilder builder = new StringBuilder();
                //拼装成字符串
                for (String value : values) {
                    builder.append(',');
                    builder.append(value);
                }
                //查询父集合
                List merges = queryForList(mergeStatementName, builder.substring(1));
                for (Object obj : list) {
                    String val = null;
                    try {
                        val = BeanUtils.getProperty(obj, getFieldName);
                    } catch (Exception e) {
                        log.debug("getProperty error! " + getFieldName);
                    }
                    //塞回去
                    if (!StringUtils.isEmpty(val)) {
                        for (Object merge : merges) {
                            if (val.equals(BeanUtils.getProperty(merge, mergeFieldKeyName))) {
                                BeanUtils.setProperty(obj, setFieldname, merge);
                                break;
                            }
                        }
                    }
                }
            }
        } catch (Exception e) {
            log.error("merge error!", e);
            throw new RuntimeException("merge error!", e);
        }
    }
}
