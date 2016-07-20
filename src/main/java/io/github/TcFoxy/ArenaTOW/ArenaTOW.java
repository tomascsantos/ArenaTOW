package io.github.TcFoxy.ArenaTOW;


import io.github.TcFoxy.ArenaTOW.nms.v1_8ish.CustomEntityType;
import mc.alk.arena.BattleArena;

import org.bukkit.plugin.Plugin;
import org.bukkit.plugin.java.JavaPlugin;

import com.sk89q.worldguard.bukkit.WorldGuardPlugin;

public class ArenaTOW extends JavaPlugin{
	static ArenaTOW pluginArenaTOW;
	
	//public TugArena tug;
	
	@Override
	public void onDisable(){
		CustomEntityType.unregisterEntities();
	}
	
	@Override
	public void onEnable(){
		pluginArenaTOW = this;
		//register with battlearena
		BattleArena.registerCompetition(this, "ArenaTow", "tow", TugArena.class, new TugExecutor());    
		TugArena.setMain(this);
		CustomEntityType.registerEntities();
	}
	
	public WorldGuardPlugin getWorldGuard() {
	    Plugin plugin = getServer().getPluginManager().getPlugin("WorldGuard");
	 
	    // WorldGuard may not be loaded
	    if (plugin == null || !(plugin instanceof WorldGuardPlugin)) {
	        return null; // Maybe you want throw an exception instead
	    }
	 
	    return (WorldGuardPlugin) plugin;
	}
	
	public static ArenaTOW getSelf() {
		return pluginArenaTOW;
	}
}
