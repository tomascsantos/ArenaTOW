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

public class PersistInfo {

	private String key;
	private Color teamColor;
	private Location loc;
	private Entity mob;
	private String info;
	
	public static enum BaseType{
		TOWER, NEXUS, SPAWNER;
	}


	public PersistInfo(String key, Color teamColor, Location loc, String info) {
		this.key = key;
		this.teamColor = teamColor;
		this.loc = loc;
		this.info = info;
	}		

	public String getKey() {
		return key;
	}
	
	public String getInfo(){
		return this.info;
	}
	
	public void setInfo(String str){
		this.info = str;
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

	public Location getSpawnLoc(){
		return loc;	
	}
	
	public static Integer getObjectId(PersistInfo info){
		if(!(info instanceof Nexus)){
			String[] rawparts = info.getKey().split("_");
			return Integer.parseInt(rawparts[4]);
		}else{
			return null;
		}
	}

	public static String locationToString(Location loc){
		double xloc = loc.getX();
		double yloc = loc.getY();
		double zloc = loc.getZ();
		String wrld = loc.getWorld().getUID().toString();
		return (wrld + "*" +xloc + "*" + yloc + "*" + zloc);
	}
	
	public static Location stringToLocation(String str){
		String[] rawparts = str.split("*");
		World wol = Bukkit.getWorld(UUID.fromString(rawparts[0]));
		Location loc = new Location(wol,
				Double.parseDouble(rawparts[1]),
				Double.parseDouble(rawparts[2]),
				Double.parseDouble(rawparts[3]));
		return loc;
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
		Bukkit.broadcastMessage("spawnmob is called in SerializableBase");
		return null;
	}
	
	public static String turnToString(PersistInfo base){
		if(base == null){
			Bukkit.broadcastMessage("base is null");
		}
		return (base.getKey() + ":" + base.getTeamColorString() + ":" + locationToString(base.getSpawnLoc()) + ":" + base.getInfo());
	}
	
	public static PersistInfo getBaseType(PersistInfo b){
		String[] rawparts = b.key.split("_");
		String  type = rawparts[2];
		if(type.equals(BaseType.NEXUS.toString())){
			return new Nexus(b.key, b.teamColor, b.loc, b.info);
		}else if(type.equals(BaseType.TOWER.toString())){
			return new Tower(b.key, b.teamColor, b.loc, b.info);
		}else if(type.equals(BaseType.SPAWNER.toString())){
			return new Spawner(b.key, b.teamColor, b.loc, b.info);
		}else{
			return null;
		}
	}

	public static PersistInfo getFromString(String str){
		String[] rawparts = str.split(":");
		String key = rawparts[0];
		Color col = getColorFromString(rawparts[1]);
		Location loc = stringToLocation(rawparts[2]);
		String info = rawparts[3];
		return new PersistInfo(key, col, loc, info);
	}
	
	public static HashMap<String, String> saveObject(HashMap<String, PersistInfo> dictionary){
		if(dictionary == null){
			return null;
		}
		HashMap<String, String> newDic = new HashMap<String, String>();
		for(Entry<String, PersistInfo> entry : dictionary.entrySet()){
			PersistInfo base = entry.getValue();
			String strValue = turnToString(base);
			newDic.put(entry.getKey(), strValue);
		}
		return newDic;
	}
	
	public static HashMap<String, PersistInfo> getObject(HashMap<String, String> dictionary){
		if(dictionary == null) return null;
		HashMap<String, PersistInfo> newDic = new HashMap<String, PersistInfo>();
		for(Entry<String, String> entry : dictionary.entrySet()){
			PersistInfo value = getFromString(entry.getValue());
			PersistInfo baseWithType = getBaseType(value);
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

