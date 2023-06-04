package bootz.gaming.bootzbot.infra.outbound.team;

import bootz.gaming.bootzbot.domain.teams.Team;
import bootz.gaming.bootzbot.domain.teams.TeamId;
import bootz.gaming.bootzbot.domain.teams.TeamRepository;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Mono;

import java.util.HashMap;
import java.util.List;

@Component
@Profile("!redis")
public class TerribleTeamRepoImpl implements TeamRepository {

    private final HashMap<TeamId, Team> repo;

    public TerribleTeamRepoImpl() {
        this.repo = new HashMap<>();
    }

    @Override
    public Mono<Team> getTeamByTeamId(TeamId teamId) {
        return Mono.justOrEmpty(this.repo.get(teamId));
    }

    @Override
    public Mono<Void> save(Team team) {
        this.repo.put(TeamId.fromTeam(team), team);
        return Mono.empty().then();
    }

    @Override
    public Mono<Void> delete(TeamId teamId) {
        this.repo.remove(teamId);
        return Mono.empty().then();
    }

    @Override
    public Mono<List<Team>> getTeams() {
        return Mono.justOrEmpty(this.repo.values().stream().toList());
    }
}
