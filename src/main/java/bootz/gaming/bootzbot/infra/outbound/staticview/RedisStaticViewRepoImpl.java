package bootz.gaming.bootzbot.infra.outbound.staticview;

import bootz.gaming.bootzbot.domain.discord.StaticTeamView;
import bootz.gaming.bootzbot.domain.discord.StaticViewRepository;
import discord4j.common.util.Snowflake;
import org.springframework.data.redis.core.ReactiveRedisOperations;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Mono;

import java.util.List;

@Component
public class RedisStaticViewRepoImpl implements StaticViewRepository {

    private final ReactiveRedisOperations<String, StaticViewDBO> redisOperations;
    private static final String KEY = "VIEWS";

    public RedisStaticViewRepoImpl(ReactiveRedisOperations<String, StaticViewDBO> redisOperations) {
        this.redisOperations = redisOperations;
    }

    @Override
    public Mono<StaticTeamView> getById(String id) {
        return redisOperations.opsForHash().get(KEY, id).cast(StaticViewDBO.class).map(StaticViewDBO::toStaticTeamView);

    }

    @Override
    public Mono<Void> save(StaticTeamView view) {
        var dbo = StaticViewDBO.fromStaticTeamView(view);
        return redisOperations.opsForHash().put(KEY, dbo.getId(), dbo).then();
    }

    @Override
    public Mono<Void> delete(String id) {
        return redisOperations.opsForHash().remove(KEY, id).then();
    }

    //Fixme Implicit Behaviour of Id!
    @Override
    public Mono<List<StaticTeamView>> getByGuild(Snowflake guildId) {
        return redisOperations.opsForHash().keys(KEY).cast(String.class)
                .filter(s -> s.startsWith(guildId.asString()))
                .flatMap(this::getById).collectList();
    }
}
