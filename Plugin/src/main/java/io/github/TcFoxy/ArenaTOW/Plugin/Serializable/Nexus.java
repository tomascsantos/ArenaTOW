package io.github.TcFoxy.ArenaTOW.Plugin.Serializable;

import io.github.TcFoxy.ArenaTOW.API.*;
import io.github.TcFoxy.ArenaTOW.Plugin.ArenaTOW;
import org.bukkit.Location;

import java.awt.*;

public class Nexus extends AbstractStructure {
		
	public Nexus(String key, Color teamColor, Location loc, String info) {
		super(key, teamColor, loc, info);
	}
	
	
	@Override
	public TOWEntity spawnMob(){
		Location spawn = getSpawnLoc();
		TOWEntityHandler handler = ArenaTOW.getEntityHandler();
		setMob(handler.spawnMob(MobType.NEXUS, getTeamColor(), spawn.getWorld(),
								spawn.getX(), spawn.getY(), spawn.getZ()));
		return getMob();
	}
	
	public Integer getHealth(){
		return getMob().getIntHealth();
	}
}
