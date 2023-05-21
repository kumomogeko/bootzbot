package bootz.gaming.bootzbot.application.commands;

import bootz.gaming.bootzbot.domain.sharedKernel.RegistrableCommand;
import bootz.gaming.bootzbot.domain.teams.Team;
import bootz.gaming.bootzbot.domain.teams.TeamService;
import bootz.gaming.bootzbot.domain.teams.teammitglied.Teammitglied;
import discord4j.core.event.domain.interaction.ChatInputInteractionEvent;
import discord4j.core.spec.EmbedCreateFields;
import discord4j.core.spec.EmbedCreateSpec;
import discord4j.core.spec.InteractionApplicationCommandCallbackSpec;
import discord4j.discordjson.json.ApplicationCommandRequest;
import discord4j.rest.util.Color;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Mono;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Function;

@Component
public class DCListTeamsCommand implements RegistrableCommand {

    private final TeamService teamService;

    public DCListTeamsCommand(TeamService teamService) {
        this.teamService = teamService;
    }

    @Override
    public ApplicationCommandRequest getDiscordCommandRequest() {
        return ApplicationCommandRequest.builder()
                .name("listteams")
                .description("Listet die Teams auf")
                .build();
    }

    @Override
    public Function<ChatInputInteractionEvent, Mono<Void>> getCommandHandler() {
        return event -> teamService.getTeams().flatMap(teams -> {
            var replySpec = InteractionApplicationCommandCallbackSpec.builder()
                    .addEmbed(createTeamSpec(teams)).build();
            return event.reply(replySpec);
        });
    }

    private EmbedCreateSpec createTeamSpec(List<Team> teams) {
        var linkFieldList = new ArrayList<EmbedCreateFields.Field>();
        for (var team : teams) {
            linkFieldList.add(EmbedCreateFields.Field.of("Team", team.getTeamname(), false));
            linkFieldList.add(EmbedCreateFields.Field.of("op gg", team.getOpGG(), true));
            linkFieldList.add(EmbedCreateFields.Field.of("Captain(s)", membersToMentions(team.getCaptains()), true));
        }

        return EmbedCreateSpec.builder().color(Color.of(0x181d29))
                .title("Die Teams")
                .description("Inklusive op.gg")
                .addAllFields(linkFieldList)
                .footer("With 💌 from Bootzbot", "https://bootz-gaming.com/wp-content/uploads/2023/04/Element-3.png")
                .build();
    }

    private String membersToMentions(List<Teammitglied> captains) {
        StringBuilder s = new StringBuilder();
        for (var captain : captains) {
            s.append(String.format(" <@%s> ", captain.getDiscordAccount()));
        }
        return s.toString();
    }
}
