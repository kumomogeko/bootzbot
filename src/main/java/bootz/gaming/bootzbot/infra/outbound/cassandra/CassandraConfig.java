package bootz.gaming.bootzbot.infra.outbound.cassandra;

import org.springframework.beans.factory.BeanClassLoaderAware;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.cassandra.config.AbstractCassandraConfiguration;
import org.springframework.data.cassandra.config.SchemaAction;
import org.springframework.data.cassandra.core.cql.keyspace.CreateKeyspaceSpecification;
import org.springframework.data.cassandra.core.cql.keyspace.KeyspaceOption;
import org.springframework.data.cassandra.repository.config.EnableCassandraRepositories;

import java.util.Arrays;
import java.util.List;

@Configuration
@EnableCassandraRepositories(basePackages = {"bootz"})
public class CassandraConfig extends AbstractCassandraConfiguration implements BeanClassLoaderAware {

    private final String points;

    public CassandraConfig(@Value("${spring.cassandra.contact-points}") String points) {
        this.points = points;
    }

    @Override
    protected String getContactPoints() {
        return this.points;
    }

    @Override
    protected String getKeyspaceName() {
        return "bootzbot";
    }
    @Override
        protected List<CreateKeyspaceSpecification> getKeyspaceCreations() {

            CreateKeyspaceSpecification specification = CreateKeyspaceSpecification.createKeyspace("bootzbot")
                    .with(KeyspaceOption.DURABLE_WRITES, true)
                    .ifNotExists();

            return Arrays.asList(specification);
        }

    @Override
    public SchemaAction getSchemaAction() {
        return SchemaAction.CREATE_IF_NOT_EXISTS;
    }
}
