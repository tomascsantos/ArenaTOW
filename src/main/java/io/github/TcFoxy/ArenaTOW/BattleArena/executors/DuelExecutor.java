package io.github.TcFoxy.ArenaTOW.BattleArena.executors;



import org.bukkit.command.CommandSender;

import io.github.TcFoxy.ArenaTOW.BattleArena.objects.ArenaPlayer;
import io.github.TcFoxy.ArenaTOW.BattleArena.objects.MatchParams;



public class DuelExecutor extends BAExecutor{
	@Override
	@MCCommand(cmds = {}, min=1, helpOrder = 10)
	public boolean duel(ArenaPlayer player, MatchParams mp, String args[]) {
		String newargs[] = new String[args.length+1];
		for (int i=0;i<args.length;i++){
			if (i==0){
				newargs[i] = "duel";}
			newargs[i+1] = args[i];
		}

		return super.duel(player, mp, newargs);
	}

	@Override
	public boolean join(ArenaPlayer player, MatchParams mp, String args[]) {
		return sendMessage(player, "&cYou can only duel with this type");
	}

	@Override
	public boolean arenaForceStart(CommandSender sender, MatchParams mp) {
		return sendMessage(sender, "&cThis command doesn't work for duel only arenas");
	}
}
