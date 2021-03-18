package com.plzhans.assignment.api.server;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.DisposableBean;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import redis.embedded.RedisServer;

import java.util.Optional;

@Slf4j
@Configuration
public class RedisEmbedServerImpl implements InitializingBean, DisposableBean {
    @Value("${redis.server.port}")
    private int redisPort;

    private RedisServer redisServer;

    @Override
    public void afterPropertiesSet() throws Exception {
        if (redisPort > 0) {
            redisServer = new RedisServer(redisPort);
            redisServer.start();
            this.log.info("embedded redis server start. port=%d", redisPort);
        }
    }

    @Override
    public void destroy() throws Exception {
        if (redisPort > 0) {
            Optional.ofNullable(redisServer).ifPresent(RedisServer::stop);
            this.log.info("embedded redis server stop. port=%d", redisPort);
        }
    }


}
