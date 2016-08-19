package org.sprintdragon.test;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.sprintdragon.teleport.admin.entity.dao.OrderTraceInfoMongoDao;
import org.sprintdragon.teleport.admin.entity.domain.OrderTraceInfoMongoBean;
import org.sprintdragon.teleport.common.utils.CommonTimeUtils;
import org.sprintdragon.teleport.persistent.query.DefaultBaseQuery;
import org.sprintdragon.teleport.persistent.utils.IdWorker;

import javax.annotation.Resource;
import java.util.Date;
import java.util.Random;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.atomic.AtomicLong;

/**
 * Created by wangdi on 16-8-16.
 */
@ContextConfiguration(locations = "classpath:spring-config.xml")
@RunWith(value = SpringJUnit4ClassRunner.class)
public class SmMongoTest {

    @Resource
    OrderTraceInfoMongoDao orderTraceInfoMongoDao;

    AtomicLong nm = new AtomicLong();
    final int executors = 4;
    final long wait_time = 1000 * 60 * 30;

    @Resource
    IdWorker idWorker;


    private OrderTraceInfoMongoBean ranDomain() {
        OrderTraceInfoMongoBean o = new OrderTraceInfoMongoBean();
        o.setOrderId(idWorker.nextId());
        o.setCustomsId("guangzhou");
        o.setType(1);
        o.setCaiFuTongStatus(2);
        o.setClearanceTime(System.currentTimeMillis());
        Date date = new Date();
        o.setCreated(date.getTime());
        o.setCreatedTime(CommonTimeUtils.getSimpleDateFormatDateTime(date));
        o.setModified(date.getTime());
        o.setModifiedTime(CommonTimeUtils.getSimpleDateFormatDateTime(date));
        o.setCustomsModel("beihuo");
        o.setCustomsregioncode(new Random().nextInt(1000) + "");
        o.setVenderId(new Random().nextLong());
        o.setProcessStatus(999);
        o.setProcessStatusMsg("成功");
        return o;
    }

    @Test
    public void testGet() throws Exception {
        new Thread(new Runnable() {
            @Override
            public void run() {
                while (true) {
                    long start = System.currentTimeMillis();
                    System.out.println("#" + nm.get());
                    try {
                        Thread.sleep(60 * 1000 - (System.currentTimeMillis() - start));
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
            }
        }).start();

        long start = System.currentTimeMillis();
        ExecutorService executorService = Executors.newFixedThreadPool(executors);
        for (int i = 0; i < executors; i++) {
            executorService.submit(new Runnable() {
                @Override
                public void run() {
                    for (int j = 0; j < 1000000; j++) {
                        orderTraceInfoMongoDao.insert(ranDomain());
                        nm.incrementAndGet();
                        try {
                            Thread.sleep(1);
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }
                    }
                }
            });
            System.out.println("已提交线程 " + (i + 1));
            Thread.sleep(i * 2 * 1000);
        }
        Thread.sleep(wait_time - (System.currentTimeMillis() - start));
    }

    @Test
    public void testCount() throws Exception {
        DefaultBaseQuery query = new DefaultBaseQuery();
        query.setPageNo(1);
        query.setPageSize(20);
        query.setCreatedSymbolGte(CommonTimeUtils.getDateFromStr("2014-6-6 10:00:00"));
        query.setCreatedSymbolLte(CommonTimeUtils.getDateFromStr("2017-6-6 10:00:00"));
        System.out.println("##" + orderTraceInfoMongoDao.findByPage(query));

    }

    @Test
    public void testCounbt2() throws Exception {
        OrderTraceInfoMongoBean query = new OrderTraceInfoMongoBean();
        query.setOrderId(20717657418l);
        System.out.println("##" + orderTraceInfoMongoDao.findBy(query));
    }
}
