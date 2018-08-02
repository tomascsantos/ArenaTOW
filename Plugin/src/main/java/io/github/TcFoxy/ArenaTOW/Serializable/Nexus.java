package io.github.TcFoxy.ArenaTOW.Plugin.Serializable;

import org.bukkit.Color;
import org.bukkit.Location;

import io.github.TcFoxy.ArenaTOW.nms.v1_13_R1.NMSUtils;
import net.minecraft.server.v1_13_R1.Entity;
import net.minecraft.server.v1_13_R1.EntityLiving;

public class Nexus extends PersistInfo{
		
	public Nexus(String key, Color teamColor, Location loc, String info) {
		super(key, teamColor, loc, info);
	}
	
	
	@Override
	public Entity spawnMob(){
		setMob(NMSUtils.spawnTeamGuardian(getSpawnLoc().getWorld(), getSpawnLoc().getX(), getSpawnLoc().getY(), getSpawnLoc().getZ(), getTeamColor()));
		return getMob();
	}
	
	public Integer getHealth(){
		return Math.round(((EntityLiving) getMob()).getHealth());
	}
	
	public Integer getMaxHealth(){
		return Math.round(((EntityLiving) getMob()).getMaxHealth());
	}
}
