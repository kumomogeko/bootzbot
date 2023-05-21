package bootz.gaming.bootzbot.domain.sharedKernel;

import discord4j.core.event.domain.interaction.ChatInputInteractionEvent;
import discord4j.discordjson.json.ApplicationCommandRequest;
import reactor.core.publisher.Mono;

import java.util.function.Function;

public interface RegistrableCommand {
    public ApplicationCommandRequest getDiscordCommandRequest();

    public Function<ChatInputInteractionEvent, Mono<Void>> getCommandHandler();


}
