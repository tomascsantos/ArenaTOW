package io.github.TcFoxy.ArenaTOW.BattleArena.controllers;

import org.bukkit.Server;
import org.bukkit.World;

public class BukkitInterface {
	static Server server;

	public static World getWorld(String name) {
		return server.getWorld(name);
	}

	public static void setServer(Server server){
		BukkitInterface.server = server;
	}

}
