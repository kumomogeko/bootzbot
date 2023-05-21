package bootz.gaming.bootzbot;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class BootzbotApplication {

    public static void main(String[] args) {
	/*	var client = DiscordClient.create(System.getenv("TOKEN"))
				.gateway()
				.setEnabledIntents(IntentSet.none())
				.login()
				.block();*/

        SpringApplication.run(BootzbotApplication.class, args);
    }

}
