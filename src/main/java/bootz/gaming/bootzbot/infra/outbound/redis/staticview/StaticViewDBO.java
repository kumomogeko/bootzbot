package bootz.gaming.bootzbot.infra.outbound.redis.staticview;

import bootz.gaming.bootzbot.domain.discord.StaticTeamView;
import discord4j.common.util.Snowflake;

public class StaticViewDBO {

    private String id;
    private long guildId;
    private long channelId;

    public StaticViewDBO() {
    }

    public StaticViewDBO(String id, long guildId, long channelId) {
        this.id = id;
        this.guildId = guildId;
        this.channelId = channelId;
    }

    public static StaticViewDBO fromStaticTeamView(StaticTeamView view) {
        return new StaticViewDBO(view.id(), view.guild().asLong(), view.channel().asLong());
    }

    public StaticTeamView toStaticTeamView() {
        return new StaticTeamView(this.id, Snowflake.of(this.guildId), Snowflake.of(this.channelId));
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public long getGuildId() {
        return guildId;
    }

    public void setGuildId(long guildId) {
        this.guildId = guildId;
    }

    public long getChannelId() {
        return channelId;
    }

    public void setChannelId(long channelId) {
        this.channelId = channelId;
    }

}
