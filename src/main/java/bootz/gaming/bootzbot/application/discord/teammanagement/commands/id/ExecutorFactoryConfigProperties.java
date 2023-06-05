package bootz.gaming.bootzbot.application.discord.teammanagement.commands.id;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

@Configuration
@ConfigurationProperties("teammanagement.id")
public class ExecutorFactoryConfigProperties {

    Long[] admins;

    public Long[] getAdmins() {
        return admins;
    }

    public void setAdmins(Long[] admins) {
        this.admins = admins;
    }
}
