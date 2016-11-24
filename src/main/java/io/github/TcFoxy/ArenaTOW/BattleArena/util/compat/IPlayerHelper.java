package io.github.TcFoxy.ArenaTOW.BattleArena.util.compat;

import java.util.UUID;

import org.bukkit.OfflinePlayer;
import org.bukkit.entity.Player;

public interface IPlayerHelper {

	double getHealth(Player player);

	double getMaxHealth(Player player);

    Object getScoreboard(Player player);

    void setScoreboard(Player player, Object scoreboard);

    UUID getID(OfflinePlayer player);

	void setHealth(Player player, double health);
}
