package bootz.gaming.bootzbot.application.discord.commands.admin;

import bootz.gaming.bootzbot.domain.discord.StaticTeamViewTeamCommand;
import bootz.gaming.bootzbot.domain.discord.StaticViewService;
import bootz.gaming.bootzbot.domain.sharedKernel.ExecutorFactory;
import bootz.gaming.bootzbot.domain.sharedKernel.RegistrableCommand;
import discord4j.core.event.domain.interaction.ChatInputInteractionEvent;
import discord4j.discordjson.json.ApplicationCommandRequest;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Mono;

import java.util.function.Function;

@Component
public class DCStaticTeamViewCommand implements RegistrableCommand {

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
                .build();
    }

    @Override
    public Function<ChatInputInteractionEvent, Mono<Void>> getCommandHandler() {
        return event -> {
            var guildId = event.getInteraction().getGuildId().orElseThrow();
            var member = event.getInteraction().getMember().orElseThrow();
            var channel = event.getInteraction().getChannelId();
            return this.executorFactory.executorFromMember(member)
                    .flatMap(executor -> this.staticViewService.registerReplyForStaticTeamView(new StaticTeamViewTeamCommand(executor, guildId, channel)))
                    .then(event.reply("Erfolgreich angelegt").withEphemeral(true));
        };
    }
}
