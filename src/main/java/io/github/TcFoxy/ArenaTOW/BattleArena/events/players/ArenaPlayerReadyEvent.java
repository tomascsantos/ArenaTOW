package io.github.TcFoxy.ArenaTOW.BattleArena.events.players;

import io.github.TcFoxy.ArenaTOW.BattleArena.objects.ArenaPlayer;

/**
 * Player has either typed command or clicked block to say they are ready
 */
public class ArenaPlayerReadyEvent extends ArenaPlayerEvent{
	boolean isReady;

	public ArenaPlayerReadyEvent(ArenaPlayer arenaPlayer, boolean isReady) {
		super(arenaPlayer);
		this.isReady = isReady;
	}

	public boolean isReady(){
		return isReady;
	}
}
