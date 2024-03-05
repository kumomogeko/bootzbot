package bootz.gaming.bootzbot.domain.discord;

import discord4j.common.util.Snowflake;
import reactor.core.publisher.Mono;

import java.util.List;

public interface StaticViewRepository {

    Mono<Void> save(StaticTeamView view);

    Mono<Void> delete(Long guild, Long channel);

    Mono<List<StaticTeamView>> getByGuild(Snowflake guildId);
}
