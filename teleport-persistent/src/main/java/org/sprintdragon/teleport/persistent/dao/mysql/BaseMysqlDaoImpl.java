package org.sprintdragon.teleport.persistent.dao.mysql;

import com.google.common.base.Preconditions;
import com.google.common.reflect.TypeToken;
import org.springframework.dao.DataAccessException;
import org.sprintdragon.teleport.common.utils.CopyPropertyUtils;
import org.sprintdragon.teleport.persistent.dao.BaseDao;
import org.sprintdragon.teleport.persistent.exception.EntityDaoException;
import org.sprintdragon.teleport.persistent.page.PaginatedArrayList;
import org.sprintdragon.teleport.persistent.page.PaginatedList;
import org.sprintdragon.teleport.persistent.query.Query;
import org.sprintdragon.teleport.persistent.utils.BasicAttributesUtils;
import org.sprintdragon.teleport.persistent.utils.EntityNamesUtils;
import org.sprintdragon.teleport.persistent.utils.IdWorker;

import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.util.List;
import java.util.Map;

/**
 * 负责为单个Entity 提供CRUD操作的IBatis DAO基类.<br/> 子类只要在类定义时指定所管理Entity的Class,
 * 即拥有对单个Entity对象的CRUD操作.
 * <p/>
 * <pre>
 * public class UserDaoImpl extends EntityDaoImpl&lt;User&gt; {
 * }
 * </pre>
 *
 * @author wangdi
 * @see BaseDao, EntityDao
 */
public class BaseMysqlDaoImpl<T> extends BaseDao implements BaseMysqlDao<T> {//implements EntityDao<T> {

    protected static final String POSTFIX_FIND = ".Find";

    protected static final String POSTFIX_COUNT = ".Count";

    protected static final String POSTFIX_FIND_BY_PAGE = ".FindByPage";

    protected static final String POSTFIX_FIND_BY_PAGE_LIKE = ".FindByPageLike";

    protected static final String POSTFIX_SELECT_PRIAMARYKEY = ".Get";

    protected static final String POSTFIX_INSERT = ".Insert";

    protected static final String POSTFIX_UPDATE = ".Update";

    protected static final String POSTFIX_UPDATE_CAS_MODIFY = ".UpdateCasModify";

    protected static final String POSTFIX_DELETE = ".Delete";

    TypeToken<T> typeTokenDomain = new TypeToken<T>(getClass()) {
    };
    /**
     * DAO所管理的Entity类型.
     */
    protected Class<T> entityClass;
    /**
     * 命名空间 (同EntityDaoIBatisXmlUtil 生成的xml配套 建议不要自行赋值)
     */
    private String nameSpace;
    /**
     * snowflake Id生成器 begin
     */
    private IdWorker idWorker;
    //snowflake end

    public BaseMysqlDaoImpl() {
        try {
            entityClass = getSuperClassGenricType(getClass(), 0);
            nameSpace = EntityNamesUtils.getHumpClassNames(entityClass.getName());
        } catch (Exception e) {
            logger.error("BaseMysqlDaoImpl error", e);
        }
    }

    /*
    * 通过反射,获得定义Class时声明的父类的范型参数的类型.
    *
    * @param clazz clazz The class to introspect @param index the Index of the
    * generic ddeclaration,start from 0. @return the index generic declaration,
    * or <code>Object.class</code> if cannot be determined
    */
    private static Class getSuperClassGenricType(Class clazz, int index) {
        Type genType = clazz.getGenericSuperclass();
        if (!(genType instanceof ParameterizedType)) {
            return Object.class;
        }
        Type[] params = ((ParameterizedType) genType).getActualTypeArguments();
        if (index >= params.length || index < 0) {
            return Object.class;
        }
        if (!(params[index] instanceof Class)) {
            return Object.class;
        }
        return (Class) params[index];
    }

    /**
     * 根据主键获得单个对象
     */
    public T get(long ID) throws EntityDaoException {
        try {
            return (T) queryForObject(nameSpace + POSTFIX_SELECT_PRIAMARYKEY, ID);
        } catch (Exception e) {
            throw new EntityDaoException(e);
        }
    }

    /*
     * 根据条件查询对象（无分页）
     *
     */
    public List<T> findBy(T t) throws EntityDaoException {
        try {
            return queryForList(nameSpace + POSTFIX_FIND, t);
        } catch (Exception e) {
            throw new EntityDaoException(e);
        }
    }

    @Override
    public T findBySingle(T t) throws EntityDaoException {
        try {
            return (T) queryForObject(nameSpace + POSTFIX_FIND, t);
        } catch (Exception e) {
            throw new EntityDaoException(e);
        }
    }

