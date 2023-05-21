package bootz.gaming.bootzbot.application.commands;

import bootz.gaming.bootzbot.domain.sharedKernel.ExecutorFactory;
import bootz.gaming.bootzbot.domain.teams.TeamId;
import bootz.gaming.bootzbot.domain.teams.TeamService;
import bootz.gaming.bootzbot.domain.teams.teammitglied.RemoveTeammitgliedCommand;
import bootz.gaming.bootzbot.domain.teams.teammitglied.Teammitglied;
import discord4j.core.event.domain.interaction.ChatInputInteractionEvent;
import discord4j.core.object.command.ApplicationCommandInteractionOptionValue;
import discord4j.core.object.command.ApplicationCommandOption;
import discord4j.discordjson.json.ApplicationCommandOptionData;
import discord4j.discordjson.json.ApplicationCommandRequest;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Mono;

import java.util.Set;
import java.util.function.Function;

@Component
public class DCRemoveTeammitgliedCommand extends AbstractRegistrableCommand {

    private final TeamService teamService;
    private final ExecutorFactory executorFactory;
    private final String teamnameOption = "teamname";
    private final String spielerdiscordOption = "spielerdiscord";

    public DCRemoveTeammitgliedCommand(TeamService teamService, ExecutorFactory executorFactory) {
        this.teamService = teamService;
        this.executorFactory = executorFactory;
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
    public Function<ChatInputInteractionEvent, Mono<Void>> getCommandHandler() {
        return event -> {
            var guildId = event.getInteraction().getGuildId().orElseThrow();
            var member = event.getInteraction().getMember().orElseThrow();
            return this.executorFactory.executorFromMember(member).flatMap(executor -> {
                var teamName = getOption(event, teamnameOption, ApplicationCommandInteractionOptionValue::asString);
                var discord = getOption(event, spielerdiscordOption, ApplicationCommandInteractionOptionValue::asSnowflake);
                //FIXME Teammitglied creation is unnecessary here
                var command = new RemoveTeammitgliedCommand(executor, new TeamId(guildId.asLong(), teamName), new Teammitglied(discord.asLong(), Set.of(),""));
                return teamService.removeTeammitglied(command).then(event.reply(String.format("Teammitglied entfernt: <@%s>", discord.asString())));
            });
        };
    }
}
