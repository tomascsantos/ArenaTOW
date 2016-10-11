package io.github.TcFoxy.ArenaTOW.BattleArena.events.players;

import io.github.TcFoxy.ArenaTOW.BattleArena.objects.ArenaLocation;
import io.github.TcFoxy.ArenaTOW.BattleArena.objects.ArenaPlayer;
import io.github.TcFoxy.ArenaTOW.BattleArena.objects.LocationType;
import io.github.TcFoxy.ArenaTOW.BattleArena.objects.TeleportDirection;
import io.github.TcFoxy.ArenaTOW.BattleArena.objects.arenas.ArenaType;
import io.github.TcFoxy.ArenaTOW.BattleArena.objects.teams.ArenaTeam;



public class ArenaPlayerTeleportEvent extends ArenaPlayerEvent{
	final ArenaTeam team;
	final ArenaLocation src;
	final ArenaLocation dest;
	final TeleportDirection direction;
	final ArenaType arenaType;

	public ArenaPlayerTeleportEvent(ArenaType at, ArenaPlayer arenaPlayer, ArenaTeam team,
			ArenaLocation src, ArenaLocation dest, TeleportDirection direction) {
		super(arenaPlayer);
		this.arenaType = at;
		this.team = team;
		this.src = src;
		this.dest = dest;
		this.direction = direction;
	}

	public ArenaType getArenaType(){
		return arenaType;
	}

	public ArenaTeam getTeam() {
		return team;
	}

	public TeleportDirection getDirection(){
		return direction;
	}

	public LocationType getSrcType(){
		return src.getType();
	}

	public LocationType getDestType(){
		return dest.getType();
	}

	public ArenaLocation getSrcLocation() {
		return src;
	}

	public ArenaLocation getDestLocation() {
		return dest;
	}
}
