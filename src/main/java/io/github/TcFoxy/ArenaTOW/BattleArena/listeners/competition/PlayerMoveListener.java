package io.github.TcFoxy.ArenaTOW.BattleArena.listeners.competition;


import org.bukkit.Bukkit;
import org.bukkit.World;
import org.bukkit.event.player.PlayerMoveEvent;

import io.github.TcFoxy.ArenaTOW.BattleArena.controllers.plugins.WorldGuardController;
import io.github.TcFoxy.ArenaTOW.BattleArena.listeners.PlayerHolder;
import io.github.TcFoxy.ArenaTOW.BattleArena.objects.arenas.ArenaListener;
import io.github.TcFoxy.ArenaTOW.BattleArena.objects.events.ArenaEventHandler;
import io.github.TcFoxy.ArenaTOW.BattleArena.objects.events.EventPriority;
import io.github.TcFoxy.ArenaTOW.BattleArena.objects.options.TransitionOption;
import io.github.TcFoxy.ArenaTOW.BattleArena.objects.regions.WorldGuardRegion;


public class PlayerMoveListener implements ArenaListener{
	PlayerHolder holder;
    WorldGuardRegion region;
    final World w;
	public PlayerMoveListener(PlayerHolder holder, WorldGuardRegion region){
		this.holder = holder;
        this.region = region;
        this.w = Bukkit.getWorld(region.getWorldName());
    }

    @ArenaEventHandler(priority=EventPriority.HIGH)
    public void onPlayerMove(PlayerMoveEvent event){
        if (!event.isCancelled() && w.getUID() == event.getTo().getWorld().getUID() &&
                holder.hasOption(TransitionOption.WGNOLEAVE) &&
                WorldGuardController.hasWorldGuard()){
            /// Did we actually even move
            if (event.getFrom().getBlockX() != event.getTo().getBlockX()
                    || event.getFrom().getBlockY() != event.getTo().getBlockY()
                    || event.getFrom().getBlockZ() != event.getTo().getBlockZ()){
                if (WorldGuardController.isLeavingArea(event.getFrom(), event.getTo(),w,region.getID())){
                    event.setCancelled(true);}
            }
        }
    }
}
