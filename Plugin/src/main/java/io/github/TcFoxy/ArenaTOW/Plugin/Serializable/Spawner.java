package io.github.TcFoxy.ArenaTOW.Plugin.Serializable;

import java.util.HashMap;
import java.util.Map.Entry;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Color;
import org.bukkit.Location;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;

import io.github.TcFoxy.ArenaTOW.Plugin.Utils;
import io.github.TcFoxy.ArenaTOW.nms.v1_13_R1.NMSUtils;
import net.minecraft.server.v1_13_R1.Entity;
import net.minecraft.server.v1_13_R1.EntityLiving;

public class Spawner extends PersistInfo{

	private HashMap<Integer, Location> paths = new HashMap<Integer, Location>();
	private HashMap<Entity, Integer> zombies;


	public Spawner(String key, Color teamColor, Location loc, String info) {
		super(key, teamColor, loc, info);
		getSpawnerInfo();
		zombies  = new HashMap<Entity, Integer>();
	}

	@Override
	public Entity spawnMob(){
		setMob(NMSUtils.spawnTeamZombie(getSpawnLoc().getWorld(), getSpawnLoc().getX(), getSpawnLoc().getY(), getSpawnLoc().getZ(), getTeamColor()));
		LivingEntity en = (LivingEntity) getMob().getBukkitEntity();
		en.getEquipment().setHelmet(Utils.makeMobHelm(getTeamColor()));
		return getMob();
	}

	public void addPp(Location loc, Player p) {
		if(paths.containsValue(loc)){
			p.sendMessage(ChatColor.DARK_RED + "Already created a pathpoint in this position. No duplicate created");
			return;
		}
		if(ppIsTooFar(loc)){
			p.sendMessage(ChatColor.DARK_RED + "PathPoint is too far from previous point. Get closer and try again");
			return;
		}
		paths.put(paths.size(), loc);
		this.saveSpawnerInfo();
		p.sendMessage("Pathpoint #" + paths.size() + " created for " +
						PersistInfo.getTeamColorStringReadable(this.getKey()) +" Spawner #" +
						PersistInfo.getObjectId(this));
	}

	private boolean ppIsTooFar(Location loc) {
		Location origin;
		if(paths.isEmpty()){
			origin = this.getSpawnLoc();
		}else{
			origin = paths.get(paths.size()-1);
		}
		return origin.distance(loc) > 10;
		}

	public void listPp(){
		for(int i=0; i<paths.size();i++){
			Bukkit.broadcastMessage(paths.get(i) + "");
		}
	}

	public boolean hasPps(){
		if(paths.size() > 0){
			return true;
		}else{
			return false;
		}
	}

	public void clearPath() {
		paths.clear();
	}
	
	
	public Location newPathDest(Entity ent){
		if(zombies.containsKey(ent)){
			Integer buf = zombies.get(ent);
			zombies.put(ent, buf+1);
			return paths.get(buf+1);
		}
		return null;
	}
	
	public Location getPathDest(Entity ent){
		if(zombies.containsKey(ent)){
			Integer buf = zombies.get(ent);
			if(paths.containsKey(buf)){
				return paths.get(buf);
			}
		}
		return null;
	}
	
	public boolean containsPps(Location loc){
		return paths.containsValue(loc);
	}


	public void addMob(Entity ent){
		zombies.put(ent, 0);
	}

	public void killMobs(){
		for(Entity zombie : zombies.keySet()){
			if(zombie.isAlive()){
				((EntityLiving) zombie).setHealth(0);

			}
		}
	}

	public void removeMob(Entity ent){
		zombies.remove(ent);
	}

	public HashMap<Entity, Integer> getZombies(){
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
		for (Location l: paths.values()) {
			buf += l.toString() + "\n";
		}
		Bukkit.broadcastMessage("Path locations of Spawner " + getKey() + ":\n" + buf);
	}

	public void getSpawnerInfo() {
		if(getInfo().equalsIgnoreCase("nopaths")){
			return;
		}
		String[] rawparts = getInfo().split(";");
		for (int i = 0; i < rawparts.length; i++) {
			paths.put(i, stringToLocation(rawparts[i]));
		}
	}

	public void saveSpawnerInfo(){
		String buf = "nopaths";
		if(paths != null){
			buf="";
			for(Entry<Integer, Location> entry : paths.entrySet()){
				buf+= locationToString(entry.getValue());
				if(paths.get(entry.getKey()+1) != null){
					buf+= ";";
				}
			}
		}
		setInfo(buf);
	}

}
