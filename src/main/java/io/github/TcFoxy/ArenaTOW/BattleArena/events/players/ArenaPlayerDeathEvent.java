package io.github.TcFoxy.ArenaTOW.BattleArena.events.players;

import org.bukkit.event.entity.PlayerDeathEvent;

import io.github.TcFoxy.ArenaTOW.BattleArena.objects.ArenaPlayer;
import io.github.TcFoxy.ArenaTOW.BattleArena.objects.teams.ArenaTeam;

public class ArenaPlayerDeathEvent extends ArenaPlayerEvent{
	final ArenaTeam team;
	PlayerDeathEvent event;
	boolean exiting = false;

	public ArenaPlayerDeathEvent(ArenaPlayer arenaPlayer, ArenaTeam team) {
		super(arenaPlayer);
		this.team = team;
	}

	public ArenaTeam getTeam() {
		return team;
	}

	public void setPlayerDeathEvent(PlayerDeathEvent event){
		this.event = event;
	}
	public PlayerDeathEvent getPlayerDeathEvent() {
		return event;
	}

	public boolean isTrueDeath() {
		return event != null;
	}

	public boolean isExiting() {
		return exiting;
	}

	public void setExiting(boolean exiting) {
		this.exiting = exiting;
	}

}
