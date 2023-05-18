package bootz.gaming.bootzbot.infra.inbound;

import discord4j.core.DiscordClient;
import discord4j.core.GatewayDiscordClient;
import discord4j.gateway.intent.IntentSet;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Component;

@Component
@Profile("!test")

public class DiscordBotAccessProvision {
    private final GatewayDiscordClient client;

    public DiscordBotAccessProvision(DiscordClientProperties props) {
       this.client =  DiscordClient.create(props.getToken())
               .gateway()
               .setEnabledIntents(IntentSet.none())
               .login()
               .block();
    }
    public GatewayDiscordClient getClient() {
       return client;
   }
}
