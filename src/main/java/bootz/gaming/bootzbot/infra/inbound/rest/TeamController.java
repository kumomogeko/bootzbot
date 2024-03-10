package bootz.gaming.bootzbot.infra.inbound.rest;

import bootz.gaming.bootzbot.application.discord.teammanagement.commands.id.ExecutorFactory;
import bootz.gaming.bootzbot.domain.teams.Team;
import bootz.gaming.bootzbot.domain.teams.TeamRepository;
import bootz.gaming.bootzbot.infra.inbound.discord.DiscordBotAccessProvision;
import org.springframework.context.annotation.Profile;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationToken;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Mono;

import java.security.Principal;
import java.util.List;

@RestController
@Profile("rest")
public class TeamController {
    private final TeamRepository repository;
    private final ExecutorFactory executorFactory;
    private final DiscordBotAccessProvision discordBotAccessProvision;

    public TeamController(TeamRepository repository, ExecutorFactory executorFactory, DiscordBotAccessProvision discordBotAccessProvision) {
        this.repository = repository;
        this.executorFactory = executorFactory;
        this.discordBotAccessProvision = discordBotAccessProvision;
    }

    @GetMapping("/api/guilds/{guild}/teams")
    Mono<ResponseEntity<List<Team>>> listTeams(@PathVariable Long guild, Authentication auth){
        JwtAuthenticationToken jwt = (JwtAuthenticationToken) auth;
        var snowflakeString = jwt.getToken().getClaimAsString("upn");
        return this.executorFactory.executorFromLong(guild, Long.parseLong(snowflakeString), discordBotAccessProvision.getClient()).flatMap(executor -> {
            System.out.println("Nutzer ist admin? " +executor.isAdmin());
            System.out.println("Nutzer ist " +executor.getDiscordAccount());
        return repository.getTeams().map(ResponseEntity::ok);
        });
    }

    @PostMapping("/api/guilds/{guild}/teams")
    Mono<ResponseEntity<Void>> saveTeam(@PathVariable Long guild, @RequestBody Team team, Authentication auth) {
        return repository.save(team).thenReturn(ResponseEntity.noContent().build());
    }
}
