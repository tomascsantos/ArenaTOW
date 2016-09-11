package io.github.TcFoxy.ArenaTOW.BattleArena.listeners.competition;


import org.bukkit.entity.Player;
import org.bukkit.event.entity.PotionSplashEvent;

import io.github.TcFoxy.ArenaTOW.BattleArena.BattleArena;
import io.github.TcFoxy.ArenaTOW.BattleArena.listeners.PlayerHolder;
import io.github.TcFoxy.ArenaTOW.BattleArena.objects.ArenaPlayer;
import io.github.TcFoxy.ArenaTOW.BattleArena.objects.arenas.ArenaListener;
import io.github.TcFoxy.ArenaTOW.BattleArena.objects.events.ArenaEventHandler;
import io.github.TcFoxy.ArenaTOW.BattleArena.objects.options.TransitionOption;

public class PotionListener implements ArenaListener{
	PlayerHolder holder;

	public PotionListener(PlayerHolder holder){
		this.holder = holder;
	}

	@ArenaEventHandler(needsPlayer=false)
	public void onPotionSplash(PotionSplashEvent event) {
		if (!event.isCancelled())
			return;
		if (event.getEntity().getShooter() instanceof Player){
			Player p = (Player) event.getEntity().getShooter();
			ArenaPlayer ap = BattleArena.toArenaPlayer(p);
			if (holder.isHandled(ap) && holder.hasOption(TransitionOption.POTIONDAMAGEON)){
				event.setCancelled(false);
			}
		}
	}
}
