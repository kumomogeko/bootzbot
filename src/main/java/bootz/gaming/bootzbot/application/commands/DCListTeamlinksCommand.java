package bootz.gaming.bootzbot.application.commands;

import bootz.gaming.bootzbot.domain.sharedKernel.ExecutorFactory;
import bootz.gaming.bootzbot.domain.teams.TeamId;
import bootz.gaming.bootzbot.domain.teams.TeamReadCommand;
import bootz.gaming.bootzbot.domain.teams.TeamService;
import bootz.gaming.bootzbot.domain.teams.teamlinks.Teamlink;
import discord4j.core.event.domain.interaction.ChatInputInteractionEvent;
import discord4j.core.object.command.ApplicationCommandInteractionOptionValue;
import discord4j.core.object.command.ApplicationCommandOption;
import discord4j.core.spec.EmbedCreateFields;
import discord4j.core.spec.EmbedCreateSpec;
import discord4j.core.spec.InteractionApplicationCommandCallbackSpec;
import discord4j.discordjson.json.ApplicationCommandOptionData;
import discord4j.discordjson.json.ApplicationCommandRequest;
import discord4j.rest.util.Color;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Mono;

import java.util.ArrayList;
import java.util.Map;
import java.util.function.Function;

@Component
public class DCListTeamlinksCommand extends AbstractRegistrableCommand {
    private final TeamService teamService;
    private final ExecutorFactory executorFactory;
    private final String teamnameOption = "teamname";

    public DCListTeamlinksCommand(TeamService teamService, ExecutorFactory executorFactory) {
        this.teamService = teamService;
        this.executorFactory = executorFactory;
    }

    @Override
    public ApplicationCommandRequest getDiscordCommandRequest() {
        return ApplicationCommandRequest.builder()
                .name("linklist")
                .description("Listet die Links eines Teams auf")
                .addOption(ApplicationCommandOptionData.builder()
                        .type(ApplicationCommandOption.Type.STRING.getValue())
                        .description("Name des Teams")
                        .name(teamnameOption)
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
                var teamname = getOption(event, teamnameOption, ApplicationCommandInteractionOptionValue::asString);
                var command = new TeamReadCommand(executor, new TeamId(guildId.asLong(), teamname));

                return teamService.getLinks(command).flatMap(links -> {
                    var replySpec = InteractionApplicationCommandCallbackSpec.builder()
                            .addEmbed(createLinkSpec(teamname, links)).build();
                    return event.reply(replySpec);
                });
            });
        };
    }

    private EmbedCreateSpec createLinkSpec(String teamname, Map<String, Teamlink> links) {
        var linkFieldList = new ArrayList<EmbedCreateFields.Field>();
        for (var link : links.entrySet()) {
            linkFieldList.add(EmbedCreateFields.Field.of("Beschreibung", link.getValue().getName(), false));
            linkFieldList.add(EmbedCreateFields.Field.of("ID", link.getKey(), true));
            linkFieldList.add(EmbedCreateFields.Field.of("Link", link.getValue().getLink(), true));
        }

        return EmbedCreateSpec.builder().color(Color.of(0x181d29))
                .title(teamname)
                .description("Teamlinks")
                .addAllFields(linkFieldList)
                .footer("With 💌 from Bootzbot", "https://bootz-gaming.com/wp-content/uploads/2023/04/Element-3.png")
                .build();
    }
}
