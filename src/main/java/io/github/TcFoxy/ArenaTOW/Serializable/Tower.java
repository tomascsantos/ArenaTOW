package io.github.TcFoxy.ArenaTOW.Serializable;

import java.util.HashMap;
import java.util.Map.Entry;
import java.util.UUID;

import net.minecraft.server.v1_10_R1.EntityCreature;

import org.bukkit.Bukkit;
import org.bukkit.Color;
import org.bukkit.Location;
import org.bukkit.World;

public class Tower {
	private Color teamColor;
	private String key;
	private World world;
	private Location loc;
	private EntityCreature mob;
	private TowerType type;
	
	public static enum TowerType {
		NEXUS, TOWER;
	}
	
	public Tower(String key, World world, Color teamColor, Location spot, TowerType type) {
		this.key = key;
		this.world = world;
		this.teamColor = teamColor;
		this.loc = loc;
		this.type = type;
	}

	public String getKey() {
		return key;
	}

	public Color getTeamColor() {
		return teamColor;
	}
	
	public String teamColorString(){
		return String.valueOf(teamColor.asRGB());
	}
	
	public static Color getTeamStringColor(String str){
		int num = Integer.parseInt(str);
		return Color.fromRGB(num);
	}

	public World getWorld() {
		return world;
	}
	
	public Location getLoc(){
		return loc;	
	}
	
	public TowerType getType(){
		return this.type;
	}
	
	public String getStringType(){
		return TowerType.TOWER.toString();
	}
	
	public static TowerType getTowerType(String str){
		if(str == "TOWER"){
			return TowerType.TOWER;
		}else if(str == "NEXUS"){
			return TowerType.NEXUS;
		}else{
			Bukkit.broadcastMessage("ERROR, not a valid towertype to pass Tower.java getTowerType()");
			return null;
		}
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
				//String elname = t.getName();
				String teamColor = t.getTeamColor().toString();
				String towertype = t.getStringType();
				UUID worldUUID = t.getWorld().getUID();
				String uuidString = worldUUID.toString();
				Location loc = t.getLoc();
				double xloc = loc.getX();
				double yloc = loc.getY();
				double zloc = loc.getZ();
				String stringloc = xloc + ":" + yloc + ":" + zloc;
				towers.put(key, teamColor + ":" + uuidString + ":" + stringloc + ":" + towertype);
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
			
			Color teamColor = getTeamStringColor(rawparts[0]);
						
			UUID uuid = UUID.fromString(rawparts[1]);
			World towWorld = Bukkit.getWorld(uuid);
			
			Location loc = new Location(towWorld,
						Double.parseDouble(rawparts[2]),
						Double.parseDouble(rawparts[3]),
						Double.parseDouble(rawparts[4])); 
			
			TowerType towertype = getTowerType(rawparts[5]);
			
			Tower t = new Tower(key, towWorld, teamColor, loc, towertype);
			towerteams.put(key, t);
		}
		return towerteams;
		
	}
}
