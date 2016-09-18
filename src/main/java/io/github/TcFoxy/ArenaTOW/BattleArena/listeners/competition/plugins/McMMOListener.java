package io.github.TcFoxy.ArenaTOW.BattleArena.listeners.competition.plugins;

import java.util.Collection;
import java.util.HashSet;

import org.bukkit.event.Cancellable;

//import com.gmail.nossr50.datatypes.skills.SkillType;
//import com.gmail.nossr50.events.skills.McMMOPlayerSkillEvent;

import io.github.TcFoxy.ArenaTOW.BattleArena.objects.arenas.ArenaListener;
import io.github.TcFoxy.ArenaTOW.BattleArena.objects.events.ArenaEventHandler;
import io.github.TcFoxy.ArenaTOW.BattleArena.util.Log;

public class McMMOListener implements ArenaListener {

	//static HashSet<SkillType> disabledSkills;

//	@ArenaEventHandler
//	public void skillDisabled(McMMOPlayerSkillEvent event){
//		if (disabledSkills ==null || event.getPlayer() == null || !(event instanceof Cancellable) ||
//                !disabledSkills.contains(event.getSkill())){
//			return;}
//        ((Cancellable)event).setCancelled(true);
//	}

//	public static void setDisabledSkills(Collection<String> disabledCommands) {
//		if (disabledSkills == null){
//            disabledSkills = new HashSet<SkillType>();}
//		for (String s: disabledCommands) {
//            SkillType st = SkillType.getSkill(s);
//            if (st == null){
//                Log.err("mcMMO skill " + s +" was not found");
//                continue;
//            }
//            disabledSkills.add(st);
//        }
//    }

//    public static boolean hasDisabledSkills() {
//        return disabledSkills != null && !disabledSkills.isEmpty();
//    }
}
