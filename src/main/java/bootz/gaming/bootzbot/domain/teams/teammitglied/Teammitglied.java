package bootz.gaming.bootzbot.domain.teams.teammitglied;

import bootz.gaming.bootzbot.domain.sharedKernel.Executor;
import bootz.gaming.bootzbot.util.Entity;
import reactor.util.annotation.NonNull;

import java.util.Objects;
import java.util.Set;

@Entity
public class Teammitglied implements Executor {

    @NonNull
    private final Long discordAccount;
    private Set<Rolle> rollen;
    @NonNull
    private String leagueName;

    public Teammitglied(Long discordAccount, Set<Rolle> rollen, String leagueName) {
        if (null == discordAccount || null == leagueName) {
            throw new NullPointerException();
        }
        this.discordAccount = discordAccount;
        this.rollen = rollen;
        this.leagueName = leagueName;
    }

    public void addRolle(Rolle rolle){
        this.rollen.add(rolle);
    }

    public void removeRolle(Rolle rolle){
        if(this.rollen.size() <= 1){
            throw new RuntimeException("Spieler muss mindestens eine Rolle haben!");
        }
        this.rollen.remove(rolle);
    }

    public String getLeagueName() {
        return leagueName;
    }

    public Long getDiscordAccount() {
        return discordAccount;
    }

    @Override
    public boolean isAdmin() {
        return false;
    }

    public boolean isEqual(Teammitglied teammitglied) {
        return this.getDiscordAccount().equals(teammitglied.getDiscordAccount());
    }

    public boolean isCaptain() {
        return this.rollen.contains(Rolle.CAPTAIN);
    }


    public Set<Rolle> getRollen() {
        return this.rollen;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Teammitglied that = (Teammitglied) o;
        return discordAccount.equals(that.discordAccount);
    }

    @Override
    public int hashCode() {
        return Objects.hash(discordAccount);
    }
}
