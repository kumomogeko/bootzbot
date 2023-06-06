package bootz.gaming.bootzbot.application.discord.teammanagement.commands;

import bootz.gaming.bootzbot.infra.inbound.DiscordBotAccessProvision;
import discord4j.core.GatewayDiscordClient;
import discord4j.core.event.domain.interaction.ChatInputInteractionEvent;
import discord4j.discordjson.json.ApplicationCommandData;
import discord4j.discordjson.json.ApplicationCommandRequest;
import discord4j.rest.RestClient;
import discord4j.rest.service.ApplicationService;
import org.slf4j.LoggerFactory;
import org.slf4j.Logger;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.annotation.Profile;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Component
@Profile("!test")
public class DiscordEventRegistrar {

    Logger log = LoggerFactory.getLogger(DiscordEventRegistrar.class);
    private final List<RegistrableCommand> commandRegistrarList;
    private final GatewayDiscordClient client;
    private final Map<String, RegistrableCommand> commandList;

    public DiscordEventRegistrar(List<RegistrableCommand> commands, DiscordBotAccessProvision discordBotAccessProvision) {
        this.commandRegistrarList = commands;
        this.client = discordBotAccessProvision.getClient();
        this.commandList = new HashMap<>();
    }

    @EventListener(ApplicationReadyEvent.class)
    public void doAllAfterStartup() {
        var appId = Long.parseLong(System.getenv("APP_ID"));
        var client = this.client.getRestClient();
        this.registerCommands(appId, client);
        this.removeOldCommands(appId, client);
        this.reactCommands();
    }

    public void registerCommands(long appId, RestClient restClient) {
        var commandRequestList = new ArrayList<ApplicationCommandRequest>();
        for (var command : commandRegistrarList) {
            ApplicationCommandRequest discordCommandRequest = command.getDiscordCommandRequest();
            commandRequestList.add(discordCommandRequest);
            //applicationService.createGlobalApplicationCommand(appId, discordCommandRequest).subscribe();
            commandList.put(discordCommandRequest.name(), command);
            log.info("Command registered: {}", discordCommandRequest.name());
        }
        restClient.getApplicationService().bulkOverwriteGlobalApplicationCommand(appId, commandRequestList).subscribe();
    }

    public void removeOldCommands(long appId, RestClient restClient) {
        //var guildId = 1108533746627530802L;
        ApplicationService applicationService = restClient.getApplicationService();
        applicationService.getGlobalApplicationCommands(appId)
                .filter(applicationCommandData -> !commandList.containsKey(applicationCommandData.name()))
                .doOnNext(applicationCommandData -> {
                    log.info("Removing old command: " + applicationCommandData.name());
                })
                .map(ApplicationCommandData::id)
                .flatMap(id -> applicationService.deleteGlobalApplicationCommand(appId, id.asLong()))
                .subscribe();
    }

    public void reactCommands() {
        client
                .on(ChatInputInteractionEvent.class, event -> {
                    log.info("received event for command: {}",event.getCommandName());
                    return this.commandList.get(event.getCommandName())
                            .getCommandHandler()
                            .apply(event)
                            .doOnSuccess(unused -> log.info("completed work for command: {}", event.getCommandName()))
                            .onErrorResume(throwable -> event.reply(String.format("Fehler: %s", throwable.getMessage())));
                })
                .subscribe();
    }
}
