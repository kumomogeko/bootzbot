package bootz.gaming.bootzbot.domain.teams.teamlinks;

import bootz.gaming.bootzbot.util.ValueObject;
import org.springframework.data.cassandra.core.mapping.UserDefinedType;

import java.util.Objects;

@ValueObject
@UserDefinedType("teamlink")
public class Teamlink {
    private final String name;
    private final String link;

    public Teamlink(String name, String link) {
        this.name = name;
        this.link = link;
    }

    public String getName() {
        return name;
    }

    public String getLink() {
        return this.link;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Teamlink teamlink = (Teamlink) o;
        return Objects.equals(name, teamlink.name) && Objects.equals(link, teamlink.link);
    }

    @Override
    public int hashCode() {
        return Objects.hash(name, link);
    }
}
