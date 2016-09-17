package io.github.TcFoxy.ArenaTOW.BattleArena.objects.victoryconditions;

import io.github.TcFoxy.ArenaTOW.BattleArena.competition.match.Match;
import io.github.TcFoxy.ArenaTOW.BattleArena.events.players.ArenaPlayerDeathEvent;
import io.github.TcFoxy.ArenaTOW.BattleArena.objects.events.ArenaEventHandler;
import io.github.TcFoxy.ArenaTOW.BattleArena.objects.events.EventPriority;
import io.github.TcFoxy.ArenaTOW.BattleArena.objects.teams.ArenaTeam;
import io.github.TcFoxy.ArenaTOW.BattleArena.objects.victoryconditions.interfaces.DefinesNumLivesPerPlayer;

public class NLives extends VictoryCondition implements DefinesNumLivesPerPlayer{
	int nLives; /// number of lives before a player is eliminated from a team

	public NLives(Match match) {
		super(match);
		nLives = 1;
	}

	public NLives(Match match, Integer maxLives) {
		super(match);
		this.nLives = maxLives;
	}

	public void setMaxLives(Integer maxLives) {
		this.nLives = maxLives;
	}

	@ArenaEventHandler( priority=EventPriority.LOW)
	public void playerDeathEvent(ArenaPlayerDeathEvent event) {
		ArenaTeam team = event.getTeam();
		Integer deaths = team.getNDeaths(event.getPlayer());
		if (deaths == null)
			deaths = 1;
		if (deaths >= nLives){
			team.killMember(event.getPlayer());}
	}

	@Override
	public int getLivesPerPlayer() {
		return nLives;
	}
}
