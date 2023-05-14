package bootz.gaming.bootzbot.domain.teams;

import bootz.gaming.bootzbot.domain.teams.teamlinks.Teamlink;

public record AddTeamLinkCommand(Teammitglied runner,String linkId, Teamlink link, boolean isOpGG) {
}
