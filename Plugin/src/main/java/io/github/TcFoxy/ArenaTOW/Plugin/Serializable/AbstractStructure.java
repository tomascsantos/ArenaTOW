package io.github.TcFoxy.ArenaTOW.Plugin.Serializable;

import java.awt.*;
import java.util.HashMap;
import java.util.Map.Entry;

import io.github.TcFoxy.ArenaTOW.API.TOWEntity;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.World;


public abstract class AbstractStructure implements Structure{

	private String key;
	private Color teamColor;
	private Location loc;
	private TOWEntity mob;
	private String info;
	
	public enum BaseType{
	}


	public AbstractStructure(String key, Color teamColor, Location loc, String info) {
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
	
	public static Integer getObjectId(AbstractStructure info){
		if(!(info instanceof Nexus)){
			String[] rawparts = info.getKey().split("_");
			return Integer.parseInt(rawparts[4]);
		}else{
			return null;
		}
	}

	


	
	public TOWEntity getMob(){
		return this.mob;
	}

	public boolean hasMob(){
		return true;
	}
	
	public void setMob(TOWEntity ent){
		this.mob = ent;
	}
	
	public String getMobString(){
		return this.getMob().getClass().getName();
	}
	
	public String getMobFromString(String str, World wol){
		NMSConstants.getMobFromString(str, wol);
		return null;
		
	}
	
	public abstract TOWEntity spawnMob();

	public static String turnToString(AbstractStructure base){
		return (base.getKey() + ":" + base.getTeamColorString() + ":" + 
				locationToString(base.getSpawnLoc()) + ":" + base.getInfo());
	}
	
	public static AbstractStructure getBaseType(AbstractStructure b){
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

	public static AbstractStructure getFromString(String str){
		String[] rawparts = str.split(":");
		String key = rawparts[0];
		Color col = getColorFromString(rawparts[1]);
		Location loc = stringToLocation(rawparts[2]);
		String info = rawparts[3];
		return new AbstractStructure(key, col, loc, info);
	}
	
	public static HashMap<String, String> saveObject(HashMap<String, AbstractStructure> dictionary){
		if(dictionary == null){
			return null;
		}
		HashMap<String, String> newDic = new HashMap<String, String>();
		for(Entry<String, AbstractStructure> entry : dictionary.entrySet()){
			AbstractStructure base = entry.getValue();
			String strValue = turnToString(base);
			newDic.put(entry.getKey(), strValue);
		}
		return newDic;
	}
	
	public static HashMap<String, AbstractStructure> getObject(HashMap<String, String> dictionary){
		if(dictionary == null) return null;
		HashMap<String, AbstractStructure> newDic = new HashMap<String, AbstractStructure>();
		for(Entry<String, String> entry : dictionary.entrySet()){
			AbstractStructure value = getFromString(entry.getValue());
			AbstractStructure baseWithType = getBaseType(value);
			newDic.put(entry.getKey(), baseWithType);
		}
		return newDic;
	}
}

