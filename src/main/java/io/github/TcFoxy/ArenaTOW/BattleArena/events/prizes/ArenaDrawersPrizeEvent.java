package io.github.TcFoxy.ArenaTOW.BattleArena.events.prizes;

import java.util.Collection;

import io.github.TcFoxy.ArenaTOW.BattleArena.competition.Competition;
import io.github.TcFoxy.ArenaTOW.BattleArena.objects.teams.ArenaTeam;


public class ArenaDrawersPrizeEvent extends ArenaPrizeEvent {

	public ArenaDrawersPrizeEvent(Competition competition, Collection<ArenaTeam> teams) {
		super(competition, teams);
	}

}
