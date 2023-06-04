package bootz.gaming.bootzbot.domain.discord;

import discord4j.common.util.Snowflake;
import reactor.core.publisher.Mono;

import java.util.List;

public interface StaticViewRepository {
    Mono<StaticTeamView> getById(String id);

    Mono<Void> save(StaticTeamView view);

    Mono<Void> delete(String id);

    Mono<List<StaticTeamView>> getByGuild(Snowflake guildId);
}
