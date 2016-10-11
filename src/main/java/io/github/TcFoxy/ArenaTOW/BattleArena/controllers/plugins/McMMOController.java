package io.github.TcFoxy.ArenaTOW.BattleArena.controllers.plugins;



import io.github.TcFoxy.ArenaTOW.BattleArena.listeners.competition.plugins.McMMOListener;
import io.github.TcFoxy.ArenaTOW.BattleArena.objects.arenas.ArenaListener;


public class McMMOController {
	static boolean enabled = false;

	public static boolean enabled() {
		return enabled;
	}

	public static void setEnable(boolean enable) {
        enabled = enable;
	}

//    public static void setDisabledSkills(List<String> disabled) {
//        try{McMMOListener.setDisabledSkills(disabled);}catch(Exception e){Log.printStackTrace(e);}
//    }
//
//    public static boolean hasDisabledSkills() {
//        return McMMOListener.hasDisabledSkills();
//    }

    public static ArenaListener createNewListener() {
        return new McMMOListener();
    }
}
