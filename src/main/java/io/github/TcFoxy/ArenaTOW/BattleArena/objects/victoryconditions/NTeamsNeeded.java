package io.github.TcFoxy.ArenaTOW.BattleArena.objects.victoryconditions;



import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import io.github.TcFoxy.ArenaTOW.BattleArena.competition.match.Match;
import io.github.TcFoxy.ArenaTOW.BattleArena.objects.MatchResult;
import io.github.TcFoxy.ArenaTOW.BattleArena.objects.events.ArenaEventHandler;
import io.github.TcFoxy.ArenaTOW.BattleArena.objects.teams.ArenaTeam;
import io.github.TcFoxy.ArenaTOW.BattleArena.objects.victoryconditions.interfaces.DefinesNumTeams;
import io.github.TcFoxy.ArenaTOW.BattleArena.util.MinMax;
import mc.alk.arena.events.teams.TeamDeathEvent;

public class NTeamsNeeded extends VictoryCondition implements DefinesNumTeams{
	MinMax neededTeams;

	public NTeamsNeeded(Match match, int nTeams) {
		super(match);
		this.neededTeams = new MinMax(nTeams);
	}

	@Override
    public MinMax getNeededNumberOfTeams(){
		return neededTeams;
	}

	@SuppressWarnings("UnusedParameters")
    @ArenaEventHandler
	public void onTeamDeathEvent(TeamDeathEvent event) {
		/// Killing this player killed the team
		List<ArenaTeam> leftAlive = new ArrayList<ArenaTeam>(neededTeams.min+1);
		/// Iterate over the players to see if we have one team left standing
		for (ArenaTeam t: match.getTeams()){
			if (t.isDead())
				continue;
			leftAlive.add(t);
			if (leftAlive.size() >= neededTeams.min){ ///more than enough teams still in the match
				return;}
		}
		if (leftAlive.isEmpty()){
			match.setLosers();
			return;
		}
		if (leftAlive.size() < neededTeams.min){
			MatchResult mr = new MatchResult();
			mr.setVictors(leftAlive);
			Set<ArenaTeam> losers = new HashSet<ArenaTeam>(match.getTeams());
			losers.removeAll(leftAlive);
			mr.setLosers(losers);
			match.endMatchWithResult(mr);
		}
	}

    @Override
    public String toString(){
        return "[VC "+this.getClass().getSimpleName()+" : " + id+" nTeams="+neededTeams+"]";
    }
}
