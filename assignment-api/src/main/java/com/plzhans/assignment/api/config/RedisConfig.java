package com.plzhans.assignment.api.config;

import lombok.extern.slf4j.Slf4j;
import lombok.val;
import org.redisson.Redisson;
import org.redisson.api.RedissonClient;
import org.redisson.config.Config;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.connection.lettuce.LettuceConnectionFactory;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.repository.configuration.EnableRedisRepositories;
import org.springframework.data.redis.serializer.StringRedisSerializer;

@Slf4j
@Configuration
@EnableRedisRepositories
public class RedisConfig {
    @Value("${spring.redis.host}")
    private String redisHost;

    @Value("${spring.redis.port}")
    private int redisPort;

    @Autowired
    RedisEmbeddedConfig redisEmbeddedConfig;

    @Bean
    public RedisConnectionFactory redisConnectionFactory() {
        LettuceConnectionFactory factory = new LettuceConnectionFactory(redisHost, redisPort);
        return factory;
    }

    @Bean
    public RedisTemplate<?, ?> redisTemplate() {
        RedisTemplate<byte[], byte[]> redisTemplate = new RedisTemplate<>();
        redisTemplate.setConnectionFactory(redisConnectionFactory());
        redisTemplate.setKeySerializer(new StringRedisSerializer());
        //redisTemplate.setValueSerializer(new Jackson2JsonRedisSerializer());
        return redisTemplate;
    }

    @Bean
    public RedissonClient redissonClient() {
        if(redisEmbeddedConfig == null){
            log.info("redis external use.");
        } else {
            log.info("redis embedded use.");
        }
        val config = new Config();
        //config.setCodec(StringCodec.INSTANCE);
        config.useSingleServer()
                .setAddress(String.format("redis://%s:%d", redisHost, redisPort))
                //.setPassword()
                .setDatabase(0)
                .setTimeout(3000);
        return Redisson.create(config);
    }

}
