package io.github.TcFoxy.ArenaTOW.BattleArena.objects.victoryconditions;

import io.github.TcFoxy.ArenaTOW.BattleArena.competition.match.Match;
import io.github.TcFoxy.ArenaTOW.BattleArena.objects.arenas.ArenaListener;

public class ChangeStateCondition implements ArenaListener{
	protected final Match match;

	public ChangeStateCondition(Match match){
		this.match = match;
	}
}
