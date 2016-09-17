package io.github.TcFoxy.ArenaTOW.BattleArena.controllers;



import org.bukkit.Bukkit;

import io.github.TcFoxy.ArenaTOW.BattleArena.BattleArena;
import io.github.TcFoxy.ArenaTOW.BattleArena.objects.modules.ArenaModule;
import io.github.TcFoxy.ArenaTOW.BattleArena.util.CaseInsensitiveMap;


public enum ModuleController {
	INSTANCE;

	CaseInsensitiveMap<ArenaModule> modules = new CaseInsensitiveMap<ArenaModule>();

	public static void addModule(ArenaModule module){
		INSTANCE.modules.put(module.getName(), module);
		Bukkit.getPluginManager().registerEvents(module, BattleArena.getSelf());
	}

	public static ArenaModule getModule(String moduleName){
		return INSTANCE.modules.get(moduleName);
	}
}
