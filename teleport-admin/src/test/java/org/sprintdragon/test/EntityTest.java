package org.sprintdragon.test;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.sprintdragon.teleport.admin.entity.domain.OrderTraceInfoMysqlBean;
import org.sprintdragon.teleport.admin.entity.domain.OrderTraceInfoMysqlQuery;
import org.sprintdragon.teleport.common.utils.CommonTimeUtils;
import org.sprintdragon.teleport.persistent.dao.EntityDao;
import org.sprintdragon.teleport.persistent.generate.EntityDaoBaseTest;

import javax.annotation.Resource;
import java.util.List;

/**
 * Created by wangdi on 16-8-12.
 */
@ContextConfiguration(locations = "classpath:spring-config.xml")
@RunWith(value = SpringJUnit4ClassRunner.class)
public class EntityTest extends EntityDaoBaseTest<OrderTraceInfoMysqlBean, OrderTraceInfoMysqlQuery> {

    @Resource
    EntityDao<OrderTraceInfoMysqlBean> orderTraceInfoMysqlDao;


    @Override
    public EntityDao<OrderTraceInfoMysqlBean> getEntityDao() {
        return orderTraceInfoMysqlDao;
    }

    @Test
    public void testExecute() throws Exception {
        execute();
    }

    @Test
    public void testGenerate() throws Exception {
        System.out.println(generateSqlAndXml());
    }

    @Test
    public void testQuery() throws Exception {
        OrderTraceInfoMysqlQuery query = new OrderTraceInfoMysqlQuery();
        query.setPageNo(1);
        query.setPageSize(20);
        query.setCreatedSymbolGte(CommonTimeUtils.getDateFromStr("2014-8-8 10:00:00"));
        query.setCreatedSymbolLte(CommonTimeUtils.getDateFromStr("2016-8-12 10:00:00"));
        List<OrderTraceInfoMysqlBean> list = orderTraceInfoMysqlDao.findByPage(query);
        System.out.println("#" + list);
    }
}
