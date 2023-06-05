package bootz.gaming.bootzbot.application.discord.teammanagement.commands.teams;

import bootz.gaming.bootzbot.application.discord.teammanagement.commands.AbstractRegistrableIdentifiedCommand;
import bootz.gaming.bootzbot.domain.sharedKernel.Executor;
import bootz.gaming.bootzbot.application.discord.teammanagement.commands.id.ExecutorFactory;
import bootz.gaming.bootzbot.domain.teams.TeamId;
import bootz.gaming.bootzbot.domain.teams.TeamService;
import bootz.gaming.bootzbot.domain.teams.teamlinks.RemoveTeamLinkTeamCommand;
import discord4j.core.event.domain.interaction.ChatInputInteractionEvent;
import discord4j.core.object.command.ApplicationCommandInteractionOptionValue;
import discord4j.core.object.command.ApplicationCommandOption;
import discord4j.discordjson.json.ApplicationCommandOptionData;
import discord4j.discordjson.json.ApplicationCommandRequest;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Mono;

@Component
public class DCRemoveTeamlinkCommand extends AbstractRegistrableIdentifiedCommand {

    private final TeamService teamService;
    private final String teamnameOption = "teamname";
    private final String linkidOption = "linkid";

    public DCRemoveTeamlinkCommand(TeamService teamService, ExecutorFactory executorFactory) {
        super(executorFactory);
        this.teamService = teamService;
    }

    @Override
    public ApplicationCommandRequest getDiscordCommandRequest() {
        return ApplicationCommandRequest.builder()
                .name("removelink")
                .description("Entfernt einen Link eines Teams")
                .addOption(ApplicationCommandOptionData.builder()
                        .type(ApplicationCommandOption.Type.STRING.getValue())
                        .description("Name des Teams")
                        .name(teamnameOption)
                        .required(true)
                        .build())
                .addOption(ApplicationCommandOptionData.builder()
                        .type(ApplicationCommandOption.Type.STRING.getValue())
                        .description("ID des Links")
                        .name(linkidOption)
                        .required(true)
                        .build())
                .build();
    }


    @Override
    public Mono<Void> innerCommandHandler(Executor runner, ChatInputInteractionEvent event) {
        var guildId = event.getInteraction().getGuildId().orElseThrow();
        var teamname = getOption(event, teamnameOption, ApplicationCommandInteractionOptionValue::asString);
        var linkid = getOption(event, linkidOption, ApplicationCommandInteractionOptionValue::asString);

        var command = new RemoveTeamLinkTeamCommand(runner, new TeamId(guildId.asLong(), teamname), linkid);
        return teamService.removeTeamlink(command).then(event.reply("Link entfernt"));
    }
}
