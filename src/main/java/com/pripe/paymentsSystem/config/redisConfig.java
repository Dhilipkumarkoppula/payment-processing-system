package com.pripe.paymentsSystem.config;
import com.pripe.paymentsSystem.entity.payment;
import org.springframework.boot.cache.autoconfigure.RedisCacheManagerBuilderCustomizer;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.cache.RedisCacheConfiguration;
import org.springframework.data.redis.serializer.RedisSerializationContext;
import org.springframework.data.redis.serializer.RedisSerializer;
import tools.jackson.databind.json.JsonMapper;
import java.time.Duration;
@Configuration(proxyBeanMethods = false)
public class redisConfig {
    @Bean
    public RedisCacheManagerBuilderCustomizer redisCacheManagerBuilderCustomizer() {
        JsonMapper Mapper = JsonMapper.builder().findAndAddModules().build();
        RedisSerializer<Object> Serializer = new RedisSerializer<>() {
            @Override
            public byte[] serialize(Object Value) {
                if (Value == null) return new byte[0];
                return Mapper.writeValueAsBytes(Value);
            }

            @Override
            public Object deserialize(byte[] Bytes) {
                if (Bytes == null || Bytes.length == 0) return null;
                return Mapper.readValue(Bytes, payment.class);
            }
        };
        return builder -> builder.withCacheConfiguration("payments",
                RedisCacheConfiguration.defaultCacheConfig().entryTtl(Duration.ofSeconds(300))
                .serializeValuesWith(RedisSerializationContext.SerializationPair.fromSerializer(Serializer)));
    }
}