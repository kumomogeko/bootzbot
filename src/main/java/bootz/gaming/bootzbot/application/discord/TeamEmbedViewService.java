package bootz.gaming.bootzbot.application.discord;

import bootz.gaming.bootzbot.domain.teams.Team;
import bootz.gaming.bootzbot.domain.teams.teammitglied.Teammitglied;
import discord4j.core.spec.EmbedCreateFields;
import discord4j.core.spec.EmbedCreateSpec;
import discord4j.rest.util.Color;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.format.FormatStyle;
import java.util.ArrayList;
import java.util.List;

@Service
public class TeamEmbedViewService {

    public EmbedCreateSpec createTeamSpec(List<Team> teams) {
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
                .footer("With 💌 from Bootzbot @" + LocalDateTime.now().format(DateTimeFormatter.ofLocalizedDateTime(FormatStyle.MEDIUM)), "https://bootz-gaming.com/wp-content/uploads/2023/04/Element-3.png")
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
