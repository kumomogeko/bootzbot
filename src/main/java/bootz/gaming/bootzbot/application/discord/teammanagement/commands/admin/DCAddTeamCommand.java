package bootz.gaming.bootzbot.application.discord.teammanagement.commands.admin;

import bootz.gaming.bootzbot.application.discord.teammanagement.commands.AbstractRegistrableAdminCommand;
import bootz.gaming.bootzbot.domain.sharedKernel.Executor;
import bootz.gaming.bootzbot.application.discord.teammanagement.commands.id.ExecutorFactory;
import bootz.gaming.bootzbot.domain.teams.AddTeamCommand;
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
public class DCAddTeamCommand extends AbstractRegistrableAdminCommand {

    private final TeamService teamService;

    public DCAddTeamCommand(TeamService teamService, ExecutorFactory executorFactory) {
        super(executorFactory);
        this.teamService = teamService;
    }

    @Override
    public ApplicationCommandRequest getDiscordCommandRequest() {
        return ApplicationCommandRequest.builder()
                .name("addteam")
                .description("Fügt ein Team hinzu")
                .addOption(ApplicationCommandOptionData.builder()
                        .type(ApplicationCommandOption.Type.STRING.getValue())
                        .description("Name des Teams")
                        .name("name")
                        .required(true)
                        .build())
                .build();
    }

    @Override
    public Mono<Void> innerCommandHandler(Executor executor, ChatInputInteractionEvent event) {
        var guildId = event.getInteraction().getGuildId().orElseThrow();
        var teamName = this.getOption(event, "name", ApplicationCommandInteractionOptionValue::asString);
        var command = new AddTeamCommand(executor, new TeamId(guildId.asLong(), teamName));
        return teamService.addTeam(command).then(event.reply(String.format("Team hinzugefügt: %s", teamName)));
    }
}
