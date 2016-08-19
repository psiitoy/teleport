package org.sprintdragon.teleport.persistent.generate;

import com.google.common.reflect.TypeToken;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.util.Assert;
import org.sprintdragon.teleport.common.utils.ReflectUtils;
import org.sprintdragon.teleport.persistent.attribute.BasicAttributeEnum;
import org.sprintdragon.teleport.persistent.dao.EntityDao;
import org.sprintdragon.teleport.persistent.page.PaginatedList;
import org.sprintdragon.teleport.persistent.query.DefaultBaseQuery;
import org.sprintdragon.teleport.persistent.query.Query;
import org.sprintdragon.teleport.persistent.query.domain.ColumnOrder;
import org.sprintdragon.teleport.persistent.query.domain.OrderByDescEnum;
import org.sprintdragon.teleport.persistent.utils.BasicAttributesUtils;

import javax.annotation.PostConstruct;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

/**
 * Created by wangdi on 15-7-17.
 */
public abstract class EntityDaoBaseTest<Domain, DomainQuery extends Query> {

    public final static Logger logger = LoggerFactory.getLogger(EntityDaoBaseTest.class);

    private final static int pageSize = 10;
    private final static int insertObjCount = 100;
    private final static int maxPageSize = 10000;

    public abstract EntityDao<Domain> getEntityDao();

    //测试数据id列表
    private List<Long> tempIdList;
    //数据库中原来的数据
    private int beforeCount;
    TypeToken<Domain> typeTokenDomain = new TypeToken<Domain>(getClass()) {
    };
    TypeToken<DomainQuery> typeTokenDomainQuery = new TypeToken<DomainQuery>(getClass()) {
    };

    @PostConstruct
    public void initBefore() {
        tempIdList = new ArrayList<Long>();
    }

    public void execute() throws Exception {
        try {
            beforeCount = testCountDomain();
            logger.info("[DB框架测试]系统中存在" + beforeCount + "条数据");
            logger.info("[DB框架测试]testCount 成功");
            testInsertDomain();
            logger.info("[DB框架测试]testInsert 成功");
            testQueryPageFirstAndLastDomain();
            logger.info("[DB框架测试]testQueryPage 成功");
            testQueryPageOrderByDescAndAscDomain();
            logger.info("[DB框架测试]testQueryPageOrderByDescAndAsc 成功");
            testGetAndUpdateDomain();
            logger.info("[DB框架测试]testGetAndUpdate 成功");
        } catch (Exception e) {
            logger.error("[DB框架测试]execute error", e);
        } finally {
            testDeleteDomain();
            logger.info("[DB框架测试]testDelete 成功 [finish]");
        }
    }

    private Domain newInstanceDomain() throws Exception {
        return (Domain) typeTokenDomain.getRawType().newInstance();
    }

    private DomainQuery newInstanceDomainQuery() throws Exception {
        return (DomainQuery) typeTokenDomainQuery.getRawType().newInstance();
    }

    public int testCountDomain() throws Exception {
        return getEntityDao().count(newInstanceDomain());
    }

    public void testInsertDomain() throws Exception {
        int count = testCountDomain();
        for (int i = 0; i < insertObjCount; i++) {
            Domain t = newInstanceDomain();
            t = ReflectUtils.setFieldNullToRandomValue(t);
            Long id = System.currentTimeMillis();
            BasicAttributesUtils.setBasicId(t, id);
            tempIdList.add(BasicAttributesUtils.getBasicId(t));
            getEntityDao().insert(t);
        }
        logger.info("[DB框架测试]插入" + insertObjCount + "条测试数据");
        int nowCount = (insertObjCount + count);
        logger.info("[DB框架测试]当前数据数量为" + nowCount);
        Assert.isTrue(getEntityDao().count(newInstanceDomain()) == nowCount);
        Assert.isTrue(getEntityDao().findBy(newInstanceDomain()).size() == nowCount);
        Query query = newInstanceDomainQuery();
        query.setPageNo(Query.FIRST_PAGE);
        query.setPageSize(maxPageSize);
        Assert.isTrue(getEntityDao().findByPage(query).size() == nowCount);
    }

    public void testGetAndUpdateDomain() throws Exception {
        Domain queryT = newInstanceDomain();
        BasicAttributesUtils.setBasicId(queryT, tempIdList.get(new Random().nextInt(tempIdList.size())));
        Domain t = getEntityDao().findBySingle(queryT);
        Assert.notNull(t);
        logger.info("[DB框架测试]更新前[id=" + BasicAttributesUtils.getBasicId(queryT) + ",created=" + BasicAttributesUtils.getCreated(queryT) + ",modify=" + BasicAttributesUtils.getModify(queryT) + "]");
        Assert.isTrue(getEntityDao().update(t) > 0);
        t = getEntityDao().findBySingle(queryT);
        logger.info("[DB框架测试]更新后[id=" + BasicAttributesUtils.getBasicId(queryT) + ",created=" + BasicAttributesUtils.getCreated(queryT) + ",modify=" + BasicAttributesUtils.getModify(queryT) + "]");
    }

