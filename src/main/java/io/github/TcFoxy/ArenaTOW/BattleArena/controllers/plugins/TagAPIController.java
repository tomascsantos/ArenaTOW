package io.github.TcFoxy.ArenaTOW.BattleArena.controllers.plugins;

import io.github.TcFoxy.ArenaTOW.BattleArena.listeners.competition.plugins.TagAPIListener;
import io.github.TcFoxy.ArenaTOW.BattleArena.objects.arenas.ArenaListener;

public class TagAPIController {
	static boolean hasTagAPI = false;

	public static boolean enabled() {
		return hasTagAPI;
	}

	public static void setEnable(boolean enable) {
		hasTagAPI = enable;
		TagAPIListener.enable();
	}

	public static ArenaListener getNewListener() {
		return TagAPIListener.INSTANCE;
	}
}
