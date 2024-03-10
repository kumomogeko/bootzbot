package bootz.gaming.bootzbot.application.discord.teammanagement;

import bootz.gaming.bootzbot.domain.teams.Team;
import bootz.gaming.bootzbot.domain.teams.teammitglied.Teammitglied;
import discord4j.core.event.domain.interaction.ChatInputInteractionEvent;
import discord4j.core.spec.EmbedCreateFields;
import discord4j.core.spec.EmbedCreateSpec;
import discord4j.core.spec.InteractionApplicationCommandCallbackSpec;
import discord4j.core.spec.InteractionFollowupCreateSpec;
import discord4j.rest.util.Color;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.format.FormatStyle;
import java.util.ArrayList;
import java.util.List;

@Service
public class TeamEmbedViewService {

    public List<EmbedCreateSpec> createTeamSpec(List<Team> teams) {
        var embedList = new ArrayList<EmbedCreateSpec>();
        var linkFieldList = new ArrayList<EmbedCreateFields.Field>();
        for (var team : teams) {
            linkFieldList.add(EmbedCreateFields.Field.of("Team", team.getTeamname(), false));
            linkFieldList.add(EmbedCreateFields.Field.of("op gg", team.getOpGG(), true));
            linkFieldList.add(EmbedCreateFields.Field.of("Captain(s)", membersToMentions(team.getCaptains()), true));
        }

        var listOfLists = new ArrayList<List<EmbedCreateFields.Field>>();
        //24 because an embded can take up to 25 fields and a team compartment is 3 fields, max fit is 24 field for a clean view
        for(int i =0; i<linkFieldList.size();i+=24){
            listOfLists.add(linkFieldList.subList(i,Math.min(i+24, linkFieldList.size())));
        }

        for(var sublist: listOfLists) {
            embedList.add(EmbedCreateSpec.builder().color(Color.of(0x181d29))
                    .title("Die Teams")
                    .description("Inklusive op.gg")
                    .addAllFields(sublist)
                    .footer("With 💌 from Bootzbot @" + LocalDateTime.now().format(DateTimeFormatter.ofLocalizedDateTime(FormatStyle.MEDIUM)), "https://bootz-gaming.com/wp-content/uploads/2023/04/Element-3.png")
                    .build());
        }
        return embedList;
    }

    public Mono<Void> postEventTeamSpec(ChatInputInteractionEvent event, List<EmbedCreateSpec> specs){
        if(specs.isEmpty()){
            return Mono.empty();
        }

        Mono<Void> chainOfResponses = Mono.empty();

        for(var spec: specs){
            var tempreplySpec = InteractionFollowupCreateSpec.builder()
                    .addEmbed(spec).build();
            chainOfResponses = chainOfResponses.then(event.createFollowup(tempreplySpec).then());
        }
        return chainOfResponses;
    }

    private String membersToMentions(List<Teammitglied> captains) {
        StringBuilder s = new StringBuilder();
        for (var captain : captains) {
            s.append(String.format(" <@%s> ", captain.getDiscordAccount()));
        }
        return s.toString();
    }
}
