package bootz.gaming.bootzbot.domain.teams.teamlinks;

import bootz.gaming.bootzbot.domain.sharedKernel.Executor;
import bootz.gaming.bootzbot.domain.teams.TeamCommand;
import bootz.gaming.bootzbot.domain.teams.TeamId;

public record AddTeamLinkTeamCommand(Executor runner, TeamId teamId, String linkId, Teamlink link,
                                     boolean isOpGG) implements TeamCommand {
}
