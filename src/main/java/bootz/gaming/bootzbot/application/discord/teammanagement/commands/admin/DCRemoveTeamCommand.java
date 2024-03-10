package bootz.gaming.bootzbot.application.discord.teammanagement.commands.admin;

import bootz.gaming.bootzbot.application.discord.teammanagement.commands.AbstractRegistrableAdminCommand;
import bootz.gaming.bootzbot.domain.sharedKernel.Executor;
import bootz.gaming.bootzbot.application.discord.teammanagement.commands.id.ExecutorFactory;
import bootz.gaming.bootzbot.domain.teams.RemoveTeamCommand;
import bootz.gaming.bootzbot.domain.teams.TeamId;
import bootz.gaming.bootzbot.domain.teams.TeamService;
import discord4j.core.event.domain.interaction.ChatInputInteractionEvent;
import discord4j.core.object.command.ApplicationCommandInteractionOptionValue;
import discord4j.core.object.command.ApplicationCommandOption;
import discord4j.discordjson.json.ApplicationCommandOptionData;
import discord4j.discordjson.json.ApplicationCommandRequest;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Mono;

@Component
public class DCRemoveTeamCommand extends AbstractRegistrableAdminCommand {

    private final TeamService teamService;

    public DCRemoveTeamCommand(TeamService teamService, ExecutorFactory executorFactory) {
        super(executorFactory);
        this.teamService = teamService;
    }

    @Override
    public ApplicationCommandRequest getDiscordCommandRequest() {
        return ApplicationCommandRequest.builder()
                .name("removeteam")
                .description("Entfernt ein Team")
                .addOption(ApplicationCommandOptionData.builder()
                        .type(ApplicationCommandOption.Type.STRING.getValue())
                        .description("Name des Teams")
                        .name("name")
                        .required(true)
                        .build())
                .build();

    }

    @Override
    public Mono<Void> innerCommandHandler(Executor runner, ChatInputInteractionEvent event) {
        var guildId = event.getInteraction().getGuildId().orElseThrow();
        var teamName = this.getOption(event, "name", ApplicationCommandInteractionOptionValue::asString);
        var command = new RemoveTeamCommand(runner, new TeamId(guildId.asLong(), teamName));
        return teamService.removeTeam(command).then(event.createFollowup(String.format("Team entfernt: %s", teamName)).then());
    }
}
