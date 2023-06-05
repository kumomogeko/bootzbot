package bootz.gaming.bootzbot.application.discord.teammanagement.commands;

import bootz.gaming.bootzbot.domain.sharedKernel.Executor;
import discord4j.core.event.domain.interaction.ChatInputInteractionEvent;
import reactor.core.publisher.Mono;

import java.util.function.Function;

public abstract class AbstractRegistrableAdminCommand extends AbstractRegistrableCommand implements RegistrableRestrictedCommand {

    private final ExecutorFactory executorFactory;

    protected AbstractRegistrableAdminCommand(ExecutorFactory executorFactory) {
        this.executorFactory = executorFactory;
    }


    @Override
    public final Function<ChatInputInteractionEvent, Mono<Void>> getCommandHandler() {
        return event -> this.getPermissionCheck(event).flatMap(executor -> innerCommandHandler(executor,event));
    }

    @Override
    public final Mono<Executor> getPermissionCheck(ChatInputInteractionEvent event) {
            var member = event.getInteraction().getMember().orElseThrow();
            return executorFactory
                    .executorFromMember(member)
                    .filter(Executor::isAdmin)
                    .switchIfEmpty(Mono.error(new RuntimeException("Keine Berechtigung")));
    }
}
