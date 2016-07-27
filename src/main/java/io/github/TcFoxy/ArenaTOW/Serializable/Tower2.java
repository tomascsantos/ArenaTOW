package io.github.TcFoxy.ArenaTOW.Serializable;

import io.github.TcFoxy.ArenaTOW.nms.v1_10_R1.interfaces.NMSUtils;
import net.minecraft.server.v1_10_R1.Entity;

import org.bukkit.Color;
import org.bukkit.Location;

public class Tower2 extends SerializableBase {

	public Tower2(String key, Color teamColor, Location loc) {
		super(key, teamColor, loc);
	}
	
	@Override
	public Entity spawnMob(){
		setMob(NMSUtils.spawnTeamGolem(getLoc().getWorld(), getLoc().getX(), getLoc().getY(), getLoc().getZ(), getTeamColor()));
		return getMob();
	}
	
}
