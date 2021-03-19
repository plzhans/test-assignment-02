package com.plzhans.assignment.api.config;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.DisposableBean;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Configuration;
import redis.embedded.RedisServer;

import java.util.Optional;

/**
 * The type Redis server config.
 */
@Slf4j
@Configuration()
@ConditionalOnProperty(name = "redis.embedded", havingValue = "true")
public class RedisEmbeddedConfig implements InitializingBean, DisposableBean {

    @Value("${spring.redis.port}")
    private int redisPort;

    private RedisServer redisServer;

    @Override
    public void afterPropertiesSet() throws Exception {
        redisServer = new RedisServer(redisPort);
        redisServer.start();
        this.log.info("embedded redis server start. port={}", redisPort);
    }

    @Override
    public void destroy() throws Exception {
        Optional.ofNullable(redisServer).ifPresent(RedisServer::stop);
        this.log.info("embedded redis server stop. port={}", redisPort);
    }


}
