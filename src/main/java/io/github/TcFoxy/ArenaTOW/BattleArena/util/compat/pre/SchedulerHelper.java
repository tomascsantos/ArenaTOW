package io.github.TcFoxy.ArenaTOW.BattleArena.util.compat.pre;

import org.bukkit.Bukkit;
import org.bukkit.plugin.Plugin;

import io.github.TcFoxy.ArenaTOW.BattleArena.util.compat.ISchedulerHelper;

public class SchedulerHelper implements ISchedulerHelper {

    @SuppressWarnings("deprecation")
    @Override
    public int scheduleAsyncTask(Plugin plugin, Runnable task, long ticks) {
        return Bukkit.getScheduler().scheduleAsyncDelayedTask(plugin, task,ticks);
    }
}
