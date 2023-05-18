package bootz.gaming.bootzbot.application.commands;

import bootz.gaming.bootzbot.domain.sharedKernel.ExecutorFactory;
import bootz.gaming.bootzbot.domain.sharedKernel.RegistrableCommand;
import bootz.gaming.bootzbot.domain.teams.TeamId;
import bootz.gaming.bootzbot.domain.teams.TeamService;
import bootz.gaming.bootzbot.domain.teams.teammitglied.AddTeammitgliedCommand;
import bootz.gaming.bootzbot.domain.teams.teammitglied.Rolle;
import bootz.gaming.bootzbot.domain.teams.teammitglied.Teammitglied;
import discord4j.core.event.domain.interaction.ChatInputInteractionEvent;
import discord4j.core.object.command.ApplicationCommandInteractionOption;
import discord4j.core.object.command.ApplicationCommandInteractionOptionValue;
import discord4j.core.object.command.ApplicationCommandOption;
import discord4j.discordjson.json.ApplicationCommandOptionData;
import discord4j.discordjson.json.ApplicationCommandRequest;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Mono;

import java.util.Set;
import java.util.function.Function;

@Component
public class DiscordAddTeammitgliedCommand implements RegistrableCommand {

    private final TeamService teamService;
    private final ExecutorFactory executorFactory;
    private final String teamnameOption = "teamname";
    private final String leaguenameOption = "leaguename";
    private final String spielerdiscordOption = "spielerdiscord";
    private final String istCapOption = "istcap";

    public DiscordAddTeammitgliedCommand(TeamService teamService, ExecutorFactory executorFactory) {
        this.teamService = teamService;
        this.executorFactory = executorFactory;
    }

    @Override
    public ApplicationCommandRequest getDiscordCommandRequest() {
        return ApplicationCommandRequest.builder()
                .name("memberadd")
                .description("Fügt ein Teammitglied einem Team hinzu")
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
                .addOption(ApplicationCommandOptionData.builder()
                        .name(leaguenameOption)
                        .description("League Account des Spielers")
                        .type(ApplicationCommandOption.Type.STRING.getValue())
                        .required(true)
                        .build())
                .addOption(ApplicationCommandOptionData.builder()
                        .name(istCapOption)
                        .description("Ist Captain?")
                        .type(ApplicationCommandOption.Type.BOOLEAN.getValue())
                        .required(false)
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
                var leagueName = getOption(event, leaguenameOption, ApplicationCommandInteractionOptionValue::asString);
                var discord = getOption(event, spielerdiscordOption, ApplicationCommandInteractionOptionValue::asSnowflake);
                var roles = getOption(event, istCapOption, ApplicationCommandInteractionOptionValue::asBoolean) ? Set.of(Rolle.CAPTAIN, Rolle.MITGLIED): Set.of(Rolle.MITGLIED);
                var command = new AddTeammitgliedCommand(executor, new TeamId(guildId.asLong(), teamName),new Teammitglied(discord,roles,leagueName));
                return teamService.addTeammitglied(command);
            }).then(event.reply("Mitglied hinzugefügt"));
        };
    }



    private <X> X getOption(ChatInputInteractionEvent event, String opName, Function<ApplicationCommandInteractionOptionValue,X> transformer) {
        return event.getOption(opName)
                .flatMap(ApplicationCommandInteractionOption::getValue)
                .map(transformer)
                .orElseThrow();
    }
}
