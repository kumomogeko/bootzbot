package bootz.gaming.bootzbot.domain.teams.teammitglied;

import bootz.gaming.bootzbot.domain.sharedKernel.Executor;
import bootz.gaming.bootzbot.domain.teams.TeamCommand;
import bootz.gaming.bootzbot.domain.teams.TeamId;
import discord4j.common.util.Snowflake;

public record AddTeammitgliedRollenTeamCommand(Executor runner, TeamId teamId, Snowflake teammitglied, Rolle rolle) implements TeamCommand {

}
