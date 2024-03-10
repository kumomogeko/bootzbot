package bootz.gaming.bootzbot.application.discord.teammanagement.commands.id;

import bootz.gaming.bootzbot.domain.sharedKernel.Admin;
import bootz.gaming.bootzbot.domain.sharedKernel.Executor;
import bootz.gaming.bootzbot.domain.sharedKernel.User;
import discord4j.common.util.Snowflake;
import discord4j.core.GatewayDiscordClient;
import discord4j.core.object.entity.Member;
import discord4j.rest.util.Permission;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Mono;

import java.util.Arrays;
import java.util.List;

@Component
public class ExecutorFactory {

    private final List<Long> adminlist;

    public ExecutorFactory(ExecutorFactoryConfigProperties config) {
        this.adminlist = Arrays.asList(config.getAdmins());
    }

    public Mono<Executor> executorFromMember(Member member) {
        return member.getBasePermissions().map(permissions -> permissions.asEnumSet().contains(Permission.ADMINISTRATOR))
                .map(isAdmin -> adminlist.contains(member.getId().asLong()) || isAdmin)
                .map(isAdmin -> isAdmin ? new Admin(member.getId()) : new User(member.getId()));
    }

    public Mono<Executor> executorFromLong(Long guild, Long user, GatewayDiscordClient client) {
        return client.getMemberById(Snowflake.of(guild), Snowflake.of(user)).flatMap(this::executorFromMember);
    }
}
