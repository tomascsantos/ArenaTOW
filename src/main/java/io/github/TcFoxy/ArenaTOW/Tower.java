package io.github.TcFoxy.ArenaTOW;

import java.util.HashMap;
import java.util.Map.Entry;
import java.util.UUID;

import net.minecraft.server.v1_10_R1.EntityCreature;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.World;

import com.sk89q.worldguard.protection.regions.ProtectedCuboidRegion;

public class Tower {
	private String towerteam = "notsetted";
	private String name = "notset";
	private World world = null;
	ProtectedCuboidRegion region = null;
	private Location spot;
	//private CustomEntityIronGolem golem = null;
	//private CustomEntityWither wither = null;
	private EntityCreature mob = null;
	private String type = null;
	
	public Tower(String name, World world, String towerteam, Location spot, String type) {
		this.name = name;
		this.world = world;
		this.towerteam = towerteam;
		this.spot = spot;
		this.type = type;
	}

	public String getName() {
		return name;
	}

	public String getTeam() {
		return towerteam;
	}

	public World getWorld() {
		return world;
	}
	
	public Location getLoc(){
		return spot;	
	}
	
	public String getType(){
		return type;
	}
	
	public void setMob(EntityCreature themob){
		mob = themob;
	}
	
	public EntityCreature getMob(){
		return mob;
	}
	
	public Integer getHealth(){
		return Math.round(getMob().getHealth());
	}
	
	public Integer getMaxHealth(){
		return Math.round(getMob().getMaxHealth());
	}
	

	public HashMap<String, String> createSaveableTowers(HashMap<String, Tower> towerteams) {
		HashMap<String, String> towers = new HashMap<String, String>();
		if (towerteams != null) {
			for (Entry<String, Tower> entry : towerteams.entrySet()) {
				Tower t = entry.getValue();
				String key = entry.getKey();
				String elname = t.getName();
				String eltowerteam = t.getTeam();
				String towertype = t.getType();
				UUID worldUUID = t.getWorld().getUID();
				String uuidString = worldUUID.toString();
				Location loc = t.getLoc();
				double xloc = loc.getX();
				double yloc = loc.getY();
				double zloc = loc.getZ();
				String stringloc = xloc + ":" + yloc + ":" + zloc;
				towers.put(key, elname + ":" + eltowerteam + ":" + uuidString + ":" + stringloc + ":" + towertype);
			}

		}
		return towers;
	}
	
	public static HashMap<String, Tower> loadTowers(HashMap<String, String> towerSave){
		HashMap<String, Tower> towerteams = new HashMap<String, Tower>();
		for(Entry<String, String> entry: towerSave.entrySet()){
			String key = entry.getKey();
			String rawvalue = entry.getValue();
			String[] rawparts = rawvalue.split(":");
			String thisname = rawparts[0];
			String thistowerteam = rawparts[1];
			String uuidstring = rawparts[2];
			
			UUID uuid = UUID.fromString(uuidstring);
			World thisworld = Bukkit.getWorld(uuid);
			
			// Your string is already split here
			Location loc = new Location(thisworld,
						Double.parseDouble(rawparts[3]),
						Double.parseDouble(rawparts[4]),
						Double.parseDouble(rawparts[5])); 
			
			String towertype = rawparts[6];
			
			Tower t = new Tower(thisname, thisworld, thistowerteam, loc, towertype);
			towerteams.put(key, t);
		}
		return towerteams;
		
	}
}
