package bootz.gaming.bootzbot.domain.teams;

import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.HashMap;

@Component
public class TeamFactory {
    public Team fromTeamId(TeamId teamId) {
        return new Team(teamId.getGuild(), teamId.getName(), new ArrayList<>(), new HashMap<>(), null);
    }
}
