package bootz.gaming.bootzbot.application.discord.teammanagement.commands.teams;

import bootz.gaming.bootzbot.application.discord.teammanagement.commands.AbstractRegistrableIdentifiedCommand;
import bootz.gaming.bootzbot.domain.sharedKernel.Executor;
import bootz.gaming.bootzbot.application.discord.teammanagement.commands.id.ExecutorFactory;
import bootz.gaming.bootzbot.domain.teams.TeamId;
import bootz.gaming.bootzbot.domain.teams.TeamReadTeamCommand;
import bootz.gaming.bootzbot.domain.teams.TeamService;
import bootz.gaming.bootzbot.domain.teams.teammitglied.Rolle;
import bootz.gaming.bootzbot.domain.teams.teammitglied.Teammitglied;
import discord4j.core.event.domain.interaction.ChatInputInteractionEvent;
import discord4j.core.object.command.ApplicationCommandInteractionOptionValue;
import discord4j.core.object.command.ApplicationCommandOption;
import discord4j.core.spec.EmbedCreateFields;
import discord4j.core.spec.EmbedCreateSpec;
import discord4j.core.spec.InteractionApplicationCommandCallbackSpec;
import discord4j.core.spec.InteractionFollowupCreateSpec;
import discord4j.discordjson.json.ApplicationCommandOptionData;
import discord4j.discordjson.json.ApplicationCommandRequest;
import discord4j.rest.util.Color;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Mono;

import java.util.ArrayList;
import java.util.List;

@Component
public class DCListTeamMembersCommand extends AbstractRegistrableIdentifiedCommand {

    private final TeamService teamService;
    private final String teamnameOption = "teamname";

    public DCListTeamMembersCommand(TeamService teamService, ExecutorFactory executorFactory) {
        super(executorFactory);
        this.teamService = teamService;
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


    private EmbedCreateSpec createMemberSpec(String teamname, List<Teammitglied> members) {
        var memberFieldList = new ArrayList<EmbedCreateFields.Field>();
        for (var member : members) {
            memberFieldList.add(EmbedCreateFields.Field.of("Spieler", String.format("<@%s>", member.getDiscordAccount()), false));
            memberFieldList.add(EmbedCreateFields.Field.of("League", String.format("%s", member.getLeagueName()), true));
            memberFieldList.add(EmbedCreateFields.Field.of("Rollen", String.join(", ", member.getRollen().stream().map(Rolle::toString).toList()), true));
        }

        return EmbedCreateSpec.builder().color(Color.of(0x181d29))
                .title(teamname)
                .description("Mitglieder des Teams")
                .addAllFields(memberFieldList)
                .footer("With love from Bootzbot", "https://bootz-gaming.com/wp-content/uploads/2023/04/Element-3.png")
                .build();
    }

    @Override
    public Mono<Void> innerCommandHandler(Executor runner, ChatInputInteractionEvent event) {
        var guildId = event.getInteraction().getGuildId().orElseThrow();
        var teamname = getOption(event, teamnameOption, ApplicationCommandInteractionOptionValue::asString);
        var command = new TeamReadTeamCommand(runner, new TeamId(guildId.asLong(), teamname));

        return teamService.listMembers(command).flatMap(members -> {
            var replySpec = InteractionFollowupCreateSpec.builder()
                    .addEmbed(createMemberSpec(teamname, members)).build();
            return event.createFollowup(replySpec).then();
        });
    }
}
