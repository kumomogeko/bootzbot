package bootz.gaming.bootzbot.infra.outbound.redis.staticview;

import bootz.gaming.bootzbot.domain.discord.StaticTeamView;
import bootz.gaming.bootzbot.domain.discord.StaticViewRepository;
import discord4j.common.util.Snowflake;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Profile;
import org.springframework.data.redis.core.ReactiveRedisOperations;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Mono;

import java.util.List;

@Component
@Profile("redis")
public class RedisStaticViewRepoImpl implements StaticViewRepository {

    Logger log = LoggerFactory.getLogger(RedisStaticViewRepoImpl.class);

    private final ReactiveRedisOperations<String, StaticViewDBO> redisOperations;
    private static final String KEY = "VIEWS";

    public RedisStaticViewRepoImpl(ReactiveRedisOperations<String, StaticViewDBO> redisOperations) {
        this.redisOperations = redisOperations;
    }

    public Mono<StaticTeamView> getById(String id) {
        log.info("Entry getById");
        return redisOperations.opsForHash().get(KEY, id)
                .cast(StaticViewDBO.class)
                .map(StaticViewDBO::toStaticTeamView)
                .doOnSuccess(view -> log.info("Exit getById"));

    }

    @Override
    public Mono<Void> save(StaticTeamView view) {
        log.info("Entry save");
        var dbo = StaticViewDBO.fromStaticTeamView(view);
        return redisOperations.opsForHash().put(KEY, dbo.getId(), dbo)
                .doOnSuccess(u -> log.info("Exit save"))
                .then();
    }

    @Override
    public Mono<Void> delete(Long guild, Long channel) {
        log.info("Entry delete");
        return redisOperations.opsForHash().remove(KEY, guild.toString()+channel.toString())
                .doOnSuccess(u -> log.info("Exit delete"))
                .then();
    }

    //Fixme Implicit Behaviour of Id!
    @Override
    public Mono<List<StaticTeamView>> getByGuild(Snowflake guildId) {
        log.info("Entry getByGuild");
        return redisOperations.opsForHash().keys(KEY).cast(String.class)
                .filter(s -> s.startsWith(guildId.asString()))
                .flatMap(this::getById)
                .collectList()
                .doOnSuccess(view -> log.info("Exit getByGuild"));
    }
}
