package bootz.gaming.bootzbot.domain.teams.teamlinks;

import bootz.gaming.bootzbot.domain.sharedKernel.Executor;
import bootz.gaming.bootzbot.domain.teams.Command;
import bootz.gaming.bootzbot.domain.teams.TeamId;

public record AddTeamLinkCommand(Executor runner, TeamId teamId, String linkId, Teamlink link, boolean isOpGG) implements Command {
}
