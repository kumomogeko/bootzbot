package bootz.gaming.bootzbot.domain.sharedKernel;

import discord4j.common.util.Snowflake;

public interface Executor {
    Snowflake getDiscordAccount();
    boolean isAdmin();
}
