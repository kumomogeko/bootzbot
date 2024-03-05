package bootz.gaming.bootzbot.infra.inbound.rest;

import bootz.gaming.bootzbot.domain.teams.Team;
import bootz.gaming.bootzbot.domain.teams.TeamRepository;
import org.springframework.context.annotation.Profile;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Mono;

import java.util.List;

@RestController
@Profile("rest")
public class TeamController {
    private final TeamRepository repository;

    public TeamController(TeamRepository repository) {
        this.repository = repository;
    }

    @GetMapping("/api/teams")
    Mono<ResponseEntity<List<Team>>> listTeams(){
        return repository.getTeams().map(ResponseEntity::ok);
    }

    @PostMapping("/api/teams")
    Mono<ResponseEntity<Void>> saveTeam(@RequestBody Team team) {
        return repository.save(team).thenReturn(ResponseEntity.noContent().build());
    }
}
