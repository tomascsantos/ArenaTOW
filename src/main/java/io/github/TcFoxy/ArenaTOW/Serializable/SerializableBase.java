package io.github.TcFoxy.ArenaTOW.Serializable;

import io.github.TcFoxy.ArenaTOW.nms.v1_10_R1.NMSConstants;

import java.util.HashMap;
import java.util.Map.Entry;
import java.util.UUID;

import net.minecraft.server.v1_10_R1.Entity;

import org.bukkit.Bukkit;
import org.bukkit.Color;
import org.bukkit.Location;
import org.bukkit.World;

public class SerializableBase {

	private String key;
	private Color teamColor;
	private Location loc;
	private Entity mob;
	
	public static enum BaseType{
		TOWER, NEXUS;
	}


	public SerializableBase(String key, Color teamColor, Location loc) {
		this.key = key;
		this.teamColor = teamColor;
		this.loc = loc;
	}		

	public String getKey() {
		return key;
	}

	public Color getTeamColor() {
		return teamColor;
	}

	public String getTeamColorString(){
		return String.valueOf(teamColor.asRGB());
	}

	public static Color getColorFromString(String str){
		int num = Integer.parseInt(str);
		return Color.fromRGB(num);
	}

	public Location getLoc(){
		return loc;	
	}

	public String locationToString(){
		this.getLoc();
		double xloc = loc.getX();
		double yloc = loc.getY();
		double zloc = loc.getZ();
		String wrld = loc.getWorld().getUID().toString();
		return (wrld + ":" +xloc + ":" + yloc + ":" + zloc);
	}
	
	public Entity getMob(){
		return this.mob;
	}
	
	public void setMob(Entity ent){
		this.mob = ent;
	}
	
	public String getMobString(){
		return this.getMob().getClass().getName();
	}
	
	public String getMobFromString(String str, World wol){
		NMSConstants.getMobFromString(str, wol);
		return null;
		
	}
	
	public Entity spawnMob(){
		return null;
	}
	
	public static String turnToString(SerializableBase base){
		if(base == null){
			Bukkit.broadcastMessage("base is null");
		}
		return (base.getKey() + ":" + base.getTeamColorString() + ":" + base.locationToString());
	}
	
	public static SerializableBase getBaseType(SerializableBase b){
		String[] rawparts = b.key.split("_");
		String  type = rawparts[2];
		if(type == BaseType.NEXUS.toString()){
			return new Nexus(b.key, b.teamColor, b.loc);
		}else if(type == BaseType.TOWER.toString()){
			return new Tower2(b.key, b.teamColor, b.loc);
		}else{
			return null;
		}
	}

	public static SerializableBase getFromString(String str){
		String[] rawparts = str.split(":");
		String key = rawparts[0];
		Color col = getColorFromString(rawparts[1]);
		
		World wol = Bukkit.getWorld(UUID.fromString(rawparts[2]));
		Location loc = new Location(wol,
				Double.parseDouble(rawparts[3]),
				Double.parseDouble(rawparts[4]),
				Double.parseDouble(rawparts[5])); 
		
		//Entity mob = NMSConstants.getMobFromString(rawparts[6], wol);
		
		
		return new SerializableBase(key, col, loc);
	}
	
	public static HashMap<String, String> saveObject(HashMap<String, SerializableBase> dictionary){
		if(dictionary == null){
			return null;
		}
		
		HashMap<String, String> newDic = new HashMap<String, String>();
		for(Entry<String, SerializableBase> entry : dictionary.entrySet()){
			if(entry.getValue() == null){
				Bukkit.broadcastMessage("entry value is null SerializableBase");
			}
			SerializableBase base = entry.getValue();
			String strValue = turnToString(base);
			newDic.put(entry.getKey(), strValue);
		}
		return newDic;
	}
	
	public static HashMap<String, SerializableBase> getObject(HashMap<String, String> dictionary){
		if(dictionary == null) return null;
		
		HashMap<String, SerializableBase> newDic = new HashMap<String, SerializableBase>();
		for(Entry<String, String> entry : dictionary.entrySet()){
			SerializableBase value = getFromString(entry.getValue());
			SerializableBase baseWithType = getBaseType(value);
			newDic.put(entry.getKey(), baseWithType);
		}
		return newDic;
	}
	


//	public HashMap<String, String> createSaveableTowers(HashMap<String, Tower> towerteams) {
//		HashMap<String, String> towers = new HashMap<String, String>();
//		if (towerteams != null) {
//			for (Entry<String, Tower> entry : towerteams.entrySet()) {
//				Tower t = entry.getValue();
//				String key = entry.getKey();
//				//String elname = t.getName();
//				String teamColor = t.getTeamColor().toString();
//				String towertype = t.getStringType();
//				UUID worldUUID = t.getWorld().getUID();
//				String uuidString = worldUUID.toString();
//				Location loc = t.getLoc();
//				double xloc = loc.getX();
//				double yloc = loc.getY();
//				double zloc = loc.getZ();
//				String stringloc = xloc + ":" + yloc + ":" + zloc;
//				towers.put(key, teamColor + ":" + uuidString + ":" + stringloc + ":" + towertype);
//			}
//
//		}
//		return towers;
//	}
//
//	public static HashMap<String, Tower> loadTowers(HashMap<String, String> towerSave){
//		HashMap<String, Tower> towerteams = new HashMap<String, Tower>();
//		for(Entry<String, String> entry: towerSave.entrySet()){
//			String key = entry.getKey();
//			String rawvalue = entry.getValue();
//			String[] rawparts = rawvalue.split(":");
//
//			Color teamColor = getTeamStringColor(rawparts[0]);
//
//			UUID uuid = UUID.fromString(rawparts[1]);
//			World towWorld = Bukkit.getWorld(uuid);
//
//			Location loc = new Location(towWorld,
//					Double.parseDouble(rawparts[2]),
//					Double.parseDouble(rawparts[3]),
//					Double.parseDouble(rawparts[4])); 
//
//			TowerType towertype = getTowerType(rawparts[5]);
//
//			Tower t = new Tower(key, towWorld, teamColor, loc, towertype);
//			towerteams.put(key, t);
//		}
//		return towerteams;
//
//	}
}

