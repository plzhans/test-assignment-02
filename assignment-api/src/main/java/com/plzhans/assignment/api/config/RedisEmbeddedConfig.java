package com.plzhans.assignment.api.config;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Configuration;
import org.springframework.stereotype.Component;
import redis.embedded.RedisServer;

import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;
import java.util.Optional;

/**
 * The type Redis server config.
 */
@Slf4j
@Configuration()
public class RedisEmbeddedConfig{

    @Value("${redis.embedded}")
    private boolean embedded;

    @Value("${spring.redis.port}")
    private int redisPort;

    private RedisServer redisServer;

    @PostConstruct
    public void afterPropertiesSet() throws Exception {
        if(embedded == false){
            this.log.info("embedded redis server pass.");
            return;
        }
        redisServer = new RedisServer(redisPort);

        this.log.info("embedded redis server begin. port={}", redisPort);
        redisServer.start();
        if(!redisServer.isActive()){
            this.log.error("embedded redis server fail.");
            throw new Exception("embedded redis start fail.");
        }
        this.log.info("embedded redis server start.");
    }

    @PreDestroy
    public void destroy() throws Exception {
        Optional.ofNullable(redisServer).ifPresent(RedisServer::stop);
        this.log.info("embedded redis server stop. port={}", redisPort);
    }


}
