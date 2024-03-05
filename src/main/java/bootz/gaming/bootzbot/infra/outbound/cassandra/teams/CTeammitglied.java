package bootz.gaming.bootzbot.infra.outbound.cassandra.teams;

import bootz.gaming.bootzbot.domain.teams.teammitglied.Rolle;
import bootz.gaming.bootzbot.domain.teams.teammitglied.Teammitglied;
import org.springframework.data.annotation.PersistenceCreator;
import org.springframework.data.cassandra.core.mapping.UserDefinedType;

import java.util.HashSet;
import java.util.Set;

@UserDefinedType("teammitglied")
public class CTeammitglied {
    private Long discordAccount;
    private Set<Rolle> rollen = new HashSet<>();
    private String leagueName;

    @PersistenceCreator
    public CTeammitglied(Long discordAccount, Set<Rolle> rollen, String leagueName) {
        this.discordAccount = discordAccount;
        this.rollen = rollen;
        this.leagueName = leagueName;
    }

    public CTeammitglied(Teammitglied teammitglied) {
        this.discordAccount = teammitglied.getDiscordAccount();
        this.rollen = teammitglied.getRollen();
        this.leagueName = teammitglied.getLeagueName();
    }

    public Teammitglied toTeammitglied() {
        return new Teammitglied(this.discordAccount, new HashSet<>(this.rollen), this.leagueName);
    }
}
