package io.github.TcFoxy.ArenaTOW.Plugin.Serializable;

import io.github.TcFoxy.ArenaTOW.API.MobType;
import io.github.TcFoxy.ArenaTOW.API.TOWEntity;
import io.github.TcFoxy.ArenaTOW.Plugin.ArenaTOW;
import io.github.TcFoxy.ArenaTOW.Plugin.TugArena;
import org.bukkit.*;

public class Tower extends PersistInfo {

    public Tower(String key, Color teamColor, Location loc, String info) {
        super(key, teamColor, loc, info);
    }

    @Override
    public TOWEntity spawnMob(TugArena arena) {
        Location spawn = getSpawnLoc();
        setMob(arena.getEntityHandler().spawnMob(arena.getEntityHandler(), MobType.TOWER, getTeamColor(),
                spawn.getWorld(), spawn.getX(), spawn.getY(), spawn.getZ()));
        return getMob();
    }

}
