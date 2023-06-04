package bootz.gaming.bootzbot.domain.teams;

import bootz.gaming.bootzbot.domain.sharedKernel.Executor;

public record RemoveTeamTeamCommand(Executor runner, TeamId teamId) implements TeamCommand {

}
