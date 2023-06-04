package bootz.gaming.bootzbot.domain.sharedKernel;

import bootz.gaming.bootzbot.domain.teams.TeamId;

public record TeamUpdatedEvent(TeamId teamId) implements DomainEvent {

}
