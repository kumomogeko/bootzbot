package bootz.gaming.bootzbot.application.commands;

import bootz.gaming.bootzbot.domain.sharedKernel.ExecutorFactory;
import bootz.gaming.bootzbot.domain.teams.TeamId;
import bootz.gaming.bootzbot.domain.teams.TeamReadCommand;
import bootz.gaming.bootzbot.domain.teams.TeamService;
import bootz.gaming.bootzbot.domain.teams.teammitglied.Teammitglied;
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
import java.util.List;
import java.util.function.Function;

@Component
public class DCListTeamMembersCommand extends AbstractRegistrableCommand {

    private final TeamService teamService;
    private final ExecutorFactory executorFactory;
    private final String teamnameOption = "teamname";

    public DCListTeamMembersCommand(TeamService teamService, ExecutorFactory executorFactory) {
        this.teamService = teamService;
        this.executorFactory = executorFactory;
    }

    @Override
    public ApplicationCommandRequest getDiscordCommandRequest() {
        return ApplicationCommandRequest.builder()
                .name("listmembers")
                .description("Listet die Mitglieder eines Teams auf")
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

                return teamService.listMembers(command).flatMap(members -> {
                    var replySpec = InteractionApplicationCommandCallbackSpec.builder()
                            .addEmbed(createMemberSpec(teamname, members)).build();
                    return event.reply(replySpec);
                });
            });
        };
    }

    private EmbedCreateSpec createMemberSpec(String teamname, List<Teammitglied> members) {
        var memberFieldList = new ArrayList<EmbedCreateFields.Field>();
        for (var member : members) {
            memberFieldList.add(EmbedCreateFields.Field.of("Spieler", String.format("<@%s>", member.getDiscordAccount()), false));
            memberFieldList.add(EmbedCreateFields.Field.of("League", String.format("%s", member.getLeagueName()), true));
            memberFieldList.add(EmbedCreateFields.Field.of("Captain", (member.isCaptain() ? "Ja" : "Nein"), true));
        }

        return EmbedCreateSpec.builder().color(Color.of(0x181d29))
                .title(teamname)
                .description("Mitglieder des Teams")
                .addAllFields(memberFieldList)
                .footer("With love from Bootzbot", "https://bootz-gaming.com/wp-content/uploads/2023/04/Element-3.png")
                .build();
    }
}
