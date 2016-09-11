package io.github.TcFoxy.ArenaTOW.BattleArena.events.players;

import io.github.TcFoxy.ArenaTOW.BattleArena.objects.ArenaPlayer;
import io.github.TcFoxy.ArenaTOW.BattleArena.objects.teams.ArenaTeam;

/**
 * Signifies that the player has typed the command to leave the competition
 */
public class ArenaPlayerEnterMatchEvent extends ArenaPlayerEvent{
	final ArenaTeam team;

	public ArenaPlayerEnterMatchEvent(ArenaPlayer arenaPlayer, ArenaTeam team) {
		super(arenaPlayer);
		this.team = team;
	}

	public ArenaTeam getTeam() {
		return team;
	}

}
