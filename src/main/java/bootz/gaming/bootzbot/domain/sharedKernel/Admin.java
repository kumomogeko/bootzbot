package bootz.gaming.bootzbot.domain.sharedKernel;

import discord4j.common.util.Snowflake;

public class Admin implements Executor{
    private final Snowflake snowflake;

    public Admin(Snowflake snowflake) {
        this.snowflake = snowflake;
    }

    @Override
    public Snowflake getDiscordAccount() {
        return snowflake;
    }

    @Override
    public boolean isAdmin() {
        return true;
    }
}
