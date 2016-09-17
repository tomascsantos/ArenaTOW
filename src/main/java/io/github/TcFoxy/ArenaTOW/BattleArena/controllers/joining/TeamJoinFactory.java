package io.github.TcFoxy.ArenaTOW.BattleArena.controllers.joining;



import java.util.List;

import io.github.TcFoxy.ArenaTOW.BattleArena.Defaults;
import io.github.TcFoxy.ArenaTOW.BattleArena.competition.Competition;
import io.github.TcFoxy.ArenaTOW.BattleArena.objects.MatchParams;
import io.github.TcFoxy.ArenaTOW.BattleArena.objects.exceptions.NeverWouldJoinException;
import io.github.TcFoxy.ArenaTOW.BattleArena.objects.teams.ArenaTeam;

public class TeamJoinFactory {

    public static AbstractJoinHandler createTeamJoinHandler(MatchParams params) throws NeverWouldJoinException {
        return createTeamJoinHandler(params,null, null);
    }

    public static AbstractJoinHandler createTeamJoinHandler(MatchParams params, Competition competition) throws NeverWouldJoinException {
		return createTeamJoinHandler(params,competition,null);
	}

    public static AbstractJoinHandler createTeamJoinHandler(MatchParams params, List<ArenaTeam> teams) throws NeverWouldJoinException {
        AbstractJoinHandler as = createTeamJoinHandler(params, null, teams);
        return as;
    }

	public static AbstractJoinHandler createTeamJoinHandler(MatchParams params, Competition competition,
			List<ArenaTeam> teams) throws NeverWouldJoinException {
		if (params.getMaxTeams() <= Defaults.MAX_TEAMS ){
			return new AddToLeastFullTeam(params, competition, teams);	/// lets try and add players to all players first
		} else { /// finite team size
			return new BinPackAdd(params, competition, teams);
		}
	}

}
