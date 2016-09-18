package io.github.TcFoxy.ArenaTOW.BattleArena.util.compat.v1_6_R1;

import org.bukkit.Bukkit;
import org.bukkit.plugin.Plugin;

import io.github.TcFoxy.ArenaTOW.BattleArena.util.compat.ISchedulerHelper;

public class SchedulerHelper implements ISchedulerHelper {


    @Override
    public int scheduleAsyncTask(Plugin plugin, Runnable task, long ticks) {
        return Bukkit.getScheduler().runTaskLaterAsynchronously(plugin,task,ticks).getTaskId();
    }

}
