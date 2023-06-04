package bootz.gaming.bootzbot.domain.discord;


import bootz.gaming.bootzbot.application.discord.TeamEmbedViewService;
import bootz.gaming.bootzbot.domain.sharedKernel.TeamUpdatedEvent;
import bootz.gaming.bootzbot.domain.teams.TeamService;
import bootz.gaming.bootzbot.infra.inbound.DiscordBotAccessProvision;
import discord4j.common.util.Snowflake;
import discord4j.core.GatewayDiscordClient;
import org.springframework.context.event.EventListener;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.HashSet;
import java.util.Set;

@Service
public class StaticViewService {

    private final StaticViewRepository repository;
    private final TeamService teamService;
    private final TeamEmbedViewService teamEmbedViewService;
    private final GatewayDiscordClient client;

    private final Set<Snowflake> updatedGuilds;

    public StaticViewService(StaticViewRepository repository, TeamService teamService, TeamEmbedViewService teamEmbedViewService, DiscordBotAccessProvision discordBotAccessProvision) {
        this.repository = repository;
        this.teamService = teamService;
        this.client = discordBotAccessProvision.getClient();
        this.teamEmbedViewService = teamEmbedViewService;
        this.updatedGuilds = new HashSet<>();
    }

    public Mono<Void> registerReplyForStaticTeamView(StaticTeamViewTeamCommand command) {
        if (!command.runner().isAdmin()) {
            return Mono.error(new RuntimeException("Keine Berechtigung!"));
        }
        StaticTeamView view = new StaticTeamView(command);
        return this.repository.save(view);
    }

    @EventListener
    public synchronized void sendMessageOnUpdate(TeamUpdatedEvent teamUpdatedEvent) {
        this.updatedGuilds.add(Snowflake.of(teamUpdatedEvent.teamId().getGuild()));
    }

    //1000 = 1second
    @Scheduled(fixedRate = 60000)
    public void updateLaggedTeamEvents() {
        if (this.updatedGuilds.isEmpty()) {
            return;
        }
        Set<Snowflake> setCopy;
        synchronized (this) {
            setCopy = new HashSet<>(this.updatedGuilds);
            this.updatedGuilds.clear();
        }
        Flux.fromIterable(setCopy)
                .flatMap(snowflake -> repository.getByGuild(snowflake).flatMapMany(Flux::fromIterable))
                .flatMap(view -> view.sendUpdate(this.teamService, this.teamEmbedViewService, this.client))
                .subscribe();

    }


}
