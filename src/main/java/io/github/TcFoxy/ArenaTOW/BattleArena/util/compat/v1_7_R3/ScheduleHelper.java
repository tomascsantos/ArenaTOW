package io.github.TcFoxy.ArenaTOW.BattleArena.util.compat.v1_7_R3;

import org.bukkit.Bukkit;
import org.bukkit.plugin.Plugin;

import io.github.TcFoxy.ArenaTOW.BattleArena.util.compat.ISchedulerHelper;

public class ScheduleHelper implements ISchedulerHelper{

	@Override
	public int scheduleAsyncTask(Plugin plugin, Runnable task, long ticks) {
		return Bukkit.getScheduler().runTaskLaterAsynchronously(plugin,task,ticks).getTaskId();
	}

}
