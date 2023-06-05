package bootz.gaming.bootzbot.domain.teams;

import bootz.gaming.bootzbot.domain.sharedKernel.DomainEventPublisher;
import bootz.gaming.bootzbot.domain.sharedKernel.Executor;
import bootz.gaming.bootzbot.domain.sharedKernel.TeamUpdatedEvent;
import bootz.gaming.bootzbot.domain.teams.teamlinks.AddTeamLinkTeamCommand;
import bootz.gaming.bootzbot.domain.teams.teamlinks.RemoveTeamLinkTeamCommand;
import bootz.gaming.bootzbot.domain.teams.teamlinks.Teamlink;
import bootz.gaming.bootzbot.domain.teams.teammitglied.AddUpdateTeammitgliedTeamCommand;
import bootz.gaming.bootzbot.domain.teams.teammitglied.RemoveTeammitgliedTeamCommand;
import bootz.gaming.bootzbot.domain.teams.teammitglied.Teammitglied;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;

import java.util.List;
import java.util.Map;
import java.util.function.BiConsumer;
import java.util.function.Function;

@Service
public class TeamService {
    private final TeamRepository repository;
    private final TeamFactory teamFactory;
    private final DomainEventPublisher publisher;

    public TeamService(TeamRepository repository, TeamFactory teamFactory, DomainEventPublisher publisher) {
        this.repository = repository;
        this.teamFactory = teamFactory;
        this.publisher = publisher;
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
                .switchIfEmpty(repository.save(teamFactory.fromTeamId(command.teamId())))
                .doOnSuccess(unused -> this.publisher.publishEvent(new TeamUpdatedEvent(command.teamId())));
    }

    public Mono<Void> removeTeam(RemoveTeamCommand command) {
        if (notAllowedToExecuteTeamAdminAction(command.runner())) {
            return Mono.error(new RuntimeException("Keine Berechtigung!"));
        }
        return this.repository.delete(command.teamId())
                .doOnSuccess(unused -> this.publisher.publishEvent(new TeamUpdatedEvent(command.teamId())));
    }

    public Mono<Void> renameTeam(RenameTeamCommand command){
        return this.repository.getTeamByTeamId(new TeamId(command.teamId().getGuild(), command.newName()))
                .map(team -> {throw new RuntimeException("Teamname ist nicht frei!");})
                .then()
                .then(this.executeTeamActionFromRepoAndSave(Team::renameTeam, command).then(this.repository.delete(command.teamId())));
    }

    public Mono<List<Team>> getTeams() {
        return this.repository.getTeams();
    }

    public Mono<Void> addOrUpdateTeammitglied(AddUpdateTeammitgliedTeamCommand command) {
        return this.executeTeamActionFromRepoAndSave(Team::addOrUpdateTeammitglied, command);
    }


    public Mono<Void> removeTeammitglied(RemoveTeammitgliedTeamCommand command) {
        return this.executeTeamActionFromRepoAndSave(Team::removeTeammitglied, command);
    }

    public Mono<Void> addTeamlink(AddTeamLinkTeamCommand command) {
        return this.executeTeamActionFromRepoAndSave(Team::addOrUpdateTeamLink, command);
    }

    public Mono<Void> removeTeamlink(RemoveTeamLinkTeamCommand command) {
        return this.executeTeamActionFromRepoAndSave(Team::removeTeamLink, command);
    }

    public Mono<String> getOpgg(TeamReadTeamCommand command) {
        return this.executeTeamReadActionFromRepo(Team::getOpGG, command);
    }

    public Mono<List<Teammitglied>> listMembers(TeamReadTeamCommand command) {
        return this.executeTeamReadActionFromRepo(Team::getMembers, command);
    }

    public Mono<Map<String, Teamlink>> getLinks(TeamReadTeamCommand command) {
        return this.executeTeamReadActionFromRepo(Team::getLinklist, command);
    }

    private boolean notAllowedToExecuteTeamAdminAction(Executor executor) {
        return !executor.isAdmin();
    }

    private <X extends TeamCommand> Mono<Void> executeTeamActionFromRepoAndSave(BiConsumer<Team, X> consumer, X command) {
        return this.repository
                .getTeamByTeamId(command.teamId())
                .switchIfEmpty(Mono.error(new RuntimeException("Team nicht gefunden oder existiert nicht!")))
                .map(team -> {
                    consumer.accept(team, command);
                    return team;
                }).flatMap(this.repository::save).doOnSuccess(unused -> {
                    this.publisher.publishEvent(new TeamUpdatedEvent(command.teamId()));
                });
    }

    private <X extends TeamCommand, Y> Mono<Y> executeTeamReadActionFromRepo(Function<Team, Y> consumer, X command) {
        return this.repository
                .getTeamByTeamId(command.teamId())
                .switchIfEmpty(Mono.error(new RuntimeException("Team nicht gefunden oder existiert nicht!")))
                .map(consumer);
    }

}
