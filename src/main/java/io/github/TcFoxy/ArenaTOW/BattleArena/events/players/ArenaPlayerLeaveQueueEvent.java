package io.github.TcFoxy.ArenaTOW.BattleArena.events.players;

import io.github.TcFoxy.ArenaTOW.BattleArena.objects.ArenaPlayer;
import io.github.TcFoxy.ArenaTOW.BattleArena.objects.MatchParams;
import io.github.TcFoxy.ArenaTOW.BattleArena.objects.arenas.Arena;
import io.github.TcFoxy.ArenaTOW.BattleArena.objects.joining.ArenaMatchQueue;

public class ArenaPlayerLeaveQueueEvent extends ArenaPlayerEvent {
//    final ArenaTeam team;
    final MatchParams params;
    final Arena arena;


    public ArenaPlayerLeaveQueueEvent(ArenaPlayer arenaPlayer, MatchParams params, Arena arena) {
        super(arenaPlayer);
        this.params = params;
        this.arena = arena;
    }

//    public ArenaTeam getTeam() {
//        return team;
//    }

    public MatchParams getParams() {
        return params;
    }

	public Arena getArena() {
        return arena;
    }

    public int getPlayersInArenaQueue(Arena arena) {
        return ArenaMatchQueue.getPlayersInArenaQueue(arena);
    }
//
//	public int getNPlayers(){
//		return ptp.getNPlayersInQueue();
//	}
}
