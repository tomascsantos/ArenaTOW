package io.github.TcFoxy.ArenaTOW.Serializable;

import io.github.TcFoxy.ArenaTOW.nms.v1_10_R1.MyEntityZombie;

import java.util.Iterator;
import java.util.LinkedList;
import java.util.UUID;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.World;

public class Minion {

	private String key;
	private String team;
	private Location start;
	//private CustomEntityZombie entity;
	private MyEntityZombie entity;
	//private HashMap<Integer, Location> pointLoc;
	private LinkedList<Location> paths = new LinkedList<Location>();

	public Minion(String name, String team, Location start) {
		// Eventually just add our entity here
		this.key = name;		
		this.team = team;
		this.start = start;
	}
	
	public Minion(Minion m) {
		key = m.key;
		team = m.team;
		start = m.start;
		entity = m.entity;
		// Needs a real deep copy
		for (Location l : m.paths) {
			paths.add(l);
		}
	}
	
	
	public Minion(String key, String data) {
		// Creates a minion based on serialized data
		this.key = key;
		String []locStrs = data.split(";");
		// This is bad if the minion does not have a start loc
		team = locStrs[0];
		String []startXYZ = locStrs[1].split(":");
		
		String uuidstring = startXYZ[0];
		UUID uuid = UUID.fromString(uuidstring);
		World world = Bukkit.getWorld(uuid);
		this.start = new Location(world, Double.parseDouble(startXYZ[1]),
				                  Double.parseDouble(startXYZ[2]), Double.parseDouble(startXYZ[3])); 
		
		for (int i = 2; i < locStrs.length; i++) {
			String []locXYZ = locStrs[i].split(":");
			uuidstring = locXYZ[0];
			uuid = UUID.fromString(uuidstring);
			world = Bukkit.getWorld(uuid);
			paths.add(new Location(world, Double.parseDouble(locXYZ[1]),
					                  Double.parseDouble(locXYZ[2]), Double.parseDouble(locXYZ[3]))); 
		}
	}
	
	public String getName() {
		return key;
	}

	public String getTeam() {
		return team;
	}
	
	public Location getStartLoc(){
		// GetLoc should *probably* return a currentloc
		return start;
	}


	public void addPathLoc(Location loc) {
		paths.add(loc);
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
	
	public void setMinionEntity(MyEntityZombie minion) {
		entity = minion;
	}
	
	public MyEntityZombie getMinion(){
		return entity;
	}
	
	public Location getcurrentLocation(){
		return entity.getBukkitEntity().getLocation();
	}
	
	public void printLocations() {
		String buf = "";
		for (Location l: paths) {
			buf += l.toString() + "\n";
		}
		Bukkit.broadcastMessage("Path locations of minion " + key + ":\n" + buf);
	}
	
	public Boolean findPathLocation(Location loc) {
		for (Location l: paths) {
			if ((l.getBlockX() == loc.getBlockX()) &&
				(l.getBlockY() == loc.getBlockY()) &&
				(l.getBlockZ() == loc.getBlockZ())) {
				return true;
			}
		}
		return false;
	}
	
	
	// Make method to dump String for serialization
	
	public String serializeMinion() {
		String buf = "";
		buf += team + ";";
		buf += locToString(start);
		if(paths != null){
			Iterator<Location> iloc = paths.iterator();
			while (iloc.hasNext()) {
				buf += locToString(iloc.next());
			}
		}
		return buf;
	}
	
	// This is just kind of a utility function
	public static String locToString(Location location){
		Location loc = location;
		UUID worldUUID = loc.getWorld().getUID();
		String uuidString = worldUUID.toString();
		double xloc = loc.getX();
		double yloc = loc.getY();
		double zloc = loc.getZ();
		String stringloc = uuidString + ":" + xloc + ":" + yloc + ":" + zloc + ";";
		return stringloc;
		
	}	
	/*
	public HashMap <String, String> createSavableMinions(HashMap<String, Minion> minionStuff){
		HashMap<String, String> minions = new HashMap<String, String>();
		if (minionStuff != null){
			for (Entry<String, Minion> entry: minionStuff.entrySet()){
				Minion m = entry.getValue();
				String key = entry.getKey();
				String elname = m.getName();
				String elteam = m.getTeam();
				String stringloc1 = m.locToString(m.getLoc());
				minions.put(key, elname + ":" + elteam + ":" + stringloc1);
			}
		}
		
		
		return minions;
		
	}
	*/

	/*
	public static HashMap<String, Minion> loadMinions(HashMap<String, String> minionSave) {
		HashMap<String, Minion> minionStuff = new HashMap<String, Minion>();
		for(Entry<String, String> entry: minionSave.entrySet()){
			String key = entry.getKey();
			String rawvalue = entry.getValue();
			String[] rawparts = rawvalue.split(":");
			String thisname = rawparts[0];
			String thisteam = rawparts[1];
			String uuidstring = rawparts[2];
			
			UUID uuid = UUID.fromString(uuidstring);
			World thisworld = Bukkit.getWorld(uuid);
			
			// Your string is already split here
			Location loc = new Location(thisworld,
						Double.parseDouble(rawparts[3]),
						Double.parseDouble(rawparts[4]),
						Double.parseDouble(rawparts[5])); 
			
			Minion m = new Minion(thisname, thisteam, loc);
			minionStuff.put(key, m);
		}
		return minionStuff;
	}
	*/
	


	
//	public void setPointLoc(Location loc, Integer key){
		// pointLoc.put(key, loc);
//	}
	
//	public void setAllPointLoc(HashMap<Integer, Location>hashmap){
		 //pointLoc = hashmap;
//	}
	
//	public Location getPointLoc(int i){
		 //return pointLoc.get(i);
	//}
	
	//public HashMap<Integer, Location> getPointAllLocs(){
		 //return pointLoc;
	//}

}

