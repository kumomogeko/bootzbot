package bootz.gaming.bootzbot.application.discord.teammanagement.commands.generic;

import bootz.gaming.bootzbot.application.discord.teammanagement.commands.RegistrableCommand;
import discord4j.core.event.domain.interaction.ChatInputInteractionEvent;
import discord4j.core.object.command.ApplicationCommandOption;
import discord4j.discordjson.json.ApplicationCommandOptionData;
import discord4j.discordjson.json.ApplicationCommandRequest;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Mono;

import java.util.function.Function;

@Component
public class PongCommand implements RegistrableCommand {

    @Override
    public ApplicationCommandRequest getDiscordCommandRequest() {
        return ApplicationCommandRequest.builder()
                .name("ping")
                .description("Pongs you after pinging it")
                .addOption(ApplicationCommandOptionData.builder()
                        .name("name")
                        .description("Your name")
                        .type(ApplicationCommandOption.Type.STRING.getValue())
                        .required(false).build())
                .build();
    }

    @Override
    public Function<ChatInputInteractionEvent, Mono<Void>> getCommandHandler() {
        return event -> event.reply("Pong!");
    }
}
