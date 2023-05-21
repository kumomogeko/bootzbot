package bootz.gaming.bootzbot.domain.teams;

import java.util.Objects;

public class TeamId {
    private final Long guild;
    private final String name;

    public static TeamId fromTeam(Team team) {
        return new TeamId(team);
    }

    public TeamId(Long guild, String name) {
        this.guild = guild;
        this.name = name;
    }

    public TeamId(Team team) {
        this.guild = team.getGuildId();
        this.name = team.getTeamname();
    }

    public Long getGuild() {
        return guild;
    }

    public String getName() {
        return name;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        TeamId teamId = (TeamId) o;
        return guild.equals(teamId.guild) && name.equals(teamId.name);
    }

    @Override
    public String toString() {
        return guild + name;
    }

    @Override
    public int hashCode() {
        return Objects.hash(guild, name);
    }
}
