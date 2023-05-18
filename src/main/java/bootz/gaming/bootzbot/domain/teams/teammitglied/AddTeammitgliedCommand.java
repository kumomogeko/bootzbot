package bootz.gaming.bootzbot.domain.teams.teammitglied;

import bootz.gaming.bootzbot.domain.sharedKernel.Executor;
import bootz.gaming.bootzbot.domain.teams.Command;
import bootz.gaming.bootzbot.domain.teams.TeamId;

public record AddTeammitgliedCommand(Executor runner, TeamId teamId, Teammitglied teammitglied) implements Command {
}
