package bootz.gaming.bootzbot.application.commands.admin;

import bootz.gaming.bootzbot.application.commands.AbstractRegistrableCommand;
import bootz.gaming.bootzbot.domain.sharedKernel.ExecutorFactory;
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

import java.util.function.Function;

@Component
public class DCRemoveTeamCommand extends AbstractRegistrableCommand {

    private final TeamService teamService;
    private final ExecutorFactory executorFactory;

    public DCRemoveTeamCommand(TeamService teamService, ExecutorFactory executorFactory) {
        this.teamService = teamService;
        this.executorFactory = executorFactory;
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
    public Function<ChatInputInteractionEvent, Mono<Void>> getCommandHandler() {
        return event -> {
            var guildId = event.getInteraction().getGuildId().orElseThrow();
            var member = event.getInteraction().getMember().orElseThrow();
            return this.executorFactory.executorFromMember(member).flatMap(executor -> {
                var teamName = this.getOption(event, "name", ApplicationCommandInteractionOptionValue::asString);
                var command = new RemoveTeamCommand(executor, new TeamId(guildId.asLong(), teamName));
                return teamService.removeTeam(command).then(event.reply(String.format("Team entfernt: %s", teamName)));
            });
        };

    }
}
