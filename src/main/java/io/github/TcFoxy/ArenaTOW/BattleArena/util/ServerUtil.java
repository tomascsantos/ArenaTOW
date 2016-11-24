package io.github.TcFoxy.ArenaTOW.BattleArena.util;

import java.util.Set;
import java.util.UUID;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

import io.github.TcFoxy.ArenaTOW.updater.Version;





public class ServerUtil {

    /**
     * The safe method to specify we want a player who is currently online
     * @param name Name of the player to find
     * @return Player or null
     */
    public static Player findOnlinePlayer(String name) {
        return findPlayer(name);
    }

    public static Player findPlayer(UUID id) {
        if (id == null)
            return null;
        try {
            Player player = Bukkit.getPlayer(id);
            if (player != null)
                return player;
//            if (Defaults.DEBUG_VIRTUAL) {
//                return VirtualPlayers.getPlayer(id);
//            }
        } catch (Throwable e){
            /* do nothing, craftbukkit version is not great enough */
            Log.err("Craftbukkit version does not have find player by UUID yet.");
            Log.printStackTrace(e);
        }
        return null;
    }

//    @Deprecated
//	public static Player findPlayerExact(String name) {
//		if (name == null)
//			return null;
//		Player player = Bukkit.getPlayerExact(name);
//		if (player != null)
//			return player;
//		if (Defaults.DEBUG_VIRTUAL){return VirtualPlayers.getPlayer(name);}
//		return null;
//	}

	public static Player findPlayer(String name) {
		UUID id = UUID.fromString(name);
		return Bukkit.getPlayer(id);
	}


	public static Player[] getOnlinePlayers() {
//		if (Defaults.DEBUG_VIRTUAL){
//			return VirtualPlayers.getOnlinePlayers();
//		} else {
//			return BukkitInterface.getOnlinePlayers().toArray(new Player[BukkitInterface.getOnlinePlayers().size()]);
//		}
		return Bukkit.getOnlinePlayers().toArray(new Player[Bukkit.getOnlinePlayers().size()]);
	}

	public static void findOnlinePlayers(Set<String> names, Set<Player> foundplayers, Set<String> unfoundplayers) {
		Player[] online = getOnlinePlayers();
		for (String name : names){
			Player lastPlayer = null;
			for (Player player : online) {
				String playerName = player.getName();
				if (playerName.equalsIgnoreCase(name)) {
					lastPlayer = player;
					break;
				}

				if (playerName.toLowerCase().indexOf(name.toLowerCase(),0) != -1) { /// many names match the one given
					if (lastPlayer != null) {
						lastPlayer = null;
						break;
					}
					lastPlayer = player;
				}
			}
			if (lastPlayer != null){
				foundplayers.add(lastPlayer);
			} else{
				unfoundplayers.add(name);
			}
		}
	}

	public static Version getBukkitVersion(){
		final String pkg = Bukkit.getServer().getClass().getPackage().getName();
		String version = pkg.substring(pkg.lastIndexOf('.') + 1);
		if (version.equalsIgnoreCase("craftbukkit")){
			return new Version("v1_4_5-");
		} else{
			return new Version(version);
		}
	}


}
