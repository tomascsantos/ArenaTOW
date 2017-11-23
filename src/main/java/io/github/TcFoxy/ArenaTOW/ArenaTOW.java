package io.github.TcFoxy.ArenaTOW;


import org.bukkit.plugin.Plugin;
import org.bukkit.plugin.java.JavaPlugin;

import com.sk89q.worldguard.bukkit.WorldGuardPlugin;

import io.github.TcFoxy.ArenaTOW.BattleArena.BattleArena;
import io.github.TcFoxy.ArenaTOW.nms.v1_12_R1.MyEntityType;

public class ArenaTOW{
	
	//public TugArena tug;
	
//	@Override
//	public void onDisable(){
//		MyEntityType.unregisterEntities();
//	}
	
//	@Override
//	public void onEnable(){
//		pluginArenaTOW = this;
//		//Messages msgConfig = new Messages();
//		//register with battlearena
//		BattleArena.registerCompetition(this, "ArenaTow", "tow", TugArena.class, new TugExecutor());    
//		MyEntityType.registerEntities();
//	}
//	

	public void startThePlugin(){   
		MyEntityType.registerEntities();
	}
	
	public void stopThePlugin(){
		//MyEntityType.unregisterEntities();

	}

	
	public WorldGuardPlugin getWorldGuard() {
	    Plugin Wgplugin = getSelf().getServer().getPluginManager().getPlugin("WorldGuard");
	 
	    // WorldGuard may not be loaded
	    if (Wgplugin == null || !(Wgplugin instanceof WorldGuardPlugin)) {
	        return null; // Maybe you want throw an exception instead
	    }
	 
	    return (WorldGuardPlugin) Wgplugin;
	}
	
	public static JavaPlugin getSelf() {
		return BattleArena.getSelf();
	}
}
