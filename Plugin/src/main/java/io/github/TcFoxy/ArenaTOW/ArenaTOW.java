package io.github.TcFoxy.ArenaTOW.Plugin;

import mc.alk.arena.BattleArena;
import org.bukkit.Bukkit;
import org.bukkit.plugin.java.JavaPlugin;

import java.io.File;

public class ArenaTOW extends JavaPlugin{

    private static ArenaTOW pluginArenaTOW;
    private File saveDirectory;
	public static String nmsver;

	@Override
	public void onDisable(){
		//MyEntityType.unregisterEntities();
	}


	@Override
	public void onLoad(){
		nmsver = Bukkit.getServer().getClass().getPackage().getName();
		nmsver = nmsver.substring(nmsver.lastIndexOf(".") + 1);
		saveDirectory = new File(this.getDataFolder(), "entityNBT");
		saveDirectory.mkdirs();
	}

	@Override
	public void onEnable(){
		pluginArenaTOW = this;
		//Messages msgConfig = new Messages();
		//register with battlearena
		BattleArena.registerCompetition(this, "ArenaTow", "tow", TugArena.class, new TugExecutor());
		//MyEntityType.registerEntities();
	}

	
//	public WorldGuardPlugin getWorldGuard() {
//	    Plugin Wgplugin = getSelf().getServer().getPluginManager().getPlugin("WorldGuard");
//
//	    // WorldGuard may not be loaded
//	    if (Wgplugin == null || !(Wgplugin instanceof WorldGuardPlugin)) {
//	        return null; // Maybe you want throw an exception instead
//	    }
//
//	    return (WorldGuardPlugin) Wgplugin;
//	}
	
	public static ArenaTOW getSelf() {
		return pluginArenaTOW;
	}

	public File getSaveDir() {
		return this.saveDirectory;
	}



}
