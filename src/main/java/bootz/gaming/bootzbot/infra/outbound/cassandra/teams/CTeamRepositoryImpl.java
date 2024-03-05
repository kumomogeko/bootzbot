package bootz.gaming.bootzbot.infra.outbound.cassandra.teams;

import bootz.gaming.bootzbot.domain.teams.Team;
import bootz.gaming.bootzbot.domain.teams.TeamId;
import bootz.gaming.bootzbot.domain.teams.TeamRepository;
import org.springframework.data.cassandra.core.mapping.BasicMapId;
import org.springframework.data.cassandra.core.mapping.MapId;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Mono;

import java.util.List;

@Component
public class CTeamRepositoryImpl implements TeamRepository {

    private final CassandraTeamRepo cassandraTeamRepo;

    public CTeamRepositoryImpl(CassandraTeamRepo cassandraTeamRepo) {
        this.cassandraTeamRepo = cassandraTeamRepo;
    }

    @Override
    public Mono<Team> getTeamByTeamId(TeamId teamId) {
        return cassandraTeamRepo.findById(teamIdToMapId(teamId)).map(CTeam::toTeam);
    }

    private static MapId teamIdToMapId(TeamId teamId) {
        return BasicMapId.id("guildId", teamId.getGuild()).with("teamname", teamId.getName());
    }

    @Override
    public Mono<Void> save(Team team) {
        return cassandraTeamRepo.save(CTeam.fromTeam(team)).then();
    }

    @Override
    public Mono<Void> delete(TeamId teamId) {
        return cassandraTeamRepo.deleteById(teamIdToMapId(teamId));
    }

    @Override
    public Mono<List<Team>> getTeams() {
        return cassandraTeamRepo.findAll().map(CTeam::toTeam).collectList();
    }
}
