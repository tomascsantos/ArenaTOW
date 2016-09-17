package io.github.TcFoxy.ArenaTOW.BattleArena.events.teams;

import io.github.TcFoxy.ArenaTOW.BattleArena.events.CompetitionEvent;
import io.github.TcFoxy.ArenaTOW.BattleArena.objects.teams.ArenaTeam;

public class TeamDeathEvent extends CompetitionEvent{
	final ArenaTeam team;

	public TeamDeathEvent(ArenaTeam team) {
		this.team = team;
	}

	public ArenaTeam getTeam() {
		return team;
	}
}
