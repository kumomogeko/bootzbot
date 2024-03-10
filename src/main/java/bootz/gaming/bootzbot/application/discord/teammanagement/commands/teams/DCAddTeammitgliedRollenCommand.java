package bootz.gaming.bootzbot.application.discord.teammanagement.commands.teams;

import bootz.gaming.bootzbot.application.discord.teammanagement.commands.AbstractRegistrableIdentifiedCommand;
import bootz.gaming.bootzbot.application.discord.teammanagement.commands.id.ExecutorFactory;
import bootz.gaming.bootzbot.domain.sharedKernel.Executor;
import bootz.gaming.bootzbot.domain.teams.TeamId;
import bootz.gaming.bootzbot.domain.teams.TeamService;
import bootz.gaming.bootzbot.domain.teams.teammitglied.AddTeammitgliedRollenTeamCommand;
import bootz.gaming.bootzbot.domain.teams.teammitglied.Rolle;
import discord4j.core.event.domain.interaction.ChatInputInteractionEvent;
import discord4j.core.object.command.ApplicationCommandInteractionOptionValue;
import discord4j.core.object.command.ApplicationCommandOption;
import discord4j.discordjson.json.ApplicationCommandOptionChoiceData;
import discord4j.discordjson.json.ApplicationCommandOptionData;
import discord4j.discordjson.json.ApplicationCommandRequest;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Mono;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

@Component
public class DCAddTeammitgliedRollenCommand extends AbstractRegistrableIdentifiedCommand {

    private final String teamnameOption = "teamname";
    private final String rollenOption = "rolle";
    private final String spielerdiscordOption = "spielerdiscord";

    private final TeamService teamService;

    protected DCAddTeammitgliedRollenCommand(ExecutorFactory executorFactory, TeamService teamService) {
        super(executorFactory);
        this.teamService = teamService;
    }

    @Override
    public ApplicationCommandRequest getDiscordCommandRequest() {
        List<ApplicationCommandOptionChoiceData> rollen = Arrays.stream(Rolle.values()).map(rolle -> ApplicationCommandOptionChoiceData.builder().name(rolle.name()).value(rolle.name()).build()).collect(Collectors.toList());
        return ApplicationCommandRequest.builder()
                .name("addrolle")
                .description("Fügt einem Teammitglied eine Rolle hinzu")
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
                        .name(rollenOption)
                        .description("Rolle")
                        .type(ApplicationCommandOption.Type.STRING.getValue())
                        .required(true)
                        .choices(rollen)
                        .build())
                .build();
    }

    @Override
    public Mono<Void> innerCommandHandler(Executor runner, ChatInputInteractionEvent event) {
        var guildId = event.getInteraction().getGuildId().orElseThrow();

        var teamname = getOption(event, teamnameOption, ApplicationCommandInteractionOptionValue::asString);
        var discordaccount = getOption(event, spielerdiscordOption, ApplicationCommandInteractionOptionValue::asSnowflake);
        var rolle = getOption(event, rollenOption, ApplicationCommandInteractionOptionValue::asString);
        return this.teamService.addTeammitgliedRolle(new AddTeammitgliedRollenTeamCommand(runner, new TeamId(guildId.asLong(), teamname), discordaccount, Rolle.valueOf(rolle)))
                .then(event.createFollowup("Rolle zugefügt").then());
    }
}
