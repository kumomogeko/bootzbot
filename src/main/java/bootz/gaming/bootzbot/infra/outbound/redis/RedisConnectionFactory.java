package bootz.gaming.bootzbot.infra.outbound.redis;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;
import org.springframework.data.redis.connection.ReactiveRedisConnectionFactory;
import org.springframework.data.redis.connection.RedisStandaloneConfiguration;
import org.springframework.data.redis.connection.lettuce.LettuceConnectionFactory;

@Configuration
@Profile("redis")

public class RedisConnectionFactory {
    @Bean
    public ReactiveRedisConnectionFactory getLettuceConnectionFactory(RedisConfigurationProperties redisConfigurationProperties) {
        var config = new RedisStandaloneConfiguration(redisConfigurationProperties.getHost(), redisConfigurationProperties.getPort());
        config.setPassword(redisConfigurationProperties.getPass());
        var factory = new LettuceConnectionFactory(config);
        factory.afterPropertiesSet();
        return factory;
    }

}
