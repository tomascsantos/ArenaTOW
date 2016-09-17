package io.github.TcFoxy.ArenaTOW.BattleArena.objects.victoryconditions;

import io.github.TcFoxy.ArenaTOW.BattleArena.competition.match.Match;

public class NoTeamsLeft extends NTeamsNeeded{
	public NoTeamsLeft(Match match) {
		super(match,1);
	}
}