    /*
         * 根据条件查询对象（有分页）
         *
         */
    public PaginatedList<T> findByPage(Query query) throws EntityDaoException {
        try {
            return executeQueryForList(nameSpace + POSTFIX_FIND_BY_PAGE, query);
        } catch (Exception e) {
            throw new EntityDaoException(e);
        }
    }

    /**
     * 根据条件查询对象（有分页）TODO
     *
     * @param sqlID
     * @param query 包含所有字段查询条件
     * @return
     * @throws EntityDaoException
     */
    public PaginatedList<T> findByPage(String sqlID, Query query) throws EntityDaoException {
        try {
            return this.executeQueryForList(sqlID, query);
        } catch (Exception e) {
            throw new EntityDaoException(e);
        }
    }

    /**
     * 带查询条件的分页
     */
    private PaginatedList<T> executeQueryForList(String selStmName, Query query) throws EntityDaoException {
        Preconditions.checkNotNull(selStmName, "selStmName必须赋值");
        Preconditions.checkNotNull(query, "query必须赋值");
        Preconditions.checkNotNull(query.getPageNo(), "query的pageNo和pageSize必须赋值,pageNo从1起(0同1)");
        Preconditions.checkNotNull(query.getPageSize(), "query的pageNo和pageSize必须赋值,pageNo从1起(0同1)");

        PaginatedList paginatedList = new PaginatedArrayList();
        try {
            // 得到数据记录总数
            Integer countNumber = count(CopyPropertyUtils.copyPropertiesAndInstance(query, entityClass));
            if (countNumber != null) {
                // 设置当前查询的记录总数
                paginatedList.setTotalItem(countNumber.intValue());
                paginatedList.setPageSize(query.getPageSize());
//				// 验证当前页数的合法性
//				query.validatePageInfo();
                // 得到当前页的数据集
                if (query.getIndex() == null) {
                    if (query.getPageNo() < 1) {
                        //page从1开始
                        query.setPageNo(1);
                    }
                    query.setIndex((query.getPageNo() - 1) * query.getPageSize());
                }
                //设置当前页
                paginatedList.setIndex(query.getPageNo());
                paginatedList.addAll(queryForList(selStmName, query));
            }
        } catch (DataAccessException e) {
            throw new EntityDaoException(e);
        }

        return paginatedList;
    }

    /*
     * 保存对象
     *
     */
    public T insert(T t) throws EntityDaoException {
        try {
            if (BasicAttributesUtils.getBasicId(t) == null) {
                //snowflake配置了(构造需要 机器编号+数据中心编号)则使用
                if (idWorker != null) {
                    BasicAttributesUtils.setBasicId(t, idWorker.nextId());
                    //否则使用时间戳
                } else {
                    BasicAttributesUtils.setBasicId(t, System.currentTimeMillis());
                }
            }
            insert(nameSpace + POSTFIX_INSERT, t);
            return t;
        } catch (Exception e) {
            throw new EntityDaoException(e);
        }
    }

    /*
     * 删除对象
     */
    public int delete(T t) throws EntityDaoException {
        try {
            return update(nameSpace + POSTFIX_DELETE, t);
        } catch (Exception e) {
            throw new EntityDaoException(e);
        }
    }

    /*
     * 更新对象
     *
     */
    public int update(T t) throws EntityDaoException {
        try {
            return update(nameSpace + POSTFIX_UPDATE, t);
        } catch (Exception e) {
            throw new EntityDaoException(e);
        }
    }

    @Override
    public int updateCasByModified(T t) throws EntityDaoException {
        try {
            return update(nameSpace + POSTFIX_UPDATE_CAS_MODIFY, t);
        } catch (Exception e) {
            throw new EntityDaoException(e);
        }
    }

    public int count(T t) throws EntityDaoException {
        try {
            Integer countNumber = (Integer) queryForObject(nameSpace + POSTFIX_COUNT, t);
            if (countNumber == null) {
                return 0;
            } else {
                return countNumber;
            }
        } catch (Exception e) {
            throw new EntityDaoException(e);
        }
    }

    @Override
    public Class getDomainClass() {
        return typeTokenDomain.getRawType();
    }

    public PaginatedList findByPageLike(Map<String, String> queryMap) throws EntityDaoException {
        //TODO 模糊查询
        try {
            return executeQueryForList(nameSpace + POSTFIX_FIND_BY_PAGE_LIKE, null);
        } catch (Exception e) {
            throw new EntityDaoException(e);
        }
    }

    @Override
    public String getAbout() {
        return "DB[mysql]-Class[" + getDomainClass() + "]-TableName[" + EntityNamesUtils.getSQLTableName(typeTokenDomain.getRawType().getSimpleName(), null) + "]";
    }

    public void setIdWorker(IdWorker idWorker) {
        this.idWorker = idWorker;
    }
}
