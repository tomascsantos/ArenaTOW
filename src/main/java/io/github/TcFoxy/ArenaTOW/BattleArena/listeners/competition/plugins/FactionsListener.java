package io.github.TcFoxy.ArenaTOW.BattleArena.listeners.competition.plugins;


import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;

//import com.massivecraft.factions.event.FactionsEventPowerChange;

import io.github.TcFoxy.ArenaTOW.BattleArena.listeners.competition.InArenaListener;
import io.github.TcFoxy.ArenaTOW.BattleArena.util.PlayerUtil;

public enum FactionsListener implements Listener{
	INSTANCE;

//	@EventHandler(priority = EventPriority.HIGHEST)
//	public void onFactionLoss(FactionsEventPowerChange event){
//        Player p = event.getUPlayer().getPlayer(); /// Annoyingly this has been null at times
//		if (p != null && InArenaListener.inArena(PlayerUtil.getID(p))){
//			event.setCancelled(true);}
//	}

//	public static boolean enable() {
//		try {
//			Class.forName("com.massivecraft.factions.event.FactionsEventPowerChange");
//			InArenaListener.addListener(INSTANCE);
//			return true;
//		} catch (ClassNotFoundException e) {
//			return false;
//		}
//	}
}
