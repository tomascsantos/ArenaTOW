package io.github.TcFoxy.ArenaTOW.BattleArena.executors;

import io.github.TcFoxy.ArenaTOW.BattleArena.controllers.BattleArenaController;
import io.github.TcFoxy.ArenaTOW.BattleArena.controllers.ParamController;
import io.github.TcFoxy.ArenaTOW.BattleArena.controllers.RoomController;
import io.github.TcFoxy.ArenaTOW.BattleArena.controllers.containers.LobbyContainer;
import io.github.TcFoxy.ArenaTOW.BattleArena.objects.ArenaPlayer;
import io.github.TcFoxy.ArenaTOW.BattleArena.objects.MatchParams;
import io.github.TcFoxy.ArenaTOW.BattleArena.objects.arenas.Arena;
import io.github.TcFoxy.ArenaTOW.BattleArena.util.PermissionsUtil;

public class VoteExecutor extends CustomCommandExecutor{
	BattleArenaController bac;
	public VoteExecutor(BattleArenaController bac){
		this.bac = bac;
	}

	@MCCommand
	public boolean voteForArena(ArenaPlayer ap, Arena arena){
		LobbyContainer pc = RoomController.getLobby(arena.getArenaType());
		if (pc == null){
			return sendMessage(ap, "&cThere is no lobby for "+arena.getArenaType());}
		if (!pc.isHandled(ap)){
			return sendMessage(ap, "&cYou aren't inside the lobby for "+arena.getArenaType());}
		MatchParams mp = ParamController.getMatchParamCopy(arena.getArenaType());
		if (!PermissionsUtil.hasMatchPerm(ap.getPlayer(), mp,"add")){
			return sendMessage(ap, "&cYou don't have permission to vote in a &6" + mp.getCommand());}
		pc.castVote(ap,mp,arena);
		return true;
	}
}
