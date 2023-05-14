package bootz.gaming.bootzbot.domain.teams;

import bootz.gaming.bootzbot.domain.sharedKernel.Executor;
import bootz.gaming.bootzbot.domain.teams.teamlinks.Teamlink;

public record AddTeamLinkCommand(Executor runner, String linkId, Teamlink link, boolean isOpGG) implements Command {
}
