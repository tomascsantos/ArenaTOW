package io.github.TcFoxy.ArenaTOW.Plugin.Serializable;

import io.github.TcFoxy.ArenaTOW.API.MobType;
import io.github.TcFoxy.ArenaTOW.API.TOWEntity;
import io.github.TcFoxy.ArenaTOW.Plugin.ArenaTOW;
import org.bukkit.Color;
import org.bukkit.Location;

public class Tower extends PersistInfo {

    public Tower(String key, Color teamColor, Location loc, String info) {
        super(key, teamColor, loc, info);
    }

    @Override
    public TOWEntity spawnMob() {
        Location spawn = getSpawnLoc();
        setMob(ArenaTOW.getEntityHandler().spawnMob(MobType.TOWER, getTeamColor(),
                spawn.getWorld(), spawn.getX(), spawn.getY(), spawn.getZ()));
        return getMob();
    }

}
