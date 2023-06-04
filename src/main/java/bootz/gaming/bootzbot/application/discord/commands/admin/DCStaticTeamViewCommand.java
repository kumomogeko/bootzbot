package bootz.gaming.bootzbot.application.discord.commands.admin;

import bootz.gaming.bootzbot.application.teams.commands.AbstractRegistrableCommand;
import bootz.gaming.bootzbot.domain.discord.StaticTeamViewTeamCommand;
import bootz.gaming.bootzbot.domain.discord.StaticViewService;
import bootz.gaming.bootzbot.domain.sharedKernel.ExecutorFactory;
import bootz.gaming.bootzbot.domain.sharedKernel.RegistrableCommand;
import bootz.gaming.bootzbot.domain.teams.teammitglied.Rolle;
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
public class DCStaticTeamViewCommand extends AbstractRegistrableCommand {

    private final ExecutorFactory executorFactory;
    private final StaticViewService staticViewService;

    public DCStaticTeamViewCommand(ExecutorFactory executorFactory, StaticViewService staticViewService) {
        this.executorFactory = executorFactory;
        this.staticViewService = staticViewService;
    }

    @Override
    public ApplicationCommandRequest getDiscordCommandRequest() {
        return ApplicationCommandRequest.builder()
                .name("staticteamlist")
                .description("Erzeugt eine statische Sicht aller Teams, die sich automatisch aktualisiert")
                .addOption(ApplicationCommandOptionData.builder()
                        .name("switch")
                        .description("Aktivieren = true, Deaktivieren= false")
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
            var channel = event.getInteraction().getChannelId();
            return this.executorFactory.executorFromMember(member)
                    .flatMap(executor -> {
                        var onoff = getNonRequiredOption(event, "switch", ApplicationCommandInteractionOptionValue::asBoolean).orElse(true);
                        return this.staticViewService.staticCommandHandler(new StaticTeamViewTeamCommand(executor, guildId, channel, onoff));
                    })
                    .then(event.reply("Hat funktioniert!").withEphemeral(true));
        };
    }
}
