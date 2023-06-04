package bootz.gaming.bootzbot;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication
@EnableScheduling
public class BootzbotApplication {

    public static void main(String[] args) {
        SpringApplication.run(BootzbotApplication.class, args);
    }

}
