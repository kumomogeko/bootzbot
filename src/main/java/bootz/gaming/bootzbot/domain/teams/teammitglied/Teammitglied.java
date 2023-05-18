package bootz.gaming.bootzbot.domain.teams.teammitglied;

import bootz.gaming.bootzbot.domain.sharedKernel.Executor;
import bootz.gaming.bootzbot.util.Entity;
import discord4j.common.util.Snowflake;
import reactor.util.annotation.NonNull;

import java.util.Set;

@Entity
public class Teammitglied implements Executor {

    @NonNull
    private final Snowflake discordAccount;
    private Set<Rolle> rollen;
    @NonNull
    private String leagueName;

    public Teammitglied(Snowflake discordAccount, Set<Rolle> rollen, String leagueName) {
        if(null == discordAccount || null == leagueName){
            throw new NullPointerException();
        }
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

    public boolean isEqual(Teammitglied teammitglied){
       return this.getDiscordAccount().equals(teammitglied.getDiscordAccount());
    }

    public boolean isCaptain(){
        return this.rollen.contains(Rolle.CAPTAIN);
    }
}
