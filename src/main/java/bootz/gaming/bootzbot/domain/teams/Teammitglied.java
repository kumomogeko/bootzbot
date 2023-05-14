package bootz.gaming.bootzbot.domain.teams;

import bootz.gaming.bootzbot.domain.sharedKernel.Executor;
import bootz.gaming.bootzbot.util.Entity;
import discord4j.common.util.Snowflake;

import java.util.Set;

@Entity
public class Teammitglied implements Executor {


    private final Snowflake discordAccount;
    private Set<Rolle> rollen;
    private String leagueName;

    public Teammitglied(Snowflake discordAccount, Set<Rolle> rollen, String leagueName) {
        this.discordAccount = discordAccount;
        this.rollen = rollen;
        this.leagueName = leagueName;
    }

    public String getLeagueName() {
        return leagueName;
    }

    public Snowflake getDiscordAccount() {
        return discordAccount;
    }

    @Override
    public boolean isAdmin() {
        return false;
    }
}