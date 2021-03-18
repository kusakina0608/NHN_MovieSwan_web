package com.nhn.rookie8.movieswanticketapp.configuration;

import com.nhn.rookie8.movieswanticketapp.dto.MemberIdNameDTO;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;
import org.springframework.context.annotation.Primary;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.serializer.Jackson2JsonRedisSerializer;
import org.springframework.data.redis.serializer.StringRedisSerializer;

@Log4j2
@Configuration
@Import(MvcConfiguration.class)
public class RedisConfiguration {

    @Autowired
    private RedisConnectionFactory redisConnectionFactory0;

    @Autowired
    @Qualifier("redisConnectionFactory1")
    private RedisConnectionFactory redisConnectionFactory1;

    @Bean
    @Primary
    public RedisTemplate<String, MemberIdNameDTO> redisTemplateAuth() {
        RedisTemplate<String, MemberIdNameDTO> redisTemplate = new RedisTemplate<>();
        redisTemplate.setConnectionFactory(redisConnectionFactory0);
        redisTemplate.setKeySerializer(new StringRedisSerializer());
        redisTemplate.setValueSerializer(new Jackson2JsonRedisSerializer<>(MemberIdNameDTO.class));
        return redisTemplate;
    }

    @Bean
    @Qualifier("redisTemplate1")
    public RedisTemplate<String, String> redisTemplateAuthCheck() {
        RedisTemplate<String, String> redisTemplate = new RedisTemplate<>();
        redisTemplate.setConnectionFactory(redisConnectionFactory1);
        redisTemplate.setKeySerializer(new StringRedisSerializer());
        redisTemplate.setValueSerializer(new StringRedisSerializer());
        return redisTemplate;
    }
}
