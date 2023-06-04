package bootz.gaming.bootzbot.domain.discord;

import bootz.gaming.bootzbot.domain.sharedKernel.Command;
import bootz.gaming.bootzbot.domain.sharedKernel.Executor;
import discord4j.common.util.Snowflake;

public record StaticTeamViewTeamCommand(Executor runner, Snowflake guild, Snowflake channel, Boolean on) implements Command {

}
