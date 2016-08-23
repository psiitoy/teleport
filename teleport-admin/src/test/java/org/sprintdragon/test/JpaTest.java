package org.sprintdragon.test;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.sprintdragon.teleport.jpa.OrderTraceInfoMongoInfo;
import org.sprintdragon.teleport.jpa.repository.OrderTraceMongoInfoRepository;

import javax.annotation.Resource;

/**
 * Created by wangdi on 16-8-16.
 */
@ContextConfiguration(locations = "classpath:spring-config.xml")
@RunWith(value = SpringJUnit4ClassRunner.class)
public class JpaTest {

    @Resource
    OrderTraceMongoInfoRepository repository;

    @Test
    public void testGet() throws Exception {
        Pageable pageable = new PageRequest(1, 10);
        Page<OrderTraceInfoMongoInfo> page = repository.findAll(pageable);
        System.out.println("##" + page);
        pageable = new PageRequest(2, 10);
        page = repository.findAll(pageable);
        System.out.println("##" + page);
    }
}
