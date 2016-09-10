package io.github.TcFoxy.ArenaTOW.BattleArena.events;


import io.github.TcFoxy.ArenaTOW.BattleArena.MyDefaults;

import org.bukkit.Bukkit;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;

public class MyBAEvent extends Event{
	private static final HandlerList handlers = new HandlerList();

	public void callEvent(){
		if (MyDefaults.TESTSERVER) return;
		Bukkit.getServer().getPluginManager().callEvent(this);
	}

	@Override
	public HandlerList getHandlers() {
		return handlers;
	}

	public static HandlerList getHandlerList() {
		return handlers;
	}
}
