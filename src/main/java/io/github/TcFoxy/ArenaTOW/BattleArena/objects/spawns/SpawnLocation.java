package io.github.TcFoxy.ArenaTOW.BattleArena.objects.spawns;

import org.bukkit.Location;

import io.github.TcFoxy.ArenaTOW.BattleArena.objects.YamlSerializable;

/**
 * @author alkarin
 */
public interface SpawnLocation extends YamlSerializable {
    public Location getLocation();
    public Location getUpperCorner();
    public Location getLowerCorner();
}
