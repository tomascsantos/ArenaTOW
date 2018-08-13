package io.github.TcFoxy.ArenaTOW.Plugin;

import io.github.TcFoxy.ArenaTOW.API.NMSHandler;
import io.github.TcFoxy.ArenaTOW.API.TOWEntityHandler;
import mc.alk.arena.BattleArena;
import org.bukkit.Bukkit;
import org.bukkit.plugin.java.JavaPlugin;

import java.io.File;
import java.util.logging.Logger;

public class ArenaTOW extends JavaPlugin {

    private static ArenaTOW pluginArenaTOW;
    private static NMSHandler nmsHandler;
    private static Logger logger;
    private File saveDirectory;

    public static TOWEntityHandler getEntityHandler() {
        return nmsHandler.getEntityHandler();
    }

    public static ArenaTOW getSelf() {
        return pluginArenaTOW;
    }

    @Override
    public void onDisable() {
        unregisterEntities();
    }

    @Override
    public void onEnable() {

        saveDirectory = new File(this.getDataFolder(), "entityNBT");
        saveDirectory.mkdirs();

        //Get full name of server
        String packageName = Bukkit.getServer().getClass().getPackage().getName();
        //org.bukkit.craftbukkit.version
        String version = packageName.substring(packageName.lastIndexOf(".") + 1);
        try {
            final Class<?> clazz = Class.forName("io.github.TcFoxy.ArenaTOW." + version + "." + version + "_NMSHandler");

            if (NMSHandler.class.isAssignableFrom(clazz)) {
                this.nmsHandler = (NMSHandler) clazz.getConstructor(File.class).newInstance(saveDirectory);
            }
        } catch (final Exception e) {
            e.printStackTrace();
            this.getLogger().severe("Version " + version + " of NSM is not supported");
            this.setEnabled(false);
            return;
        }


        pluginArenaTOW = this;
        logger = getLogger();

        registerEntities();
        BattleArena.registerCompetition(this, "ArenaTow", "tow", TugArena.class, new TugExecutor());
    }

    private void registerEntities() {
        nmsHandler.getEntityRegistry().registerEntities();
        getServer().getPluginManager().registerEvents(nmsHandler.getListener(), this);
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

    private void unregisterEntities() {
        nmsHandler.getEntityRegistry().unregisterEntities();
    }

    public File getSaveDir() {
        return this.saveDirectory;
    }

    public static Logger log() {
        return logger;
    }

}
