package com.jvxb.search.configuration;

import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.stereotype.Component;

/**
 * 不检查本地的9200端口
 */
@Log4j2
@Component
public class ElasticSearchConfiguration implements InitializingBean {

    static {
        System.setProperty("es.set.netty.runtime.available.processors", "false");
    }


    @Override
    public void afterPropertiesSet() throws Exception {
        log.info("*****************es_config*************************");
        log.info("es.set.netty.runtime.available.processors: " + System.getProperty("es.set.netty.runtime.available" +
                ".processors"));
        log.info("***************************************************");
    }
}