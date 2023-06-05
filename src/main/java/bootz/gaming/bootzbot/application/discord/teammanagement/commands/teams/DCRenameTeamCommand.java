package bootz.gaming.bootzbot.application.discord.teammanagement.commands.teams;

import bootz.gaming.bootzbot.application.discord.teammanagement.commands.AbstractRegistrableIdentifiedCommand;
import bootz.gaming.bootzbot.application.discord.teammanagement.commands.ExecutorFactory;
import bootz.gaming.bootzbot.domain.sharedKernel.Executor;
import bootz.gaming.bootzbot.domain.teams.RenameTeamCommand;
import bootz.gaming.bootzbot.domain.teams.TeamId;
import bootz.gaming.bootzbot.domain.teams.TeamService;
import discord4j.core.event.domain.interaction.ChatInputInteractionEvent;
import discord4j.core.object.command.ApplicationCommandInteractionOptionValue;
import discord4j.core.object.command.ApplicationCommandOption;
import discord4j.discordjson.json.ApplicationCommandOptionData;
import discord4j.discordjson.json.ApplicationCommandRequest;
import reactor.core.publisher.Mono;

public class DCRenameTeamCommand extends AbstractRegistrableIdentifiedCommand {

    private final TeamService teamService;

    public DCRenameTeamCommand(TeamService teamService, ExecutorFactory executorFactory) {
        super(executorFactory);
        this.teamService = teamService;
    }

    @Override
    public ApplicationCommandRequest getDiscordCommandRequest() {
        return ApplicationCommandRequest.builder()
                .name("renameteam")
                .description("Benennt ein Team um")
                .addOption(ApplicationCommandOptionData.builder()
                        .type(ApplicationCommandOption.Type.STRING.getValue())
                        .description("Bisheriger Name des Teams")
                        .name("currentname")
                        .required(true)
                        .build())
                .addOption(ApplicationCommandOptionData.builder()
                        .type(ApplicationCommandOption.Type.STRING.getValue())
                        .description("gewünschter Name des Teams")
                        .name("wantedname")
                        .required(true)
                        .build())
                .build();
    }

    @Override
    public Mono<Void> innerCommandHandler(Executor runner, ChatInputInteractionEvent event) {
        var guildId = event.getInteraction().getGuildId().orElseThrow();
        var currentTeamname = getOption(event, "currentname", ApplicationCommandInteractionOptionValue::asString);
        var wantedTeamname = getOption(event, "wantedname", ApplicationCommandInteractionOptionValue::asString);

        return this.teamService.renameTeam(new RenameTeamCommand(runner, new TeamId(guildId.asLong(),currentTeamname),wantedTeamname));
    }
}
