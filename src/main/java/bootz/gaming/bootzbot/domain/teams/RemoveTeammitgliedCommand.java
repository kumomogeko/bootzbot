package bootz.gaming.bootzbot.domain.teams;

import bootz.gaming.bootzbot.domain.sharedKernel.Executor;

public record RemoveTeammitgliedCommand(Executor runner, Teammitglied teammitglied) implements Command {
}
