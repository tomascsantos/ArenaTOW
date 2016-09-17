package io.github.TcFoxy.ArenaTOW.BattleArena.objects.victoryconditions;

import io.github.TcFoxy.ArenaTOW.BattleArena.competition.match.Match;

public class InfiniteLives extends NLives{

	public InfiniteLives(Match match) {
		super(match, Integer.MAX_VALUE);
	}

}
