package bootz.gaming.bootzbot.domain.teams;

import bootz.gaming.bootzbot.domain.sharedKernel.Command;
import bootz.gaming.bootzbot.domain.sharedKernel.Executor;

public interface TeamCommand extends Command {
    Executor runner();

    TeamId teamId();

}
