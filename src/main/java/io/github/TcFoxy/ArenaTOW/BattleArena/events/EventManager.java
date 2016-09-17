package io.github.TcFoxy.ArenaTOW.BattleArena.events;


import org.bukkit.Bukkit;
import org.bukkit.event.Listener;
import org.bukkit.plugin.Plugin;

import io.github.TcFoxy.ArenaTOW.BattleArena.Defaults;

public class EventManager {
    public static void registerEvents(Listener listener, Plugin plugin) {
        if (Defaults.TESTSERVER)
            return;
        Bukkit.getPluginManager().registerEvents(listener, plugin);
    }
}
