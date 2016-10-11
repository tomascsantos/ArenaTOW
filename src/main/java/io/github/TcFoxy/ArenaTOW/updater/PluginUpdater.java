package io.github.TcFoxy.ArenaTOW.updater;

import java.io.File;
import java.util.HashSet;

import org.bukkit.Bukkit;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.plugin.Plugin;

/**
 * Originally a class that downloaded and updated bukkit plugins.  Since the new bukkit rules
 * this class has been converted into a wrapper around Gravity's Updater class
 */
public class PluginUpdater {

    public static String getNameAndVersion(final Plugin plugin) {
        return plugin.getDescription().getName() + "_v"+plugin.getDescription().getVersion();
    }

    /**
     NONE get no releases,
     RELEASE get only release updates, ignore beta/alpha,
     BETA get beta/release updates, but ignore alpha builds,
     ALL get all updates
     */
    public enum UpdateOption{
        NONE/** get no releases*/,
        RELEASE/** get only release updates, ignore beta/alpha*/,
        BETA/** get beta/release updates, but ignore alpha builds*/,
        ALL/** get all updates*/;

        public static UpdateOption fromString(String name){
            try {
                return valueOf(name.toUpperCase());
            } catch(Exception e) {
                if (name.equalsIgnoreCase("ALPHA")) return ALL;
                return null;
            }
        }
    }

    /**
     NONE don't show new versions
     CONSOLE show only to console log on startup
     OPS announce to ops on join, will only show this message once per server start
     */
    public enum AnnounceUpdateOption {
        NONE/** don't show new versions*/,
        CONSOLE/** show only to console log on startup*/,
        OPS/** announce to ops on join, will only show this message once per server start*/;
        public static AnnounceUpdateOption fromString(String name){
            try {
                return valueOf(name.toUpperCase());
            } catch(Exception e) {
                return null;
            }
        }
    }

    /**
     * Check for updates for a given plugin.
     * If there are updates then it will announce the newer version to the console. It will download the newer
     * jar if the "update" variable is true.
     * This happens in an asynchronous manner to not lag the server while checking for the update
     * @param plugin JavaPlugin
     * @param file File from the bukkit plugin, use this.getFile()
     * @param bukkitId the bukkit id of this plugin
     * @param updateOption when should we update the plugin
     * @param announceOption who should recieve announcements about a newer version
     */
    public static void update(final Plugin plugin, final int bukkitId, final File file,
                              final UpdateOption updateOption,
                              final AnnounceUpdateOption announceOption) {
        if (updateOption == null || announceOption == null ||
                updateOption == UpdateOption.NONE && announceOption == AnnounceUpdateOption.NONE)
            return;
        Thread t = new Thread(new Runnable() {
            @Override
            public void run() {
                UpdateOption update = updateOption;
                Updater up = new Updater(plugin, bukkitId, file, Updater.UpdateType.NO_DOWNLOAD, false);

                String failedMessage = null;
                switch (up.getResult()) {
                    case DISABLED:/* admin doesn't want any updates */
                        update = UpdateOption.NONE;
                        // drop down
                    case SUCCESS: /* good to go */
                    case UPDATE_AVAILABLE:
                    case FAIL_NOVERSION: /* couldn't find a version */
                    case NO_UPDATE: /* no newer version found, but his check is fairly naive, keep going*/
                        break;
                    case FAIL_DOWNLOAD: /* shouldn't happen yet */ break;

                    case FAIL_DBO: /* problem with accessing bukkit */
                        failedMessage = "Couldn't connect to bukkit website";
                        break;
                    case FAIL_BADID: /* bad id */
                        failedMessage = "The id provided was invalid or doesn't exist on DBO";
                        break;
                    case FAIL_APIKEY:
                        failedMessage = "You have set a bad API key in the configuration";
                        break;
                }
                if (failedMessage != null){
                    err("&4[" + getNameAndVersion(plugin) + "] &c"+failedMessage);
                    return;
                }
                Version curVersion = new Version(plugin.getDescription().getVersion());
                String name = up.getLatestName();
                String strv;
                String delim = "^v|[\\s_-]v";
                UpdateOption remoteReleaseType = up.getLatestType() != null ?
                        UpdateOption.fromString(up.getLatestType().name()) : null;

                if (remoteReleaseType == null || name == null || name.split(delim).length != 2) {
                    err("&4[" + getNameAndVersion(plugin) + "] &ccan't find a version for the plugin result was &f" +
                            up.getResult()+" &creleaseType: "+up.getLatestType());
                    return;
                } else {
                    strv = name.split(delim)[1];
                }

                Version remoteVersion = new Version(strv);
                if (curVersion.compareTo(remoteVersion) < 0) { /// We have found a newer version
                    /// Check to see if we want this release type
                    if (update.ordinal() >= remoteReleaseType.ordinal()) {
                        info("&2[" + getNameAndVersion(plugin) + "] &ebeginning download of newer "+
                                up.getLatestType().name()+"version &f" + remoteVersion);
                        up = new Updater(plugin, bukkitId, file, Updater.UpdateType.DEFAULT, false);
                        if (up.getResult() == Updater.UpdateResult.SUCCESS) {
                            info("&2[" + getNameAndVersion(plugin) + "] &edownloaded &f" +
                                    up.getLatestType().name() +" &eversion &f"+up.getLatestName().split(delim)[1]);
                        }
                    } else if (announceOption != AnnounceUpdateOption.NONE) {
                        String[] announce = new String[]{colorChat("&2[" + getNameAndVersion(plugin) + "] &ehas a newer &f" +
                                up.getLatestType().name() + " &eversion &f" + remoteVersion),
                                colorChat("&2[" + getNameAndVersion(plugin) + "]&5 " + up.getLatestFileLink()) };
                        for (String msg :announce){
                            info(msg);}

                        if (announceOption == AnnounceUpdateOption.OPS) {
                            new AnnounceOpListener(announce, plugin);
                        }
                    }
                }
            }
        });
        t.start();
    }

    static class AnnounceOpListener implements Listener {
        final String[] announce;
        HashSet<String> alreadyAnnounced = new HashSet<String>();
        AnnounceOpListener(String[] announce, Plugin plugin) {
            this.announce = announce;
            Bukkit.getPluginManager().registerEvents(this, plugin);
        }
        public void onPlayerJoinEvent(PlayerJoinEvent event){
            if (event.getPlayer().isOp() && !alreadyAnnounced.contains(event.getPlayer().getName())) {
                for (String s: announce)
                    event.getPlayer().sendMessage(s);
            }
        }
    }

    public static String colorChat(String msg) {return msg.replace('&', (char) 167);}

    public static void info(String msg){
        try {
            Bukkit.getConsoleSender().sendMessage(colorChat(msg));
        } catch(Exception e) {
            System.out.println(colorChat(msg));
        }
    }
    public static void warn(String msg){
        try {
            Bukkit.getConsoleSender().sendMessage(colorChat(msg));
        } catch(Exception e) {
            System.out.println(colorChat(msg));
        }
    }
    public static void err(String msg){
        try {
            Bukkit.getConsoleSender().sendMessage(colorChat(msg));
        } catch(Exception e) {
            System.err.println(colorChat(msg));
        }
    }

}
