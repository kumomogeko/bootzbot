package bootz.gaming.bootzbot.domain.teams;

import bootz.gaming.bootzbot.domain.sharedKernel.Executor;

public record RemoveTeamCommand(Executor runner, TeamId teamId) implements Command {

}
