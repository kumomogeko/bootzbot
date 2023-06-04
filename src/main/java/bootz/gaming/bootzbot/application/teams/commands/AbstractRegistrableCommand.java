package bootz.gaming.bootzbot.application.teams.commands;

import bootz.gaming.bootzbot.domain.sharedKernel.RegistrableCommand;
import discord4j.core.event.domain.interaction.ChatInputInteractionEvent;
import discord4j.core.object.command.ApplicationCommandInteractionOption;
import discord4j.core.object.command.ApplicationCommandInteractionOptionValue;

import java.util.Optional;
import java.util.function.Function;

public abstract class AbstractRegistrableCommand implements RegistrableCommand {
    public <X> X getOption(ChatInputInteractionEvent event, String opName, Function<ApplicationCommandInteractionOptionValue, X> extractorFun) {
        return event.getOption(opName)
                .flatMap(ApplicationCommandInteractionOption::getValue)
                .map(extractorFun)
                .orElseThrow();
    }

    public <X> Optional<X> getNonRequiredOption(ChatInputInteractionEvent event, String opName, Function<ApplicationCommandInteractionOptionValue, X> extractorFun) {
        return event.getOption(opName)
                .flatMap(ApplicationCommandInteractionOption::getValue)
                .map(extractorFun);

    }
}
