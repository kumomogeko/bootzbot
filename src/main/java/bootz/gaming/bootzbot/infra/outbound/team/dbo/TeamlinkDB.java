package bootz.gaming.bootzbot.infra.outbound.team.dbo;

import bootz.gaming.bootzbot.domain.teams.teamlinks.Teamlink;

public class TeamlinkDB {
    private String name;
    private String link;

    public TeamlinkDB(String name, String link) {
        this.name = name;
        this.link = link;
    }

    public TeamlinkDB() {
    }

    public TeamlinkDB(Teamlink teamlink) {
        this.name = teamlink.getName();
        this.link = teamlink.getLink();
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getLink() {
        return link;
    }

    public void setLink(String link) {
        this.link = link;
    }

    public Teamlink toTeamlink() {
        return new Teamlink(this.name, this.link);
    }
}
