package bootz.gaming.bootzbot.domain.teams.teamlinks;

import bootz.gaming.bootzbot.util.ValueObject;

@ValueObject
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
        return link;
    }
}
