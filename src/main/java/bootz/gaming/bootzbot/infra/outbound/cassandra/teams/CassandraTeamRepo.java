package bootz.gaming.bootzbot.infra.outbound.cassandra.teams;

import org.springframework.data.cassandra.core.mapping.MapId;
import org.springframework.data.cassandra.repository.ReactiveCassandraRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CassandraTeamRepo extends ReactiveCassandraRepository<CTeam, MapId> {

}
