package bootz.gaming.bootzbot.domain.teams;

import bootz.gaming.bootzbot.domain.sharedKernel.Executor;
import bootz.gaming.bootzbot.domain.teams.teamlinks.AddTeamLinkCommand;
import bootz.gaming.bootzbot.domain.teams.teamlinks.RemoveTeamLinkCommand;
import bootz.gaming.bootzbot.domain.teams.teamlinks.Teamlink;
import bootz.gaming.bootzbot.domain.teams.teammitglied.AddTeammitgliedCommand;
import bootz.gaming.bootzbot.domain.teams.teammitglied.RemoveTeammitgliedCommand;
import bootz.gaming.bootzbot.domain.teams.teammitglied.Teammitglied;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;

import java.util.Collection;
import java.util.List;
import java.util.function.BiConsumer;
import java.util.function.Function;

@Service
public class TeamService {
    private final TeamRepository repository;
    private final TeamFactory teamFactory;

    public TeamService(TeamRepository repository, TeamFactory teamFactory) {
        this.repository = repository;
        this.teamFactory = teamFactory;
    }

    public Mono<Void> addTeam(AddTeamCommand command) {
        if (notAllowedToExecuteTeamAdminAction(command.runner())) {
            return Mono.error(new RuntimeException("Keine Berechtigung!"));
        }
        return this.repository
                .getTeamByTeamId(command.teamId())
                .map(team -> {
                    throw new RuntimeException("Team existiert bereits!");
                }).thenEmpty(Mono.empty())
                .switchIfEmpty(repository.save(teamFactory.fromTeamId(command.teamId())));
    }

    public Mono<Void> removeTeam(RemoveTeamCommand command) {
        if (notAllowedToExecuteTeamAdminAction(command.runner())) {
            return Mono.error(new RuntimeException("Keine Berechtigung!"));
        }
        return this.repository.delete(command.teamId());
    }

    public Mono<Void> addTeammitglied(AddTeammitgliedCommand command) {
        return this.executeTeamActionFromRepoAndSave(Team::addTeammitglied, command);
    }


    public Mono<Void> removeTeammitglied(RemoveTeammitgliedCommand command) {
        return this.executeTeamActionFromRepoAndSave(Team::removeTeammitglied, command);
    }

    public Mono<Void> addTeamlink(AddTeamLinkCommand command) {
        return this.executeTeamActionFromRepoAndSave(Team::addOrUpdateTeamLink, command);
    }

    public Mono<Void> removeTeamlink(RemoveTeamLinkCommand command) {
        return this.executeTeamActionFromRepoAndSave(Team::removeTeamLink, command);
    }

    public Mono<String> getOpgg(TeamReadCommand command) {
        return this.executeTeamReadActionFromRepo(Team::getOpGG, command);
    }

    public Mono<List<Teammitglied>> listMembers(TeamReadCommand command) {
        return this.executeTeamReadActionFromRepo(Team::getMembers, command);
    }

    public Mono<Collection<Teamlink>> getLinks(TeamReadCommand command) {
        return this.executeTeamReadActionFromRepo(Team::getLinklist, command);
    }

    private boolean notAllowedToExecuteTeamAdminAction(Executor executor) {
        return !executor.isAdmin();
    }

    private <X extends Command> Mono<Void> executeTeamActionFromRepoAndSave(BiConsumer<Team, X> consumer, X command) {
        return this.repository
                .getTeamByTeamId(command.teamId())
                .map(team -> {
                    consumer.accept(team, command);
                    return team;
                }).flatMap(this.repository::save);
    }

    private <X extends Command, Y> Mono<Y> executeTeamReadActionFromRepo(Function<Team, Y> consumer, X command) {
        return this.repository
                .getTeamByTeamId(command.teamId())
                .map(consumer);
    }

}
