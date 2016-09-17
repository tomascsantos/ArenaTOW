package io.github.TcFoxy.ArenaTOW.BattleArena.events.arenas;

import io.github.TcFoxy.ArenaTOW.BattleArena.events.BAEvent;
import io.github.TcFoxy.ArenaTOW.BattleArena.objects.arenas.Arena;

public class ArenaCreateEvent extends BAEvent{
	final Arena arena;

	public Arena getArena() {
		return arena;
	}
	public ArenaCreateEvent(Arena arena) {
		this.arena = arena;
	}

}
