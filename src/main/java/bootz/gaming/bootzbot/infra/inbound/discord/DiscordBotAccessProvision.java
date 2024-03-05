package bootz.gaming.bootzbot.infra.inbound.discord;

import discord4j.common.ReactorResources;
import discord4j.core.DiscordClient;
import discord4j.core.DiscordClientBuilder;
import discord4j.core.GatewayDiscordClient;
import discord4j.core.GatewayResources;
import discord4j.core.event.EventDispatcher;
import discord4j.gateway.GatewayReactorResources;
import discord4j.gateway.intent.IntentSet;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Component;
import reactor.core.scheduler.Schedulers;

@Component
@Profile("!test")

public class DiscordBotAccessProvision {
    private final GatewayDiscordClient client;

    public DiscordBotAccessProvision(DiscordClientProperties props) {
        GatewayReactorResources reactorResources = GatewayReactorResources.builder()
                .timerTaskScheduler(Schedulers.newSingle("TTS"))
                .blockingTaskScheduler(Schedulers.newParallel("BTS",4))
                .payloadSenderScheduler(Schedulers.newParallel("PSS",4))
                .build();


        var builder = DiscordClientBuilder.create(props.getToken())
                .setReactorResources(reactorResources)
                .build();

        EventDispatcher customDispatcher = EventDispatcher.builder()
                .eventScheduler(Schedulers.newParallel("ES",4))
                .build();

        this.client = builder
                .gateway()
                .setEnabledIntents(IntentSet.none())
                .setEventDispatcher(customDispatcher)
                .login()
                .block();
    }

    public GatewayDiscordClient getClient() {
        return client;
    }
}
