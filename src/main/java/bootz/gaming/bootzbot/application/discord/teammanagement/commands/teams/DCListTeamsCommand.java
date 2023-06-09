package bootz.gaming.bootzbot.application.discord.teammanagement.commands.teams;

import bootz.gaming.bootzbot.application.discord.teammanagement.TeamEmbedViewService;
import bootz.gaming.bootzbot.application.discord.teammanagement.commands.RegistrableCommand;
import bootz.gaming.bootzbot.domain.teams.TeamService;
import discord4j.core.event.domain.interaction.ChatInputInteractionEvent;
import discord4j.discordjson.json.ApplicationCommandRequest;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Mono;

import java.util.function.Function;

@Component
public class DCListTeamsCommand implements RegistrableCommand {

    private final TeamService teamService;
    private final TeamEmbedViewService teamViewService;


    public DCListTeamsCommand(TeamService teamService, TeamEmbedViewService teamViewService) {
        this.teamService = teamService;
        this.teamViewService = teamViewService;
    }

    @Override
    public ApplicationCommandRequest getDiscordCommandRequest() {
        return ApplicationCommandRequest.builder()
                .name("listteams")
                .description("Listet die Teams auf")
                .build();
    }

    @Override
    public Function<ChatInputInteractionEvent, Mono<Void>> getCommandHandler() {
        return event -> teamService.getTeams().flatMap(teams -> this.teamViewService.postEventTeamSpec(event, this.teamViewService.createTeamSpec(teams)));
    }


}
