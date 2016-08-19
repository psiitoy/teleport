package org.sprintdragon.teleport.admin.config;

import org.springframework.beans.BeansException;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;
import reactor.Environment;
import reactor.bus.EventBus;
import reactor.core.dispatch.ThreadPoolExecutorDispatcher;

import java.util.concurrent.LinkedBlockingDeque;
import java.util.concurrent.ThreadPoolExecutor;

/**
 * Title: <br/>
 * <p/>
 * Description: <br/>
 * <p/>
 * Date:15/9/11 Time:下午3:46
 * <p/>
 */
@Configuration
@PropertySource("classpath:reactor.properties")
public class Application implements ApplicationContextAware {

    public static ApplicationContext APPLICATION_CONTEXT;

    @Value("${reactor.dispatcher.consumer.poolSize}")
    private Integer consumerReactorPoolSize;

    @Value("${reactor.dispatcher.consumer.bufferSize}")
    private Integer consumerReactorBufferSize;

    @Value("${reactor.dispatcher.producer.poolSize}")
    private Integer producerReactorPoolSize;

    @Value("${reactor.dispatcher.producer.bufferSize}")
    private Integer producerReactorBufferSize;

    /**
     * poolSize 池容量
     * bufferSize 积压容量
     *
     * @return
     */
    @Bean
    Environment env() {
        return Environment.initializeIfEmpty()
                .assignErrorJournal().setDispatcher("consumerThreadDispatcher",
                        new ThreadPoolExecutorDispatcher(consumerReactorPoolSize, consumerReactorBufferSize, "reactor",
                                new LinkedBlockingDeque<Runnable>(consumerReactorBufferSize),
                                new ThreadPoolExecutor.CallerRunsPolicy())).setDispatcher("producerThreadDispatcher",
                        new ThreadPoolExecutorDispatcher(producerReactorPoolSize, producerReactorBufferSize, "reactor",
                                new LinkedBlockingDeque<Runnable>(producerReactorBufferSize),
                                new ThreadPoolExecutor.CallerRunsPolicy()));
    }

    @Bean(name = "consumerEventBus")
    EventBus consumerThreadDispatcher(Environment env) {
        return EventBus.create(env, Environment.dispatcher("consumerThreadDispatcher"));
    }

    @Bean(name = "producerEventBus")
    EventBus producerThreadDispatcher(Environment env) {
        return EventBus.create(env, Environment.dispatcher("producerThreadDispatcher"));
    }

    @Override
    public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
        Application.APPLICATION_CONTEXT = applicationContext;
    }
}
