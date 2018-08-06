package io.github.TcFoxy.ArenaTOW.Plugin.Serializable;

import io.github.TcFoxy.ArenaTOW.API.TOWEntity;
import org.bukkit.Location;

import java.awt.*;

public class Deathroom extends AbstractStructure implements Structure{

	public Deathroom(String key, Color teamColor, Location loc, String info) {
		super(key, teamColor, loc, info);
	}

	@Override
	public boolean hasMob(){
		return false;
	}

	@Override
	public TOWEntity spawnMob() {
		return null;
	}
}
