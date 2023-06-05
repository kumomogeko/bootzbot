package bootz.gaming.bootzbot.application.discord.teammanagement.commands;

import bootz.gaming.bootzbot.domain.sharedKernel.Executor;
import discord4j.core.event.domain.interaction.ChatInputInteractionEvent;
import reactor.core.publisher.Mono;

public interface RegistrableRestrictedCommand extends RegistrableCommand {


    public Mono<Void> innerCommandHandler(Executor runner, ChatInputInteractionEvent event);

    public Mono<Executor> getPermissionCheck(ChatInputInteractionEvent event);

}
