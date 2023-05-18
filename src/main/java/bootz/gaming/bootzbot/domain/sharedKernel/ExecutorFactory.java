package bootz.gaming.bootzbot.domain.sharedKernel;

import discord4j.core.object.entity.Member;
import discord4j.rest.util.Permission;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Mono;

@Component
public class ExecutorFactory {
    public Mono<Executor> executorFromMember(Member member){
        return member.getBasePermissions().map(permissions -> permissions.asEnumSet().contains(Permission.ADMINISTRATOR))
                .map(isAdmin -> {
                    var executor = isAdmin ? new Admin(member.getId()) : new User(member.getId());
                    return executor;
                });
    }
}
