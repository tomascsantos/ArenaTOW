package io.github.TcFoxy.ArenaTOW.BattleArena.events.teams;

import io.github.TcFoxy.ArenaTOW.BattleArena.events.BAEvent;
import io.github.TcFoxy.ArenaTOW.BattleArena.objects.MatchParams;
import io.github.TcFoxy.ArenaTOW.BattleArena.objects.teams.ArenaTeam;

public class TeamLeftQueueEvent extends BAEvent{
	final ArenaTeam team;
	MatchParams params;

	public TeamLeftQueueEvent(ArenaTeam team, MatchParams params) {
		this.team = team;
		this.params = params;
	}

	public ArenaTeam getTeam(){
		return team;
	}
	public MatchParams getParams(){
		return params;
	}
}
