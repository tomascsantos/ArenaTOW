package io.github.TcFoxy.ArenaTOW.scoreboard.scoreboard.bukkit.compat;

import java.util.UUID;

import org.bukkit.OfflinePlayer;

public interface IPlayerHelper {

    UUID getID(OfflinePlayer player);

    UUID getID(String name);
}
