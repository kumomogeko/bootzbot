package bootz.gaming.bootzbot.domain.teams;

import bootz.gaming.bootzbot.domain.sharedKernel.Executor;
import bootz.gaming.bootzbot.domain.teams.teamlinks.AddTeamLinkCommand;
import bootz.gaming.bootzbot.domain.teams.teamlinks.RemoveTeamLinkCommand;
import bootz.gaming.bootzbot.domain.teams.teamlinks.Teamlink;
import bootz.gaming.bootzbot.domain.teams.teammitglied.AddTeammitgliedCommand;
import bootz.gaming.bootzbot.domain.teams.teammitglied.RemoveTeammitgliedCommand;
import bootz.gaming.bootzbot.domain.teams.teammitglied.Teammitglied;
import bootz.gaming.bootzbot.util.AggregateRoot;
import reactor.util.annotation.Nullable;

import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

@AggregateRoot
public class Team {

    private final Long guildId;
    private String teamname;

    private final List<Teammitglied> members;
    private final Map<String, Teamlink> links;


    @Nullable
    private String customOpggTeamlinkRef;

    public Team(Long guildId, String teamname, List<Teammitglied> members, Map<String, Teamlink> links, String customOpggTeamlinkRef) {
        this.guildId = guildId;
        this.teamname = teamname;
        this.members = members;
        this.links = links;
        this.customOpggTeamlinkRef = customOpggTeamlinkRef;
    }

    public String getOpGG() {
        return Optional.ofNullable(links.get(this.customOpggTeamlinkRef)).map(Teamlink::getLink).orElse(this.generateOpGG());
    }

    private String generateOpGG() {
        var stringSeparatedMembers = members.stream()
                .map(teammitglied -> URLEncoder.encode(teammitglied.getLeagueName(), StandardCharsets.UTF_8))
                .collect(Collectors.joining(","));
        return String.format("https://www.op.gg/multisearch/euw?summoners=%s",stringSeparatedMembers);
    }

    public void addOrUpdateTeamLink(AddTeamLinkCommand command) {
        if (notAllowedToExecuteTeamAction(command.runner())){
            throw new RuntimeException("Kein Teammitglied!");
        }
        if (command.isOpGG()) {
            this.customOpggTeamlinkRef = command.linkId();
        }
        links.put(command.linkId(), command.link());
    }

    public void removeTeamLink(RemoveTeamLinkCommand command){
        if (notAllowedToExecuteTeamAction(command.runner())){
            throw new RuntimeException("Kein Teammitglied!");
        }
        var link = links.get(command.linkId());
        if(null == link){
            throw new RuntimeException("Link nicht gefunden!");
        }
        if(command.linkId().equals(this.customOpggTeamlinkRef)){
            this.customOpggTeamlinkRef = null;
        }
        this.links.remove(command.linkId());
    }

    public void addTeammitglied(AddTeammitgliedCommand command){
        if(notAllowedToExecuteTeamAction(command.runner())){
            throw new RuntimeException("Keine Berechtigung!");
        }
        if(isMember(command.teammitglied())){
            throw  new RuntimeException("Ist schon Teammitglied!");
        }
        this.members.add(command.teammitglied());
    }

    public void removeTeammitglied(RemoveTeammitgliedCommand command){
        if(notAllowedToExecuteTeamAction(command.runner())){
            throw new RuntimeException("Keine Berechtigung!");
        }
        if(!isMember(command.teammitglied())){
            throw new RuntimeException("Ist kein Teammitglied!");
        }
        this.members.removeIf(teammitglied -> command.teammitglied().isEqual(teammitglied));
    }

    private boolean isMember(Teammitglied teammitglied) {
        return this.members.stream()
                .anyMatch(member -> member.isEqual(teammitglied));
    }

    private Optional<Teammitglied> getTeammitglied(Teammitglied teammitglied){
        return this.members.stream().filter(member -> member.isEqual(teammitglied)).findFirst();
    }

    private boolean isCaptain(Teammitglied teammitglied){
        return this.getTeammitglied(teammitglied).map(Teammitglied::isCaptain).orElse(false);
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

    public Map<String, Teamlink> getLinklist() {
        return links;
    }

    public List<Teammitglied> getMembers() {
        return members;
    }

    public Long getGuildId() {
        return guildId;
    }

    public String getTeamname() {
        return teamname;
    }

    @Nullable
    public String getCustomOpggTeamlinkRef() {
        return customOpggTeamlinkRef;
    }

}
