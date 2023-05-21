package bootz.gaming.bootzbot.domain.sharedKernel;

import discord4j.common.util.Snowflake;

public class User implements Executor {

    private final Snowflake snowflake;

    public User(Snowflake snowflake) {
        this.snowflake = snowflake;
    }

    @Override
    public Long getDiscordAccount() {
        return this.snowflake.asLong();
    }

    @Override
    public boolean isAdmin() {
        return false;
    }
}
