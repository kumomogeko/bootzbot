package bootz.gaming.bootzbot.domain.teams;

import bootz.gaming.bootzbot.domain.teams.teamlinks.Teamlink;
import bootz.gaming.bootzbot.util.AggregateRoot;

import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

@AggregateRoot
public class Team {
    private final List<Teammitglied> members;

    private final Map<String, Teamlink> links;
    private Optional<Teamlink> customOpgg;

    public Team(List<Teammitglied> members, Map<String, Teamlink> links, Optional<Teamlink> customOpgg) {
        this.members = members;
        this.links = links;
        this.customOpgg = customOpgg;
    }

    public String getOpGG() {
        return customOpgg.map(Teamlink::getLink).orElse(this.generateOpGG());
    }

    private String generateOpGG() {
        var stringSeparatedMembers = members.stream().map(Teammitglied::getLeagueName).collect(Collectors.joining(","));
        return String.format("https://www.op.gg/multisearch/euw?summoners=%s", stringSeparatedMembers);
    }

    public void addTeamLink(AddTeamLinkCommand command) {
        if (!this.members.contains(command.runner())) {
            throw new RuntimeException("Kein Teammitglied!");
        }
        if (command.isOpGG()) {
            this.customOpgg = Optional.of(command.link());
        }
        links.put(command.linkId(), command.link());
    }

    public Map<String, Teamlink> getLinks() {
        return links;
    }
}
