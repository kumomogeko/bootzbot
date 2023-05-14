package bootz.gaming.bootzbot.domain.teams;

import bootz.gaming.bootzbot.domain.sharedKernel.Executor;
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
    private Teamlink customOpgg;

    public Team(List<Teammitglied> members, Map<String, Teamlink> links, Teamlink customOpgg) {
        this.members = members;
        this.links = links;
        this.customOpgg = customOpgg;
    }

    public String getOpGG() {
        return Optional.ofNullable(customOpgg).map(Teamlink::getLink).orElse(this.generateOpGG());
    }

    private String generateOpGG() {
        var stringSeparatedMembers = members.stream().map(Teammitglied::getLeagueName).collect(Collectors.joining(","));
        return String.format("https://www.op.gg/multisearch/euw?summoners=%s", stringSeparatedMembers);
    }

    public void addTeamLink(AddTeamLinkCommand command) {
        if (notAllowedToExecuteTeamAction(command.runner())){
            throw new RuntimeException("Kein Teammitglied!");
        }
        if (command.isOpGG()) {
            this.customOpgg = command.link();
        }
        links.put(command.linkId(), command.link());
    }

    private boolean notAllowedToExecuteTeamAction(Executor executor) {
        return !executor.isAdmin() &&
                this.members.stream()
                        .map(Teammitglied::getDiscordAccount)
                        .noneMatch(snowflake -> executor.getDiscordAccount().equals(snowflake));
    }

    public Map<String, Teamlink> getLinks() {
        return links;
    }
}
