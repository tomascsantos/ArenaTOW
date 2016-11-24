package io.github.TcFoxy.ArenaTOW.BattleArena.util.compat.v1_7_R3;


import java.util.UUID;

import org.bukkit.OfflinePlayer;
import org.bukkit.entity.Player;
import org.bukkit.scoreboard.Scoreboard;

import io.github.TcFoxy.ArenaTOW.BattleArena.util.compat.IPlayerHelper;

/**
 * @author alkarin
 */
public class PlayerHelper implements IPlayerHelper {

    @Override
    public void setHealth(Player player, double health) {
        player.setHealth(health);
    }

    @Override
    public double getHealth(Player player) {
        return player.getHealth();
    }

    @Override
    public double getMaxHealth(Player player) {
        return player.getMaxHealth();
    }

    @Override
    public Object getScoreboard(Player player) {
        return player.getScoreboard();
    }

    @Override
    public void setScoreboard(Player player, Object scoreboard) {
        player.setScoreboard((Scoreboard) scoreboard);
    }

    @Override
    public UUID getID(OfflinePlayer player) {
        return player.getUniqueId();
    }

}
