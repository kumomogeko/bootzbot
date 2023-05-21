package bootz.gaming.bootzbot.domain.teams

import bootz.gaming.bootzbot.domain.sharedKernel.Admin
import bootz.gaming.bootzbot.domain.teams.teamlinks.Teamlink
import discord4j.common.util.Snowflake
import reactor.core.publisher.Mono
import spock.lang.Specification

class TeamServiceTest extends Specification {

    def teamRepository = Mock(TeamRepository)
    def teamFactory = Mock(TeamFactory)
    def admin = new Admin(Snowflake.of(1l))

    def "Will get Link list from specified Team"() {
        given:
        def sut = new TeamService(teamRepository, teamFactory)
        def teamId = new TeamId(2l, "test")
        def command = new TeamReadCommand(admin, teamId)
        def teamlink = new Teamlink("A link", "https://bootz-gaming.com/")
        when:
        def result = sut.getLinks(command).block()

        then:
        1 * teamRepository.getTeamByTeamId(teamId) >> Mono.just(new Team(2l,"",[], ["link": teamlink],null))
        result.containsValue(teamlink)
    }
}
