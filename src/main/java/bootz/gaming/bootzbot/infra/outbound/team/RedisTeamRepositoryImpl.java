package bootz.gaming.bootzbot.infra.outbound.team;

import bootz.gaming.bootzbot.domain.teams.Team;
import bootz.gaming.bootzbot.domain.teams.TeamId;
import bootz.gaming.bootzbot.domain.teams.TeamRepository;
import bootz.gaming.bootzbot.infra.outbound.team.dbo.TeamDB;
import bootz.gaming.bootzbot.infra.outbound.team.dbo.TeamDBO;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Profile;
import org.springframework.data.redis.core.ReactiveRedisOperations;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Mono;

import java.util.List;

@Component
@Profile("redis")
public class RedisTeamRepositoryImpl implements TeamRepository {

    Logger log = LoggerFactory.getLogger(RedisTeamRepositoryImpl.class);
    private final ReactiveRedisOperations<String, TeamDBO> redisOperations;
    private static final String KEY = "TEAMS";

    public RedisTeamRepositoryImpl(ReactiveRedisOperations<String, TeamDBO> redisOperations) {
        this.redisOperations = redisOperations;
    }

    @Override
    public Mono<Team> getTeamByTeamId(TeamId teamId) {
        log.info("Entry getTeamByTeamId");
        return redisOperations.opsForHash().get(KEY, teamId.toString())
                .cast(TeamDBO.class)
                .map(TeamDBO::getTeam)
                .map(TeamDB::toTeam)
                .doOnSuccess(team -> log.info("Exit getTeamByTeamId"));
    }

    @Override
    public Mono<Void> save(Team team) {
        log.info("Entry save");
        var teamId = TeamId.fromTeam(team);
        var dbo = new TeamDBO(teamId.toString(), teamId, team);
        return redisOperations.opsForHash().put(KEY, dbo.id, dbo)
                .doOnSuccess(aBoolean -> log.info("Exit save"))
                .then();
    }

    @Override
    public Mono<Void> delete(TeamId teamId) {
        log.info("Entry delete");
        return redisOperations.opsForHash().remove(KEY, teamId.toString())
                .doOnSuccess(aLong -> log.info("Exit delete"))
                .then();
    }

    @Override
    public Mono<List<Team>> getTeams() {
        log.info("Entry getTeams");
        return this.redisOperations.opsForHash().values(KEY)
                .cast(TeamDBO.class)
                .map(TeamDBO::getTeam)
                .map(TeamDB::toTeam)
                .collectList()
                .doOnSuccess(teams -> log.info("Exit getTeams"));
    }
}
