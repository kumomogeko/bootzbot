package bootz.gaming.bootzbot.domain.discord;

import bootz.gaming.bootzbot.application.discord.teammanagement.TeamEmbedViewService;
import bootz.gaming.bootzbot.domain.teams.TeamService;
import bootz.gaming.bootzbot.util.AggregateRoot;
import discord4j.common.util.Snowflake;
import discord4j.core.GatewayDiscordClient;
import discord4j.core.object.entity.channel.GuildMessageChannel;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.Objects;

@AggregateRoot
public final class StaticTeamView {
    private final String id;
    private final Snowflake guild;
    private final Snowflake channel;

    public StaticTeamView(String id, Snowflake guild, Snowflake channel) {
        this.id = id;
        this.guild = guild;
        this.channel = channel;
    }


    public StaticTeamView(StaticTeamViewTeamCommand command) {
        this(command.guild(), command.channel());
    }

    public StaticTeamView(Snowflake guild, Snowflake channel) {
        this(guild.asString() + channel.asString(), guild, channel);
    }

    /**
     *
     * @return An id composed of the guild id followed by the channel id
     */
    public String id() {
        return id;
    }

    public Snowflake guild() {
        return guild;
    }

    public Snowflake channel() {
        return channel;
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == this) return true;
        if (obj == null || obj.getClass() != this.getClass()) return false;
        var that = (StaticTeamView) obj;
        return Objects.equals(this.id, that.id) &&
                Objects.equals(this.guild, that.guild) &&
                Objects.equals(this.channel, that.channel);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, guild, channel);
    }

    @Override
    public String toString() {
        return "StaticTeamView[" +
                "id=" + id + ", " +
                "guild=" + guild + ", " +
                "channel=" + channel + ']';
    }


    public Mono<Void> sendUpdate(TeamService teamService, TeamEmbedViewService embedViewService, GatewayDiscordClient client) {
        var getTeam = teamService.getTeams();
        return client.getChannelById(this.channel).ofType(GuildMessageChannel.class).zipWith(getTeam)
                .flatMap(objects -> {
                    var channel = objects.getT1();
                    var teamsList = objects.getT2();
                    return Flux.fromIterable(embedViewService.createTeamSpec(teamsList))
                            .flatMap(channel::createMessage).then();
                });
    }

}
