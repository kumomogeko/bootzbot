package bootz.gaming.bootzbot.domain.discord;

import bootz.gaming.bootzbot.application.discord.TeamEmbedViewService;
import bootz.gaming.bootzbot.domain.teams.TeamService;
import discord4j.common.util.Snowflake;
import discord4j.core.GatewayDiscordClient;
import discord4j.core.object.entity.channel.GuildMessageChannel;
import reactor.core.publisher.Mono;

import java.util.Objects;

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
                    System.out.println("Sending updated team list");
                    return channel.createMessage(embedViewService.createTeamSpec(teamsList)).then();
                });
    }

}
