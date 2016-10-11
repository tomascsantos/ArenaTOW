package io.github.TcFoxy.ArenaTOW.scoreboard.scoreboard.bukkit.compat.v1_7_R3;

import java.util.UUID;

import org.bukkit.OfflinePlayer;

import io.github.TcFoxy.ArenaTOW.scoreboard.scoreboard.bukkit.compat.IPlayerHelper;

/**
 * @author alkarin
 */
public class PlayerHelper implements IPlayerHelper {

    @Override
    public UUID getID(OfflinePlayer player) {
        return player.getUniqueId();
    }

    @Override
    public UUID getID(String name) {
        return new UUID(0, name.hashCode());
    }

}