    public int testQueryTotalPageDomain() throws Exception {
        int count = testCountDomain();
        Query query = newInstanceDomainQuery();
        query.setPageSize(pageSize);
        query.setIndex(null);
        query.setPageNo(Query.FIRST_PAGE);
        PaginatedList<Domain> pages = getEntityDao().findByPage(query);
        int mathTotalPage = count / pageSize;
        mathTotalPage += count % pageSize > 0 ? 1 : 0;
        Assert.isTrue(mathTotalPage == pages.getTotalPage());
        return pages.getTotalPage();
    }

    public void testQueryPageFirstAndLastDomain() throws Exception {
        int count = testCountDomain();
        int totalPage = testQueryTotalPageDomain();
        Query query = newInstanceDomainQuery();
        //第一页 从index 0开始
        query.setPageSize(pageSize);
        query.setIndex(null);
        query.setPageNo(Query.FIRST_PAGE);
        Long tempIdIndex1 = 0l;
        List<Domain> pages = getEntityDao().findByPage(query);
        Assert.isTrue(pages.size() == pageSize);
        logger.info("[DB框架测试]第" + query.getPageNo() + "页数据量为" + pages.size() + "[PAGESIZE=" + pageSize + ",count=" + count + "]");
        tempIdIndex1 = BasicAttributesUtils.getBasicId(pages.get(1));
        //第一页 从index 1开始 同第一次查询的第二个id
        query.setIndex(1);
        query.setPageNo(Query.FIRST_PAGE);
        pages = getEntityDao().findByPage(query);
        Assert.isTrue(BasicAttributesUtils.getBasicId(pages.get(0)).equals(tempIdIndex1));
        //最后一页
        query.setIndex(null);
        query.setPageNo(totalPage);
        pages = getEntityDao().findByPage(query);
        int lastPageSize = count % pageSize == 0 ? pageSize : count % pageSize;
        Assert.isTrue(pages.size() == lastPageSize);
        logger.info("[DB框架测试]第" + query.getPageNo() + "页数据量为" + pages.size() + "[PAGESIZE=" + pageSize + ",count=" + count + "]");
        //最后一页后一页
        query.setIndex(null);
        query.setPageNo(totalPage + 1);
        pages = getEntityDao().findByPage(query);
        Assert.isTrue(pages.size() == 0);
        logger.info("[DB框架测试]第" + query.getPageNo() + "页数据量为" + pages.size() + "[PAGESIZE=" + pageSize + ",count=" + count + "]");
    }

    public void testQueryPageOrderByDescAndAscDomain() throws Exception {
        Query query = newInstanceDomainQuery();
        if (query instanceof DefaultBaseQuery) {
            DefaultBaseQuery defaultBaseQuery = (DefaultBaseQuery) query;
            defaultBaseQuery.setPageSize(maxPageSize);
            defaultBaseQuery.setIndex(null);
            defaultBaseQuery.setPageNo(Query.FIRST_PAGE);
            defaultBaseQuery.addOrderBy(new ColumnOrder(OrderByDescEnum.DESC, BasicAttributeEnum.ID.getName()));
            List<Domain> pages = getEntityDao().findByPage(defaultBaseQuery);
            Assert.notEmpty(pages);
            Long idBuf = BasicAttributesUtils.getBasicId(pages.get(0));
            defaultBaseQuery.clearOrderBy();
            defaultBaseQuery.addOrderBy(new ColumnOrder(OrderByDescEnum.ASC, BasicAttributeEnum.ID.getName()));
            pages = getEntityDao().findByPage(defaultBaseQuery);
            Assert.notEmpty(pages);
            Assert.isTrue(idBuf.equals(BasicAttributesUtils.getBasicId(pages.get(pages.size() - 1))));
        }
    }

    public void testDeleteDomain() throws Exception {
        for (Long id : tempIdList) {
            Domain t = newInstanceDomain();
            BasicAttributesUtils.setBasicId(t, id);
            getEntityDao().delete(t);
        }
        logger.info("[DB框架测试]删除" + tempIdList.size() + "条数据");
        Query query = newInstanceDomainQuery();
        query.setPageSize(maxPageSize);
        query.setPageNo(Query.FIRST_PAGE);
        Assert.isTrue(getEntityDao().count(newInstanceDomain()) == beforeCount);
        Assert.isTrue(getEntityDao().findByPage(query).size() == beforeCount);
        Assert.isTrue(getEntityDao().findBy(newInstanceDomain()).size() == beforeCount);
        logger.info("[DB框架测试]删除后库中数据量为" + beforeCount);
    }

    public String generateSqlAndXml() {
        return GenerateSqlAndIbatisXmlTool.generate(typeTokenDomain.getRawType(), typeTokenDomainQuery.getRawType());
    }

    public String generateSqlAndXml(String tablePrefix) {
        return GenerateSqlAndIbatisXmlTool.generate(typeTokenDomain.getRawType(), typeTokenDomainQuery.getRawType(), tablePrefix);
    }

}
