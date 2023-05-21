package bootz.gaming.bootzbot.infra.outbound.redis;

import bootz.gaming.bootzbot.infra.outbound.TeamDBO;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;
import org.springframework.data.redis.connection.ReactiveRedisConnectionFactory;
import org.springframework.data.redis.core.ReactiveRedisOperations;
import org.springframework.data.redis.core.ReactiveRedisTemplate;
import org.springframework.data.redis.serializer.Jackson2JsonRedisSerializer;
import org.springframework.data.redis.serializer.RedisSerializationContext;
import org.springframework.data.redis.serializer.StringRedisSerializer;

@Configuration
@Profile("redis")
public class RedisConfiguration {

    @Bean
    public ReactiveRedisOperations<String, TeamDBO> stringTeamDBOReactiveRedisOperations(ReactiveRedisConnectionFactory factory) {
        Jackson2JsonRedisSerializer<TeamDBO> serializer = new Jackson2JsonRedisSerializer<>(TeamDBO.class);
        Jackson2JsonRedisSerializer<String> stringSerializer = new Jackson2JsonRedisSerializer<>(String.class);

        RedisSerializationContext.RedisSerializationContextBuilder<String, TeamDBO> builder =
                RedisSerializationContext.newSerializationContext(new StringRedisSerializer());
        RedisSerializationContext<String, TeamDBO> context = builder
                .value(serializer)
                .hashKey(stringSerializer)
                .hashValue(serializer)
                .build();
        return new ReactiveRedisTemplate<>(factory, context);
    }


}
