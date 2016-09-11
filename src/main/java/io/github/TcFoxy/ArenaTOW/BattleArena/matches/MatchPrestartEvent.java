package io.github.TcFoxy.ArenaTOW.BattleArena.matches;

import io.github.TcFoxy.ArenaTOW.BattleArena.competition.match.Match;

import java.util.List;

import mc.alk.arena.objects.teams.ArenaTeam;

public class MatchPrestartEvent extends MatchEvent {
	final List<ArenaTeam> teams;

	public MatchPrestartEvent(Match match, List<ArenaTeam> teams) {
		super(match);
		this.teams = teams;
	}

	public List<ArenaTeam> getTeams() {
		return teams;
	}
}