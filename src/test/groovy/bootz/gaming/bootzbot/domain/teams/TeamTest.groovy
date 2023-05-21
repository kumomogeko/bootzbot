package bootz.gaming.bootzbot.domain.teams

import bootz.gaming.bootzbot.domain.sharedKernel.Admin
import bootz.gaming.bootzbot.domain.teams.teamlinks.AddTeamLinkCommand
import bootz.gaming.bootzbot.domain.teams.teamlinks.RemoveTeamLinkCommand
import bootz.gaming.bootzbot.domain.teams.teamlinks.Teamlink
import bootz.gaming.bootzbot.domain.teams.teammitglied.AddTeammitgliedCommand
import bootz.gaming.bootzbot.domain.teams.teammitglied.RemoveTeammitgliedCommand
import bootz.gaming.bootzbot.domain.teams.teammitglied.Teammitglied
import discord4j.common.util.Snowflake
import spock.lang.Specification

class TeamTest extends Specification {
    def discordAccount = Snowflake.of(Snowflake.DISCORD_EPOCH).asLong()
    def guildId = 1l
    def teamname = ""
    def teamId = new TeamId(guildId, teamname)

    def "Can remove Teammitglied"() {
        given:
        def teammitglied = new Teammitglied(discordAccount, Set.of(), "C")
        def admin = new Admin(Snowflake.of(2l))
        def team = new Team(guildId, teamname, [new Teammitglied(discordAccount, Set.of(), "B")], [:], null)

        when:
        team.removeTeammitglied(new RemoveTeammitgliedCommand(admin, teamId, teammitglied))

        then:
        team.getMembers().isEmpty()
    }

    def "Wont remove non-Teammitglied "() {
        given:
        def teammitglied = new Teammitglied(discordAccount, Set.of(), "C")
        def admin = new Admin(Snowflake.of(2l))
        def team = new Team(guildId, teamname, [], [:], null)

        when:
        team.removeTeammitglied(new RemoveTeammitgliedCommand(admin, teamId, teammitglied))

        then:
        thrown(RuntimeException)
    }

    def "Can add Teammitglied"() {
        given:
        def teammitglied = new Teammitglied(discordAccount, Set.of(), "B")
        def admin = new Admin(Snowflake.of(2l))
        def team = new Team(guildId, teamname, [], [:], null)

        when:
        team.addTeammitglied(new AddTeammitgliedCommand(admin, teamId, teammitglied))
        then:
        team.getMembers().contains(teammitglied)
    }

    def "Adding a Teammitglied multiple times is prevented"() {
        given:
        def teammitglied = new Teammitglied(discordAccount, Set.of(), "C")
        def admin = new Admin(Snowflake.of(2l))
        def team = new Team(guildId, teamname, [teammitglied], [:], null)

        when:
        team.addTeammitglied(new AddTeammitgliedCommand(admin, teamId, teammitglied))
        then:
        thrown(RuntimeException)
    }


    def "can remove Link"() {
        given:
        def teammitglied = new Teammitglied(discordAccount, Set.of(), "B")
        def admin = new Admin(Snowflake.of(2l))

        def teamlink = new Teamlink("Hi", "https://bootz-gaming.com/bootz-gaming-teams/")
        def team = new Team(guildId, teamname, [teammitglied], ["link": teamlink], "link")

        when:
        team.removeTeamLink(new RemoveTeamLinkCommand(admin, teamId, "link"))
        then:
        team.getLinks().isEmpty()
        team.getOpGG() != teamlink.getLink()
    }

    def "Can add a Link"() {
        given:
        def teammitglied = new Teammitglied(discordAccount, Set.of(), "B")
        def team = new Team(guildId, teamname, [new Teammitglied(discordAccount, Set.of(), "A"), teammitglied, new Teammitglied(discordAccount, Set.of(), "C")], [:], null)
        def linkId = "asdf"
        def teamlink = new Teamlink("Das ist ein Link", "https://bootz-gaming.com/bootz-gaming-teams/")

        when:
        team.addOrUpdateTeamLink(new AddTeamLinkCommand(teammitglied, teamId, linkId, teamlink, false))

        then:
        team.getLinks().containsKey(linkId)
        team.getLinks().get(linkId) == teamlink
    }

    def "Can use a link as custom OPGG"() {
        given:
        def admin = new Admin(Snowflake.of(2l))
        def team = new Team(guildId, teamname, [new Teammitglied(discordAccount, Set.of(), "A"), new Teammitglied(discordAccount, Set.of(), "C")], [:], "qwer")
        def linkId = "asdf"
        def teamlink = new Teamlink("Das ist ein Link", "https://bootz-gaming.com/bootz-gaming-teams/")

        when:
        team.addOrUpdateTeamLink(new AddTeamLinkCommand(admin, teamId, linkId, teamlink, true))

        then:
        team.getOpGG() == teamlink.getLink()
    }

    def "Rejects Links from a non member"() {
        given:
        def teammitglied = new Teammitglied(Snowflake.of(1l).asLong(), Set.of(), "B")
        def team = new Team(guildId, teamname, [new Teammitglied(discordAccount, Set.of(), "A"), new Teammitglied(discordAccount, Set.of(), "C")], [:], null)
        def linkId = "asdf"
        def teamlink = new Teamlink("Das ist ein Link", "https://bootz-gaming.com/bootz-gaming-teams/")

        when:
        team.addOrUpdateTeamLink(new AddTeamLinkCommand(teammitglied, teamId, linkId, teamlink, true))

        then:
        thrown(RuntimeException)
    }

    def "OP GG is automatically generated"() {
        given:
        def team = new Team(guildId, teamname, [new Teammitglied(discordAccount, Set.of(), "A"), new Teammitglied(discordAccount, Set.of(), "B"), new Teammitglied(discordAccount, Set.of(), "C")], [:], null)
        when:
        def result = team.getOpGG()
        then:
        result == "https://www.op.gg/multisearch/euw?summoners=A,B,C"
    }

    def "Custom OP GG is used over autogenerated"() {
        given:
        def opggLink = new Teamlink("Das opgg des Teams", "https://www.op.gg/multisearch/euw?summoners=z,y,x")
        def team = new Team(guildId, teamname, [new Teammitglied(discordAccount, Set.of(), "A"), new Teammitglied(discordAccount, Set.of(), "B"), new Teammitglied(discordAccount, Set.of(), "C")], ["opGG": opggLink], "opGG")
        when:
        def result = team.getOpGG()
        then:
        result == "https://www.op.gg/multisearch/euw?summoners=z,y,x"
    }
}
