package bootz.gaming.bootzbot.domain.teams.teammitglied;

import bootz.gaming.bootzbot.domain.sharedKernel.Executor;
import bootz.gaming.bootzbot.domain.teams.TeamCommand;
import bootz.gaming.bootzbot.domain.teams.TeamId;

public record AddUpdateTeammitgliedTeamCommand(Executor runner, TeamId teamId,
                                               Teammitglied teammitglied) implements TeamCommand {
}
