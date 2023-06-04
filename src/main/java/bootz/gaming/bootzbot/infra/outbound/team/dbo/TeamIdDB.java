package bootz.gaming.bootzbot.infra.outbound.team.dbo;

import bootz.gaming.bootzbot.domain.teams.TeamId;

public class TeamIdDB {
    private Long guild;
    private String name;

    public TeamIdDB(Long guild, String name) {
        this.guild = guild;
        this.name = name;
    }

    public TeamIdDB() {
    }

    public TeamIdDB(TeamId teamId) {
        this.guild = teamId.getGuild();
        this.name = teamId.getName();
    }

    public Long getGuild() {
        return guild;
    }

    public void setGuild(Long guild) {
        this.guild = guild;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public TeamId toTeamId() {
        return new TeamId(this.guild, this.name);
    }
}
