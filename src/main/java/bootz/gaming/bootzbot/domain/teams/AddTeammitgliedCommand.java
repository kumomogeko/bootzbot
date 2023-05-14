package bootz.gaming.bootzbot.domain.teams;

import bootz.gaming.bootzbot.domain.sharedKernel.Executor;

public record AddTeammitgliedCommand(Executor runner, Teammitglied teammitglied) implements Command {
}
