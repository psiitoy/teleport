package org.sprintdragon.test;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.sprintdragon.teleport.jpa.repository.OrderTraceInfoJpaRepository;

import javax.annotation.Resource;

/**
 * Created by wangdi on 16-8-16.
 */
@ContextConfiguration(locations = "classpath:spring-config.xml")
@RunWith(value = SpringJUnit4ClassRunner.class)
public class JpaTest {

    @Resource
    OrderTraceInfoJpaRepository repository;

    @Test
    public void testGet() throws Exception {
        System.out.println("##" + repository.findOne(766200478559182848l));
    }
}
