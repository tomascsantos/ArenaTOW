package io.github.TcFoxy.ArenaTOW.Plugin;

import io.github.TcFoxy.ArenaTOW.API.NMSHandler;
import io.github.TcFoxy.ArenaTOW.API.TOWEntityHandler;
import mc.alk.arena.BattleArena;
import org.bukkit.Bukkit;
import org.bukkit.plugin.java.JavaPlugin;

import java.io.File;

public class ArenaTOW extends JavaPlugin{

    private static ArenaTOW pluginArenaTOW;
    private static NMSHandler nmsHandler;
    private File saveDirectory;

	@Override
	public void onDisable(){
		unregisterEntities();
	}

	@Override
	public void onEnable(){
        pluginArenaTOW = this;
        initializeNMS(); //creates nms Singleton and dir for nms.

        registerEntities(); //register custom entities
		BattleArena.registerCompetition(this, "ArenaTow", "tow", TugArena.class, new TugExecutor());
	}


	private void registerEntities() {
	    nmsHandler.getEntityRegistry().registerEntities();
	    getServer().getPluginManager().registerEvents(nmsHandler.getListener(), this);
    }

    private void unregisterEntities() {
	    nmsHandler.getEntityRegistry().unregisterEntities();
    }

    private void initializeNMS() {
        //Get full name of server
        String packageName = Bukkit.getServer().getClass().getPackage().getName();
        //org.bukkit.craftbukkit.version
        String version = packageName.substring(packageName.lastIndexOf(".") + 1);
        try {
            final Class<?> clazz = Class.forName("io.github.TcFoxy.ArenaTOW." + version + "NMSHandler");

            if (NMSHandler.class.isAssignableFrom(clazz)) {
                this.nmsHandler = (NMSHandler) clazz.getConstructor().newInstance();
            }
        } catch (final Exception e) {
            e.printStackTrace();
            this.getLogger().severe("Version" + version + "of NSM is not supported");
            this.setEnabled(false);
            return;
        }

        saveDirectory = new File(this.getDataFolder(), "entityNBT");
        saveDirectory.mkdirs();
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

    public static TOWEntityHandler getEntityHandler() {
	    return nmsHandler.getEntityHandler();
    }

	public static ArenaTOW getSelf() {
		return pluginArenaTOW;
	}

	public File getSaveDir() {
		return this.saveDirectory;
	}



}
