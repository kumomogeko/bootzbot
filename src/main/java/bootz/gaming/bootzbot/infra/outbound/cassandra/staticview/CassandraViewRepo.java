package bootz.gaming.bootzbot.infra.outbound.cassandra.staticview;

import org.springframework.data.cassandra.core.mapping.MapId;
import org.springframework.data.cassandra.repository.ReactiveCassandraRepository;
import reactor.core.publisher.Flux;

public interface CassandraViewRepo extends ReactiveCassandraRepository<CStaticView, MapId> {
    Flux<CStaticView> findCStaticViewByGuildId(Long guild);
}
