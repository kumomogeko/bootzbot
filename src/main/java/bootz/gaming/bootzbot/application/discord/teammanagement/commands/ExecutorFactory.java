package bootz.gaming.bootzbot.application.discord.teammanagement.commands;

import bootz.gaming.bootzbot.domain.sharedKernel.Admin;
import bootz.gaming.bootzbot.domain.sharedKernel.Executor;
import bootz.gaming.bootzbot.domain.sharedKernel.User;
import discord4j.core.object.entity.Member;
import discord4j.rest.util.Permission;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Mono;

@Component
public class ExecutorFactory {
    public Mono<Executor> executorFromMember(Member member) {
        return member.getBasePermissions().map(permissions -> permissions.asEnumSet().contains(Permission.ADMINISTRATOR))
                .map(isAdmin -> isAdmin ? new Admin(member.getId()) : new User(member.getId()));
    }
}
