package io.github.TcFoxy.ArenaTOW.Serializable;

import io.github.TcFoxy.ArenaTOW.nms.v1_10_R1.interfaces.NMSUtils;
import net.minecraft.server.v1_10_R1.Entity;
import net.minecraft.server.v1_10_R1.EntityLiving;

import org.bukkit.Color;
import org.bukkit.Location;

public class Nexus extends SerializableBase{
		
	public Nexus(String key, Color teamColor, Location loc) {
		super(key, teamColor, loc);
	}
	
	
	@Override
	public Entity spawnMob(){
		setMob(NMSUtils.spawnTeamGuardian(getLoc().getWorld(), getLoc().getX(), getLoc().getY(), getLoc().getZ(), getTeamColor()));
		return getMob();
	}
	
	public Integer getHealth(){
		return Math.round(((EntityLiving) getMob()).getHealth());
	}
	
	public Integer getMaxHealth(){
		return Math.round(((EntityLiving) getMob()).getMaxHealth());
	}
}
