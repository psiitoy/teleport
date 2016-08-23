//package org.sprintdragon.test;
//
//import org.junit.Before;
//import org.junit.Test;
//import org.junit.runner.RunWith;
//import org.springframework.test.context.ContextConfiguration;
//import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
//import org.sprintdragon.teleport.admin.entity.domain.OrderTraceInfoMongoQuery;
//import org.sprintdragon.teleport.admin.entity.domain.OrderTraceInfoMysqlBean;
//import org.sprintdragon.teleport.common.utils.CopyPropertyUtils;
//import org.sprintdragon.teleport.jpa.OrderTraceEsInfo;
//import org.sprintdragon.teleport.jpa.repository.OrderTraceEsInfoRepository;
//import org.sprintdragon.teleport.persistent.dao.EntityDao;
//import org.sprintdragon.teleport.persistent.query.Query;
//
//import javax.annotation.Resource;
//import java.util.ArrayList;
//import java.util.List;
//
///**
// * Created by wangdi on 16-8-16.
// */
//@ContextConfiguration(locations = "classpath:spring-config.xml")
//@RunWith(value = SpringJUnit4ClassRunner.class)
//public class EsTest {
//
//    @Resource
//    OrderTraceEsInfoRepository repository;
//
//    @Resource
//    EntityDao<OrderTraceInfoMysqlBean> orderTraceInfoMysqlDao;
//
////    @Before
//    public void emptyData() throws Exception {
//        repository.deleteAll();
//    }
//
//    @Test
//    public void testSave1() throws Exception {
//        Query query = new OrderTraceInfoMongoQuery();
//        query.setPageSize(200);
//        query.setPageNo(1);
//        List<OrderTraceInfoMysqlBean> list = orderTraceInfoMysqlDao.findByPage(query);
//        System.out.println("##" + list);
//        repository.save(coverList(list));
//    }
//
//    private List<OrderTraceEsInfo> coverList(List<OrderTraceInfoMysqlBean> list) {
//        List<OrderTraceEsInfo> rList = new ArrayList<>();
//        for (OrderTraceInfoMysqlBean b : list) {
//            rList.add(CopyPropertyUtils.copyPropertiesAndInstance(b, OrderTraceEsInfo.class));
//        }
//        return rList;
//    }
//
//    @Test
//    public void testGet() throws Exception {
//        Iterable<OrderTraceEsInfo> list = repository.findAll();
//        int count = 0;
//        for (OrderTraceEsInfo a : list) {
//            count++;
//            System.out.println("##" + a);
//        }
//        System.out.println("##total=" + count);
//    }
//}
