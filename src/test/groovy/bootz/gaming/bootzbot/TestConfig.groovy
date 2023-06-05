package bootz.gaming.bootzbot

import bootz.gaming.bootzbot.infra.inbound.DiscordBotAccessProvision
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.context.annotation.Profile
import spock.mock.DetachedMockFactory

@Profile("test")
@Configuration
class TestConfig {

    def mockFactory = new DetachedMockFactory();

    @Bean
    DiscordBotAccessProvision accessProvision() {
        return mockFactory.Mock(DiscordBotAccessProvision)
    }
}
