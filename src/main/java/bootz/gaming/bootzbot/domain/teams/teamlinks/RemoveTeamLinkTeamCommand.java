package bootz.gaming.bootzbot.domain.teams.teamlinks;

import bootz.gaming.bootzbot.domain.sharedKernel.Executor;
import bootz.gaming.bootzbot.domain.teams.TeamCommand;
import bootz.gaming.bootzbot.domain.teams.TeamId;

public record RemoveTeamLinkTeamCommand(Executor runner, TeamId teamId, String linkId) implements TeamCommand {
}
