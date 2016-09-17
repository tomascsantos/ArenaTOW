package io.github.TcFoxy.ArenaTOW.BattleArena.events.arenas;

import io.github.TcFoxy.ArenaTOW.BattleArena.events.BAEvent;
import io.github.TcFoxy.ArenaTOW.BattleArena.objects.arenas.Arena;

public class ArenaDeleteEvent extends BAEvent{
	final Arena arena;

	public Arena getArena() {
		return arena;
	}
	public ArenaDeleteEvent(Arena arena) {
		this.arena = arena;
	}

}
