package bootz.gaming.bootzbot.domain.teams;

import bootz.gaming.bootzbot.domain.sharedKernel.Executor;

public record RenameTeamCommand(Executor runner, TeamId teamId, String newName) implements TeamCommand {

}
