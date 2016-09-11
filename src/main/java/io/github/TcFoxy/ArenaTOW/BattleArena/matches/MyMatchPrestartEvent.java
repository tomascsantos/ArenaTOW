package io.github.TcFoxy.ArenaTOW.BattleArena.matches;

import io.github.TcFoxy.ArenaTOW.BattleArena.competition.match.MyMatch;

import java.util.List;

import mc.alk.arena.objects.teams.ArenaTeam;

public class MyMatchPrestartEvent extends MyMatchEvent {
	final List<ArenaTeam> teams;

	public MyMatchPrestartEvent(MyMatch match, List<ArenaTeam> teams) {
		super(match);
		this.teams = teams;
	}

	public List<ArenaTeam> getTeams() {
		return teams;
	}
}
