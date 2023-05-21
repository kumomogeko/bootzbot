package bootz.gaming.bootzbot.domain.teams;

import bootz.gaming.bootzbot.domain.teams.teammitglied.Rolle;
import bootz.gaming.bootzbot.domain.teams.teammitglied.Teammitglied;
import reactor.util.annotation.NonNull;

import java.util.Set;

public class TeammitgliedDB {
    @NonNull
    private Long discordAccount;
    private Set<Rolle> rollen;
    @NonNull
    private String leagueName;


    public TeammitgliedDB() {
    }

    public TeammitgliedDB(Teammitglied teammitglied) {
        this.discordAccount = teammitglied.getDiscordAccount();
        this.rollen = teammitglied.getRollen();
        this.leagueName = teammitglied.getLeagueName();
    }

    public TeammitgliedDB(@NonNull Long discordAccount, Set<Rolle> rollen, @NonNull String leagueName) {
        this.discordAccount = discordAccount;
        this.rollen = rollen;
        this.leagueName = leagueName;
    }

    @NonNull
    public Long getDiscordAccount() {
        return discordAccount;
    }

    public Set<Rolle> getRollen() {
        return rollen;
    }

    public void setRollen(Set<Rolle> rollen) {
        this.rollen = rollen;
    }

    @NonNull
    public String getLeagueName() {
        return leagueName;
    }

    public void setLeagueName(@NonNull String leagueName) {
        this.leagueName = leagueName;
    }

    public Teammitglied toTeammitglied() {
        return new Teammitglied(this.discordAccount, this.rollen, this.leagueName);
    }
}
