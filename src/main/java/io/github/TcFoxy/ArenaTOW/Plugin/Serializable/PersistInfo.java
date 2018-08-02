package io.github.TcFoxy.ArenaTOW.Plugin.Serializable;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map.Entry;
import java.util.UUID;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Color;
import org.bukkit.Location;
import org.bukkit.World;

import io.github.TcFoxy.ArenaTOW.nms.v1_13_R1.NMSConstants;
import net.minecraft.server.v1_13_R1.Entity;

public class PersistInfo {

	private String key;
	private Color teamColor;
	private Location loc;
	private Entity mob;
	private String info;
	
	public static enum BaseType{
		TOWER, NEXUS, SPAWNER, DEATHROOM, PATHP;
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
	
	public static String getSimpleKey(String key){
		String[] rawparts = key.split("_");
		String newKey = ChatColor.GOLD + rawparts[0] + "_" + rawparts[1] 
				+ "_" +  rawparts[2] + "_" + getTeamColorStringReadable(key);
		if(rawparts[2].equals(BaseType.NEXUS.toString()) || rawparts[2].equals(BaseType.DEATHROOM.toString())){
			return newKey;
		}else{
			newKey += ChatColor.GOLD + "_" + rawparts[4];
		}
		return newKey;
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
	
	public static String getTeamColorStringReadable(String key){
		String[] rawparts = key.split("_");
		if(rawparts[3].equals(String.valueOf(Color.RED.asRGB()))){
			return "Red";
		}else{
			return "Blue";
		}
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
	
	public static ArrayList<String> getTypeObject(BaseType type, HashMap<String, PersistInfo> dic){

		ArrayList<String> towers = new ArrayList<String>();
		ArrayList<String> nexus = new ArrayList<String>();
		ArrayList<String> spawners = new ArrayList<String>();
		ArrayList<String> deathrooms = new ArrayList<String>();

		for(Entry <String, PersistInfo> entry : dic.entrySet()){
			if(entry.getValue() instanceof Nexus){
				nexus.add(entry.getKey());
			}else if(entry.getValue() instanceof Tower){
				towers.add(entry.getKey());
			}else if(entry.getValue() instanceof Spawner){
				spawners.add(entry.getKey());
			}else if(entry.getValue() instanceof Deathroom){
				deathrooms.add(entry.getKey());
			}
		}
		
		switch(type){
		case TOWER:
			return towers;
		case NEXUS:
			return nexus;
		case SPAWNER:
			return spawners;
		case DEATHROOM:
			return deathrooms;
		default:
			return null;
		}
	}
	
	public static Deathroom getDeathroom(String color, HashMap<String, PersistInfo> dic){
		for(PersistInfo base : dic.values()){
			if((base instanceof Deathroom) && PersistInfo.getTeamColorStringReadable(base.getKey()).equalsIgnoreCase(color)){
				return (Deathroom) base;
			}
		}
		return null;
	}

	public static String locationToString(Location loc){
		double xloc = loc.getX();
		double yloc = loc.getY();
		double zloc = loc.getZ();
		String wrld = loc.getWorld().getUID().toString();
		return (wrld + "*" +xloc + "*" + yloc + "*" + zloc);
	}
	
	public static Location stringToLocation(String str){
		String[] rawparts = str.split("\\*");
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
	
	public boolean hasMob(){
		return true;
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
	
	public static String turnToString(PersistInfo base){
		return (base.getKey() + ":" + base.getTeamColorString() + ":" + 
				locationToString(base.getSpawnLoc()) + ":" + base.getInfo());
	}
	
	public static PersistInfo getBaseType(PersistInfo b){
		String[] rawparts = b.key.split("_");
		String  type = rawparts[2];
		if(type.equals(BaseType.NEXUS.toString())){
			return new Nexus(b.key, b.teamColor, b.loc, b.info);
		}else if(type.equals(BaseType.TOWER.toString())){
			return new Tower(b.key, b.teamColor, b.loc, b.info);
		}else if(type.equals(BaseType.DEATHROOM.toString())){
			return new Deathroom(b.key, b.teamColor, b.loc, b.info);
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
}

