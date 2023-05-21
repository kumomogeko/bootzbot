package bootz.gaming.bootzbot.infra.outbound;

import bootz.gaming.bootzbot.domain.teams.Team;
import bootz.gaming.bootzbot.domain.teams.TeamId;

public class TeamDBO {
    public String id;
    public TeamIdDB teamId;
    public TeamDB team;

    public TeamDBO() {
    }

    public TeamDBO(String id, TeamId teamId, Team team) {
        this.id = id;
        this.teamId = new TeamIdDB(teamId);
        this.team = new TeamDB(team);
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public TeamIdDB getTeamId() {
        return teamId;
    }

    public void setTeamId(TeamIdDB teamId) {
        this.teamId = teamId;
    }

    public TeamDB getTeam() {
        return team;
    }

    public void setTeam(TeamDB team) {
        this.team = team;
    }
}
