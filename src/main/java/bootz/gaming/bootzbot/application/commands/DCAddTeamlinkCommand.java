package bootz.gaming.bootzbot.application.commands;

import bootz.gaming.bootzbot.domain.sharedKernel.ExecutorFactory;
import bootz.gaming.bootzbot.domain.teams.TeamId;
import bootz.gaming.bootzbot.domain.teams.TeamService;
import bootz.gaming.bootzbot.domain.teams.teamlinks.AddTeamLinkCommand;
import bootz.gaming.bootzbot.domain.teams.teamlinks.Teamlink;
import discord4j.core.event.domain.interaction.ChatInputInteractionEvent;
import discord4j.core.object.command.ApplicationCommandInteractionOptionValue;
import discord4j.core.object.command.ApplicationCommandOption;
import discord4j.discordjson.json.ApplicationCommandOptionData;
import discord4j.discordjson.json.ApplicationCommandRequest;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Mono;

import java.util.function.Function;

@Component
public class DCAddTeamlinkCommand extends AbstractRegistrableCommand {

    private final TeamService teamService;
    private final ExecutorFactory executorFactory;
    private final String teamnameOption = "teamname";
    private final String linkidOption = "linkid";
    private final String linkdescOption = "linkdesc";
    private final String linkOption = "link";
    private final String istopggOption = "istopgg";

    public DCAddTeamlinkCommand(TeamService teamService, ExecutorFactory executorFactory) {
        this.teamService = teamService;
        this.executorFactory = executorFactory;
    }

    @Override
    public ApplicationCommandRequest getDiscordCommandRequest() {
        return ApplicationCommandRequest.builder()
                .name("addlink")
                .description("Fügt einen Link einem Team hinzu")
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
                .addOption(ApplicationCommandOptionData.builder()
                        .type(ApplicationCommandOption.Type.STRING.getValue())
                        .description("Beschreibung des Links")
                        .name(linkdescOption)
                        .required(true)
                        .build())
                .addOption(ApplicationCommandOptionData.builder()
                        .type(ApplicationCommandOption.Type.STRING.getValue())
                        .description("Link")
                        .name(linkOption)
                        .required(true)
                        .build())
                .addOption(ApplicationCommandOptionData.builder()
                        .type(ApplicationCommandOption.Type.BOOLEAN.getValue())
                        .description("Soll der Link das Standard OP GG ersetzen?")
                        .name(istopggOption)
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
                var teamname = getOption(event, teamnameOption, ApplicationCommandInteractionOptionValue::asString);
                var linkid = getOption(event, linkidOption, ApplicationCommandInteractionOptionValue::asString);
                var linkdesc = getOption(event, this.linkdescOption, ApplicationCommandInteractionOptionValue::asString);
                var link = getOption(event, this.linkOption, ApplicationCommandInteractionOptionValue::asString);
                var isOpGG = getNonRequiredOption(event, this.istopggOption, ApplicationCommandInteractionOptionValue::asBoolean).orElse(false);

                var command = new AddTeamLinkCommand(executor, new TeamId(guildId.asLong(), teamname), linkid, new Teamlink(linkdesc, link), isOpGG);
                return teamService.addTeamlink(command).then(event.reply("Link zugefügt"));
            });
        };
    }
}
