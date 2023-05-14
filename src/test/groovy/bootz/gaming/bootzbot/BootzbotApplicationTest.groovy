package bootz.gaming.bootzbot

import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.context.ApplicationContext
import spock.lang.Specification

@SpringBootTest
class BootzbotApplicationTest extends Specification {
    @Autowired
    ApplicationContext context

    def "starts"(){
        expect:
        context
    }
}
