package bootz.gaming.bootzbot.application.discord.teammanagement.commands.teams;

import bootz.gaming.bootzbot.application.discord.teammanagement.commands.AbstractRegistrableIdentifiedCommand;
import bootz.gaming.bootzbot.domain.sharedKernel.Executor;
import bootz.gaming.bootzbot.application.discord.teammanagement.commands.id.ExecutorFactory;
import bootz.gaming.bootzbot.domain.teams.TeamId;
import bootz.gaming.bootzbot.domain.teams.TeamService;
import bootz.gaming.bootzbot.domain.teams.teammitglied.RemoveTeammitgliedTeamCommand;
import bootz.gaming.bootzbot.domain.teams.teammitglied.Teammitglied;
import discord4j.core.event.domain.interaction.ChatInputInteractionEvent;
import discord4j.core.object.command.ApplicationCommandInteractionOptionValue;
import discord4j.core.object.command.ApplicationCommandOption;
import discord4j.discordjson.json.ApplicationCommandOptionData;
import discord4j.discordjson.json.ApplicationCommandRequest;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Mono;

import java.util.Set;

@Component
public class DCRemoveTeammitgliedCommand extends AbstractRegistrableIdentifiedCommand {

    private final TeamService teamService;
    private final String teamnameOption = "teamname";
    private final String spielerdiscordOption = "spielerdiscord";

    public DCRemoveTeammitgliedCommand(TeamService teamService, ExecutorFactory executorFactory) {
        super(executorFactory);
        this.teamService = teamService;
    }

    @Override
    public ApplicationCommandRequest getDiscordCommandRequest() {
        return ApplicationCommandRequest.builder()
                .name("removemember")
                .description("Entfernt ein Teammitglied aus einem Team")
                .addOption(ApplicationCommandOptionData.builder()
                        .type(ApplicationCommandOption.Type.STRING.getValue())
                        .description("Name des Teams")
                        .name(teamnameOption)
                        .required(true)
                        .build())
                .addOption(ApplicationCommandOptionData.builder()
                        .name(spielerdiscordOption)
                        .description("Discord Account des Spielers")
                        .type(ApplicationCommandOption.Type.USER.getValue())
                        .required(true)
                        .build())
                .build();
    }

    @Override
    public Mono<Void> innerCommandHandler(Executor runner, ChatInputInteractionEvent event) {
        var guildId = event.getInteraction().getGuildId().orElseThrow();
        var teamName = getOption(event, teamnameOption, ApplicationCommandInteractionOptionValue::asString);
        var discord = getOption(event, spielerdiscordOption, ApplicationCommandInteractionOptionValue::asSnowflake);
        //FIXME Teammitglied creation is unnecessary here
        var command = new RemoveTeammitgliedTeamCommand(runner, new TeamId(guildId.asLong(), teamName), new Teammitglied(discord.asLong(), Set.of(), ""));
        return teamService.removeTeammitglied(command).then(event.reply(String.format("Teammitglied entfernt: <@%s>", discord.asString())));
    }
}
