package bootz.gaming.bootzbot.infra.outbound.cassandra.teams;

import bootz.gaming.bootzbot.domain.teams.Team;
import bootz.gaming.bootzbot.domain.teams.teamlinks.Teamlink;
import org.springframework.data.cassandra.core.cql.Ordering;
import org.springframework.data.cassandra.core.cql.PrimaryKeyType;
import org.springframework.data.cassandra.core.mapping.*;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Table("teams")
public class CTeam implements MapIdentifiable {

    @PrimaryKeyColumn(
            name = "guild",
            ordinal = 0,
            type = PrimaryKeyType.PARTITIONED
    )
    private Long guildId;

    @PrimaryKeyColumn(
            name = "teamname",
            ordinal = 1,
            type = PrimaryKeyType.CLUSTERED,
            ordering = Ordering.ASCENDING
    )
    private String teamname;

    @Column
    private List<CTeammitglied> members = new ArrayList<>();

    @Column
    private Map<String, Teamlink> links = new HashMap<>();

    @Column
    private String customOpggTeamlinkRef;

    public Long getGuildId() {
        return guildId;
    }

    public void setGuildId(Long guildId) {
        this.guildId = guildId;
    }

    public String getTeamname() {
        return teamname;
    }

    public void setTeamname(String teamname) {
        this.teamname = teamname;
    }

    public List<CTeammitglied> getMembers() {
        return members != null ? members: new ArrayList<>();
    }

    public void setMembers(List<CTeammitglied> members) {
        this.members = members;
    }

    public Map<String, Teamlink> getLinks() {
        return links != null ? links: new HashMap<>();
    }

    public void setLinks(Map<String, Teamlink> links) {
        this.links = links;
    }

    public String getCustomOpggTeamlinkRef() {
        return customOpggTeamlinkRef;
    }

    public void setCustomOpggTeamlinkRef(String customOpggTeamlinkRef) {
        this.customOpggTeamlinkRef = customOpggTeamlinkRef;
    }

    public Team toTeam() {
        return new Team(this.getGuildId(),
                this.getTeamname(),
                new ArrayList<>(this.getMembers().stream().map(CTeammitglied::toTeammitglied).toList()),
                new HashMap<>(this.getLinks()), this.customOpggTeamlinkRef);
    }

    public static CTeam fromTeam(Team team) {
        var dbo = new CTeam();
        dbo.setGuildId(team.getGuildId());
        dbo.setTeamname(team.getTeamname());
        dbo.setLinks(team.getLinks());
        dbo.setMembers(team.getMembers().stream().map(CTeammitglied::new).toList());
        dbo.setCustomOpggTeamlinkRef(team.getCustomOpggTeamlinkRef());
        return dbo;
    }

    @Override
    public MapId getMapId() {
        return BasicMapId.id("guildId", this.getGuildId()).with("teamname", this.getTeamname());
    }
}
