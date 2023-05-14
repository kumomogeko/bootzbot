package bootz.gaming.bootzbot.domain.teams;

import bootz.gaming.bootzbot.util.Entity;

import java.util.Set;

@Entity
public class Teammitglied {
    private Set<Rolle> rollen;
    private String leagueName;

    public Teammitglied(Set<Rolle> rollen, String leagueName) {
        this.rollen = rollen;
        this.leagueName = leagueName;
    }

    public String getLeagueName() {
        return leagueName;
    }
}
