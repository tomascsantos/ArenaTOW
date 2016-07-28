package io.github.TcFoxy.ArenaTOW.Serializable;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.LinkedList;

import net.minecraft.server.v1_10_R1.Entity;

import org.bukkit.Bukkit;
import org.bukkit.Color;
import org.bukkit.Location;

public class Spawner extends PersistInfo{

	private LinkedList<Location> paths = new LinkedList<Location>();
	private ArrayList<Entity> zombies;

	
	public Spawner(String key, Color teamColor, Location loc, String info) {
		super(key, teamColor, loc, info);
		getSpawnerInfo();
		zombies = new ArrayList<Entity>();
	}


	public void addPp(Location loc) {
		paths.add(loc);
	}
	
	public boolean containsPp(Location loc){
		if(paths.contains(loc)){
			return true;
		}else{
			return false;
		}
	}
	
	public Location nextPathLoc() {
		return paths.remove();
	}
	
	public void clearPath() {
		paths.clear();
	}
	
	public Location peekPathLoc() {
		return paths.peek();
	}
	
	public Integer getPathSize() {
		return paths.size();
	}
	
	public void addMob(Entity ent){
		zombies.add(ent);
	}
	
	public void removeMob(Entity ent){
		zombies.remove(ent);
	}
	
	public ArrayList<Entity> getZombies(){
		return zombies;
	}
	
	public Location getcurrentLocation(Entity e){
		if(e.isAlive()){
			return e.getBukkitEntity().getLocation();
		}else{
			return null;
		}
	}
	
	public void printLocations() {
		String buf = "";
		for (Location l: paths) {
			buf += l.toString() + "\n";
		}
		Bukkit.broadcastMessage("Path locations of Spawner " + getKey() + ":\n" + buf);
	}
	
	private void getSpawnerInfo() {
		String[] rawparts = getInfo().split(";");
		for (int i = 0; i < rawparts.length; i++) {
			paths.add(stringToLocation(rawparts[i]));
		}
	}
	
	public void saveSpawnerInfo(){
		String buf = "";
		if(paths != null){
			Iterator<Location> iloc = paths.iterator();
			while (iloc.hasNext()) {
				buf += locationToString(iloc.next());
				if(iloc.hasNext()){
					buf += ";";
				}
			}
		}
		setInfo(buf);
	}

}
