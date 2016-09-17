package io.github.TcFoxy.ArenaTOW.BattleArena.objects;



import org.bukkit.Location;

import io.github.TcFoxy.ArenaTOW.BattleArena.listeners.PlayerHolder;
import io.github.TcFoxy.ArenaTOW.BattleArena.util.SerializerUtil;

public class ArenaLocation {
	final PlayerHolder ph;
	Location location;
	final LocationType type;
	
	public enum LocationType {
		NONE, HOME, ARENA, WAITROOM, LOBBY, COURTYARD, SPECTATE, VISITOR, ANY
	}


	public ArenaLocation(PlayerHolder ph, Location location, LocationType type){
		this.ph = ph;
		this.location = location;
		this.type = type;
	}
	public LocationType getType() {
		return this.type;
	}
	public Location getLocation(){
		return this.location;
	}
	public void setLocation(Location location){
		this.location = location;
	}
	public PlayerHolder getPlayerHolder(){
		return ph;
	}
	@Override
	public String toString(){
		return "[LocationType loc="+SerializerUtil.getLocString(location) +" type="+type+"]";
	}
}
