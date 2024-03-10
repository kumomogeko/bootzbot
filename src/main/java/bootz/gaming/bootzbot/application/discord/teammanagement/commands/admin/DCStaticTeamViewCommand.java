package bootz.gaming.bootzbot.application.discord.teammanagement.commands.admin;

import bootz.gaming.bootzbot.application.discord.teammanagement.commands.AbstractRegistrableAdminCommand;
import bootz.gaming.bootzbot.domain.discord.StaticTeamViewTeamCommand;
import bootz.gaming.bootzbot.domain.discord.StaticViewService;
import bootz.gaming.bootzbot.domain.sharedKernel.Executor;
import bootz.gaming.bootzbot.application.discord.teammanagement.commands.id.ExecutorFactory;
import discord4j.core.event.domain.interaction.ChatInputInteractionEvent;
import discord4j.core.object.command.ApplicationCommandInteractionOptionValue;
import discord4j.core.object.command.ApplicationCommandOption;
import discord4j.discordjson.json.ApplicationCommandOptionData;
import discord4j.discordjson.json.ApplicationCommandRequest;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Mono;

@Component
public class DCStaticTeamViewCommand extends AbstractRegistrableAdminCommand {

    private final StaticViewService staticViewService;

    public DCStaticTeamViewCommand(ExecutorFactory executorFactory, StaticViewService staticViewService) {
        super(executorFactory);
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
    public Mono<Void> innerCommandHandler(Executor runner, ChatInputInteractionEvent event) {
        var guildId = event.getInteraction().getGuildId().orElseThrow();
        var channel = event.getInteraction().getChannelId();
        var onoff = getNonRequiredOption(event, "switch", ApplicationCommandInteractionOptionValue::asBoolean).orElse(true);
        return this.staticViewService.staticCommandHandler(new StaticTeamViewTeamCommand(runner, guildId, channel, onoff))
                .then(event.createFollowup("Hat funktioniert!").then());
    }
}
