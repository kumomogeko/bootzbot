package bootz.gaming.bootzbot.infra.outbound.cassandra.staticview;

import bootz.gaming.bootzbot.domain.discord.StaticTeamView;
import discord4j.common.util.Snowflake;
import org.springframework.data.cassandra.core.cql.PrimaryKeyType;
import org.springframework.data.cassandra.core.mapping.Column;
import org.springframework.data.cassandra.core.mapping.PrimaryKeyColumn;
import org.springframework.data.cassandra.core.mapping.Table;

@Table
public class CStaticView {


    @PrimaryKeyColumn(name = "id", ordinal = 1, type = PrimaryKeyType.CLUSTERED)
    private String id;
    @PrimaryKeyColumn(name = "guildId",
    ordinal = 0,
    type = PrimaryKeyType.PARTITIONED)
    private long guildId;
    @Column
    private long channelId;

    public CStaticView() {
    }

    public CStaticView(String id, long guildId, long channelId) {
        this.id = id;
        this.guildId = guildId;
        this.channelId = channelId;
    }

    public static CStaticView fromStaticTeamView(StaticTeamView view) {
        return new CStaticView(view.id(), view.guild().asLong(), view.channel().asLong());
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
