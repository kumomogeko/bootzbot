package bootz.gaming.bootzbot.infra.outbound.redis;

import bootz.gaming.bootzbot.domain.teams.Team;
import bootz.gaming.bootzbot.domain.teams.TeamId;
import bootz.gaming.bootzbot.domain.teams.TeamRepository;
import bootz.gaming.bootzbot.infra.outbound.TeamDB;
import bootz.gaming.bootzbot.infra.outbound.TeamDBO;
import org.springframework.context.annotation.Profile;
import org.springframework.data.redis.core.ReactiveRedisOperations;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Mono;

import java.util.List;

@Component
@Profile("redis")
public class RedisTeamRepositoryImpl implements TeamRepository {
    private final ReactiveRedisOperations<String, TeamDBO> redisOperations;
    private static final String KEY = "TEAMS";

    public RedisTeamRepositoryImpl(ReactiveRedisOperations<String, TeamDBO> redisOperations) {
        this.redisOperations = redisOperations;
    }

    @Override
    public Mono<Team> getTeamByTeamId(TeamId teamId) {
        return redisOperations.opsForHash().get(KEY, teamId.toString()).cast(TeamDBO.class).map(TeamDBO::getTeam).map(TeamDB::toTeam);
    }

    @Override
    public Mono<Void> save(Team team) {
        var teamdId = TeamId.fromTeam(team);
        var dbo = new TeamDBO(teamdId.toString(), teamdId, team);
        return redisOperations.opsForHash().put(KEY,dbo.id,dbo).then();
    }

    @Override
    public Mono<Void> delete(TeamId teamId) {
        return redisOperations.opsForHash().remove(KEY,teamId.toString()).then();
    }

    @Override
    public Mono<List<Team>> getTeams() {
        return this.redisOperations.opsForHash().values(KEY)
                .cast(TeamDBO.class)
                .map(TeamDBO::getTeam)
                .map(TeamDB::toTeam)
                .collectList();
    }
}
