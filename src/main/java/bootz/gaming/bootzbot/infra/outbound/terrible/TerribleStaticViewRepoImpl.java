package bootz.gaming.bootzbot.infra.outbound.terrible;

import bootz.gaming.bootzbot.domain.discord.StaticTeamView;
import bootz.gaming.bootzbot.domain.discord.StaticViewRepository;
import discord4j.common.util.Snowflake;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Mono;

import java.util.HashMap;
import java.util.List;
import java.util.stream.Collectors;

@Component
@Profile({"inmem"})
public class TerribleStaticViewRepoImpl implements StaticViewRepository {

    private final HashMap<String, StaticTeamView> repo;

    public TerribleStaticViewRepoImpl() {
        this.repo = new HashMap<>();
    }

    public Mono<StaticTeamView> getById(String id) {
        return Mono.justOrEmpty(repo.get(id));
    }

    @Override
    public Mono<Void> save(StaticTeamView view) {
        this.repo.put(view.id(), view);
        return Mono.empty().then();
    }

    @Override
    public Mono<Void> delete(Long guild, Long channel) {
        this.repo.remove(guild.toString()+channel.toString());
        return Mono.empty().then();
    }

    @Override
    public Mono<List<StaticTeamView>> getByGuild(Snowflake guildId) {
        return Mono.justOrEmpty(this.repo.values().stream().filter(view -> view.guild().equals(guildId)).collect(Collectors.toList()));
    }
}
