package bootz.gaming.bootzbot.infra.outbound.redis.dbo;

import bootz.gaming.bootzbot.domain.teams.Team;
import bootz.gaming.bootzbot.domain.teams.TeammitgliedDB;
import reactor.util.annotation.Nullable;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class TeamDB {
    private Long guildId;
    private String teamname;

    private List<TeammitgliedDB> members;
    private Map<String, TeamlinkDB> links;

    @Nullable
    private String customOpggTeamlinkRef;

    public TeamDB(Long guildId, String teamname, List<TeammitgliedDB> members, Map<String, TeamlinkDB> links, @Nullable String customOpggTeamlinkRef) {
        this.guildId = guildId;
        this.teamname = teamname;
        this.members = members;
        this.links = links;
        this.customOpggTeamlinkRef = customOpggTeamlinkRef;
    }

    public TeamDB() {
    }

    public TeamDB(Team team) {
        this.guildId = team.getGuildId();
        this.links = team.getLinks().entrySet().stream().collect(Collectors.toMap(Map.Entry::getKey, e -> new TeamlinkDB(e.getValue())));
        this.teamname = team.getTeamname();
        this.members = team.getMembers().stream().map(TeammitgliedDB::new).collect(Collectors.toList());
        this.customOpggTeamlinkRef = team.getCustomOpggTeamlinkRef();
    }

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

    public List<TeammitgliedDB> getMembers() {
        return members;
    }

    public void setMembers(List<TeammitgliedDB> members) {
        this.members = members;
    }

    public Map<String, TeamlinkDB> getLinks() {
        return links;
    }

    public void setLinks(Map<String, TeamlinkDB> links) {
        this.links = links;
    }

    @Nullable
    public String getCustomOpggTeamlinkRef() {
        return customOpggTeamlinkRef;
    }

    public void setCustomOpggTeamlinkRef(@Nullable String customOpggTeamlinkRef) {
        this.customOpggTeamlinkRef = customOpggTeamlinkRef;
    }

    public Team toTeam() {
        return new Team(this.guildId, this.teamname, this.members.stream().map(TeammitgliedDB::toTeammitglied).collect(Collectors.toList()),
                this.links.entrySet().stream().collect(Collectors.toMap(Map.Entry::getKey, e -> e.getValue().toTeamlink())), this.customOpggTeamlinkRef);
    }
}
