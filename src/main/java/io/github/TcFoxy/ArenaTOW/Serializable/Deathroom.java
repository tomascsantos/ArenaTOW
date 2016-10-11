package io.github.TcFoxy.ArenaTOW.Serializable;

import org.bukkit.Color;
import org.bukkit.Location;

public class Deathroom extends PersistInfo{

	public Deathroom(String key, Color teamColor, Location loc, String info) {
		super(key, teamColor, loc, info);
	}

	@Override
	public boolean hasMob(){
		return false;
	}
}
