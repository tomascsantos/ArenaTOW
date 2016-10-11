package io.github.TcFoxy.ArenaTOW.BattleArena.listeners.competition;


import org.bukkit.event.player.PlayerTeleportEvent;

import io.github.TcFoxy.ArenaTOW.BattleArena.Permissions;
import io.github.TcFoxy.ArenaTOW.BattleArena.listeners.PlayerHolder;
import io.github.TcFoxy.ArenaTOW.BattleArena.objects.arenas.ArenaListener;
import io.github.TcFoxy.ArenaTOW.BattleArena.objects.events.ArenaEventHandler;
import io.github.TcFoxy.ArenaTOW.BattleArena.objects.events.EventPriority;
import io.github.TcFoxy.ArenaTOW.BattleArena.objects.options.TransitionOption;
import io.github.TcFoxy.ArenaTOW.BattleArena.util.MessageUtil;

public class PlayerTeleportListener implements ArenaListener{
    final PlayerHolder holder;

	public PlayerTeleportListener(PlayerHolder holder){
		this.holder = holder;
	}

	@ArenaEventHandler(priority=EventPriority.HIGH)
	public void onPlayerTeleport(PlayerTeleportEvent event){
		if (event.isCancelled() || event.getPlayer().hasPermission(Permissions.TELEPORT_BYPASS_PERM))
			return;
		if (holder.hasOption(TransitionOption.NOTELEPORT)){
			MessageUtil.sendMessage(event.getPlayer(), "&cTeleports are disabled in this arena");
			event.setCancelled(true);
			return;
		}
		if (event.getFrom().getWorld().getUID() != event.getTo().getWorld().getUID() &&
				holder.hasOption(TransitionOption.NOWORLDCHANGE)){
			MessageUtil.sendMessage(event.getPlayer(), "&cWorldChanges are disabled in this arena");
			event.setCancelled(true);
		}
	}
}
