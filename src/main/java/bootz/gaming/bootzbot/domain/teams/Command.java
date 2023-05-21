package bootz.gaming.bootzbot.domain.teams;

import bootz.gaming.bootzbot.domain.sharedKernel.Executor;

public interface Command {
    Executor runner();

    TeamId teamId();

}
