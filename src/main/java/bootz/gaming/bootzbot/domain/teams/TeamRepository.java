package bootz.gaming.bootzbot.domain.teams;

import reactor.core.publisher.Mono;

import java.util.List;

public interface TeamRepository {
    //Mono<Team> getTeamByGuildAndName(Long guildId, String name);
    Mono<Team> getTeamByTeamId(TeamId teamId);

    Mono<Void> save(Team team);

    Mono<Void> delete(TeamId teamId);

    Mono<List<Team>> getTeams();
}
