package bootz.gaming.bootzbot.application;

import bootz.gaming.bootzbot.domain.sharedKernel.RegistrableCommand;
import bootz.gaming.bootzbot.infra.inbound.DiscordBotAccessProvision;
import discord4j.core.GatewayDiscordClient;
import discord4j.core.event.domain.interaction.ApplicationCommandInteractionEvent;
import discord4j.core.event.domain.interaction.ChatInputInteractionEvent;
import discord4j.discordjson.json.ApplicationCommandRequest;
import discord4j.rest.RestClient;
import discord4j.rest.service.ApplicationService;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.annotation.Profile;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Component
@Profile("!test")
public class DiscordEventRegistrar {
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
        this.registerCommands();
        this.reactCommands();
    }

    public void registerCommands() {
        RestClient restClient = client.getRestClient();
        var appId = 1108533746627530802L;
        //var guildId = 1108533746627530802L;
        ApplicationService applicationService = restClient.getApplicationService();
        for (var command : commandRegistrarList) {
            ApplicationCommandRequest discordCommandRequest = command.getDiscordCommandRequest();
            applicationService.createGlobalApplicationCommand(appId, discordCommandRequest).subscribe();
            commandList.put(discordCommandRequest.name(), command);
            System.out.printf("Command registered: %s%n", discordCommandRequest.name());
        }
    }

    public void reactCommands() {
        client
                .on(ChatInputInteractionEvent.class, event ->
                        this.commandList.get(event.getCommandName())
                                .getCommandHandler()
                                .apply(event))
                .subscribe();
    }
}
