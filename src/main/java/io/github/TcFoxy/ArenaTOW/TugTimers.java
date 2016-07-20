package io.github.TcFoxy.ArenaTOW;

import io.github.TcFoxy.ArenaTOW.MinionStuff.Minion;
import io.github.TcFoxy.ArenaTOW.nms.v1_10_R1.CustomEntityZombie;
import io.github.TcFoxy.ArenaTOW.nms.v1_10_R1.interfaces.NMSUtils;

import java.util.Map.Entry;
import java.util.concurrent.ConcurrentHashMap;

import net.minecraft.server.v1_10_R1.EnumItemSlot;

import org.bukkit.Bukkit;
import org.bukkit.Location;

public class TugTimers {

	private TugArena tug;
    Integer spawnerId, checkerId, timerId, gametimeId, gameTime = 0;//timer ID's
    
    //stores all the minions so they can be killed @ and of game
	private ConcurrentHashMap<String, Minion> livingMinions = new ConcurrentHashMap<String, Minion>();


	
	TugTimers(TugArena tug){
		this.tug = tug;
		livingMinions = new ConcurrentHashMap<String, Minion>();
	}
	
	/*
	 * called to cancell ALL the timers.
	 */
	public void cancelTimers(){
        if (spawnerId != null){
            Bukkit.getScheduler().cancelTask(spawnerId);
            spawnerId = null;
        }
        if (checkerId != null){
            Bukkit.getScheduler().cancelTask(checkerId);
            checkerId = null;
        }
        if (timerId != null){
            Bukkit.getScheduler().cancelTask(timerId);
            timerId = null;
        }
        if (gametimeId != null){
            Bukkit.getScheduler().cancelTask(gametimeId);
            gametimeId = null;
        }
        if(tug.deathtimer != null){
        	Bukkit.getScheduler().cancelTask(tug.deathtimer);
        }  
        Bukkit.getScheduler().cancelTask(tug.ACexecutor.ACC.cooldownId);
    }
	
	/*
	 * This timer keeps track of how long the game has been going
	 * and every 20 ticks (1second) it increments the "game time"
	 * by 1, this is used to calculate respawn times.
	 */
	public void gameTime(){
		gametimeId = Bukkit.getScheduler().scheduleSyncRepeatingTask(ArenaTOW.getSelf(), new Runnable(){
			@Override
			public void run() {
				gameTime++;
			}

		}, 0*Utils.TPS, 1*Utils.TPS);
	}

	
	/*
	 * every 30 seconds this method spawns 4 zombies
	 * for each spawner on each team, it sets their team,
	 * and adds them to the hashmap that contains all the living
	 * entities so that they can be killed by the end of the game
	 */
	public void startEntitySpawn(){
		spawnerId = Bukkit.getScheduler().scheduleSyncRepeatingTask(ArenaTOW.getSelf(), new Runnable(){
			@Override
			public void run() {
				for(int i = 0; i<4; i++){
					spawnMobFromFactory();
				}

			}

		}, 0*Utils.TPS, 30*Utils.TPS);
	}
	//spawnMobFromFactory() is used with above timer.
	public void spawnMobFromFactory(){
		for(Entry<String,String> entry: tug.minionFactorySpawners.entrySet()){
			Minion m = tug.minionFactory.createMinion(entry.getKey());
			Location startloc = m.getStartLoc();
			CustomEntityZombie zombie = null;
			if(m.getTeam().equalsIgnoreCase("Red")){
				zombie = NMSUtils.spawnRedZombie(startloc.getWorld(), startloc.getX(), startloc.getY(), startloc.getZ());
				zombie.setEquipment(EnumItemSlot.HEAD, Utils.makeMobHelm("Red"));
				zombie.setCustomName("RedTeam Minion");
				zombie.setCustomNameVisible(true);
			}else{
				zombie= NMSUtils.spawnBlueZombie(startloc.getWorld(), startloc.getX(), startloc.getY(), startloc.getZ());
				zombie.setEquipment(EnumItemSlot.HEAD, Utils.makeMobHelm("Blue"));
				zombie.setCustomName("BlueTeam Minion");
				zombie.setCustomNameVisible(true);
			}
			m.setMinionEntity(zombie);
			zombie.whereTo(m.peekPathLoc());
			
			livingMinions.put(livingMinions.size()+entry.getKey(), m);
		}
	}
	
	public void killminions(){
		for(Entry<String, Minion> entry: livingMinions.entrySet()){
			if (entry.getValue().getMinion().isAlive()){
				entry.getValue().getMinion().setHealth(0);
			}
		}
		livingMinions.clear();
	}

	
	/*
	 * This method checks the pathfinding target of the zombie
	 * every 5 seconds and if it has reached its current pathfining
	 * goal it removes the current goal and adds a new one
	 */
	public void startEntityChecker(){
		checkerId = Bukkit.getScheduler().scheduleSyncRepeatingTask(ArenaTOW.getSelf(), new Runnable() {
			@Override
			public void run() {
			for(Entry<String, Minion> entry : livingMinions.entrySet()){
				Minion m = entry.getValue();
				if(!m.getMinion().isAlive()){
					//do nothing
				}else if(m.peekPathLoc() != null) {
					Location cLoc = m.peekPathLoc();
					Location entityloc = m.getcurrentLocation();
					if((entityloc.getX() <= cLoc.getX()+3 && entityloc.getX() >= cLoc.getX()-3
							&& entityloc.getY() <= cLoc.getY()+3 && entityloc.getY() >= cLoc.getY()-3
							&& entityloc.getZ() <= cLoc.getZ()+3 && entityloc.getZ() >= cLoc.getZ()-3)) {
						m.nextPathLoc();
						((CustomEntityZombie)m.getMinion()).whereTo(m.peekPathLoc());
					}
				}	
			}
			}		
		}, 5*Utils.TPS, 5*Utils.TPS);
	}
}
