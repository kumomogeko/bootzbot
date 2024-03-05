package bootz.gaming.bootzbot.infra.outbound.cassandra.staticview;

import bootz.gaming.bootzbot.domain.discord.StaticTeamView;
import bootz.gaming.bootzbot.domain.discord.StaticViewRepository;
import discord4j.common.util.Snowflake;
import org.springframework.context.annotation.Profile;
import org.springframework.data.cassandra.core.mapping.BasicMapId;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Mono;

import java.util.List;

@Component
@Profile("cassandra")
public class CStaticViewRepoImpl implements StaticViewRepository {

    private final CassandraViewRepo repo;

    public CStaticViewRepoImpl(CassandraViewRepo repo) {
        this.repo = repo;
    }

    @Override
    public Mono<Void> save(StaticTeamView view) {
        return repo.save(CStaticView.fromStaticTeamView(view)).then();
    }

    @Override
    public Mono<Void> delete(Long guild, Long channel) {
        return repo.deleteById(BasicMapId.id("id", guild.toString() + channel.toString()).with("guildId", guild));
    }

    @Override
    public Mono<List<StaticTeamView>> getByGuild(Snowflake guildId) {
        return repo.findCStaticViewByGuildId(guildId.asLong()).map(CStaticView::toStaticTeamView).collectList();
    }
}
