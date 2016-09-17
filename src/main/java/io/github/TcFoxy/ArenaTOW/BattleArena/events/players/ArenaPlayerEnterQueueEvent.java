package io.github.TcFoxy.ArenaTOW.BattleArena.events.players;

import io.github.TcFoxy.ArenaTOW.BattleArena.objects.ArenaPlayer;
import io.github.TcFoxy.ArenaTOW.BattleArena.objects.arenas.Arena;
import io.github.TcFoxy.ArenaTOW.BattleArena.objects.joining.TeamJoinObject;
import io.github.TcFoxy.ArenaTOW.BattleArena.objects.pairs.JoinResult;
import io.github.TcFoxy.ArenaTOW.BattleArena.objects.teams.ArenaTeam;

public class ArenaPlayerEnterQueueEvent extends ArenaPlayerEvent{
	final ArenaTeam team;
	final JoinResult result;
	final TeamJoinObject tqo;

	public ArenaPlayerEnterQueueEvent(ArenaPlayer player, ArenaTeam team, TeamJoinObject tqo, JoinResult queueResult) {
		super(player);
		this.team = team;
		this.result = queueResult;
		this.tqo = tqo;
	}

	public ArenaTeam getTeam() {
		return team;
	}

	public JoinResult getQueueResult(){
		return result;
	}

	public Arena getArena(){
		return tqo.getJoinOptions().getArena();
	}
}
