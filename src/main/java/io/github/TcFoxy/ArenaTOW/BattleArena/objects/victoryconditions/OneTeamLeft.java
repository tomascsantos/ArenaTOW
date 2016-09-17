package io.github.TcFoxy.ArenaTOW.BattleArena.objects.victoryconditions;

import io.github.TcFoxy.ArenaTOW.BattleArena.competition.match.Match;

public class OneTeamLeft extends NTeamsNeeded{
	public OneTeamLeft(Match match) {
		super(match,2);
	}
}
