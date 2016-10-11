package io.github.TcFoxy.ArenaTOW.scoreboard;


import org.bukkit.plugin.Plugin;
import org.bukkit.plugin.java.JavaPlugin;

import io.github.TcFoxy.ArenaTOW.scoreboard.scoreboard.api.SAPIFactory;
import io.github.TcFoxy.ArenaTOW.scoreboard.scoreboard.api.SScoreboard;

public class ScoreboardAPI extends JavaPlugin{

    static ScoreboardAPI plugin;

	@Override
	public void onEnable() {
        plugin = this;
        this.getCommand("scoreboardapi").setExecutor(new ScoreboardAPIExecutor());
    }

	@Override
	public void onDisable() {
        /* do nothing */
	}

    public static SScoreboard createScoreboard(Plugin plugin, String name) {
        return SAPIFactory.createScoreboard(plugin, name);
    }

    public static SScoreboard createSAPIScoreboard(Plugin plugin, String name) {
        return SAPIFactory.createSAPIScoreboard(plugin, name);
    }

    public static ScoreboardAPI getSelf() {
        return plugin;
    }
}
