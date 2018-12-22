package io.github.TcFoxy.ArenaTOW.Plugin;

import io.github.TcFoxy.ArenaTOW.API.CustomZombie;
import io.github.TcFoxy.ArenaTOW.API.TOWEntity;
import io.github.TcFoxy.ArenaTOW.Plugin.Serializable.PersistInfo;
import io.github.TcFoxy.ArenaTOW.Plugin.Serializable.Spawner;
import org.bukkit.Bukkit;
import org.bukkit.Location;

import java.util.HashMap;

public class TugTimers {

    private TugArena tug;
    Integer spawnerId, checkerId, timerId, gametimeId, gameTime = 0;//timer ID's

    TugTimers(TugArena tug) {
        this.tug = tug;
    }

    /*
     * called to cancell ALL the timers.
     */
    public void cancelTimers() {
        if (spawnerId != null) {
            Bukkit.getScheduler().cancelTask(spawnerId);
            spawnerId = null;
        }
        if (checkerId != null) {
            Bukkit.getScheduler().cancelTask(checkerId);
            checkerId = null;
        }
        if (timerId != null) {
            Bukkit.getScheduler().cancelTask(timerId);
            timerId = null;
        }
        if (gametimeId != null) {
            Bukkit.getScheduler().cancelTask(gametimeId);
            gametimeId = null;
        }
        if (tug.deathtimer != null) {
            Bukkit.getScheduler().cancelTask(tug.deathtimer);
        }
//        if (tug.ACexecutor == null){
//        	Bukkit.getLogger().warning("ACexecutor is null");
//        }else if(tug.ACexecutor.ACC == null){
//        	Bukkit.getLogger().warning("tug.ACexecutor.ACC is null");
//        }else if (tug.ACexecutor.ACC.cooldownId == null){
//        	Bukkit.getLogger().warning("tug.ACexecutor.ACC.cooldownId is null");
//        }else{
//            Bukkit.getScheduler().cancelTask(tug.ACexecutor.ACC.cooldownId);
//        }
    }

    /*
     * This timer keeps track of how long the game has been going
     * and every 20 ticks (1second) it increments the "game time"
     * by 1, this is used to calculate respawn times.
     */
    public void gameTime() {
        gametimeId = Bukkit.getScheduler().scheduleSyncRepeatingTask(ArenaTOW.getSelf(), new Runnable() {
            @Override
            public void run() {
                gameTime++;
            }

        }, 0 * Utils.TPS, 1 * Utils.TPS);
    }


    /*
     * every 30 seconds this method spawns 4 zombies
     * for each spawner on each team, it sets their team,
     * and adds them to the hashmap that contains all the living
     * entities so that they can be killed by the end of the game
     */
    public void startEntitySpawn(TugArena arena) {
        spawnerId = Bukkit.getScheduler().scheduleSyncRepeatingTask(ArenaTOW.getSelf(), new Runnable() {
            @Override
            public void run() {
                for (int i = 0; i < 4; i++) {
                    spawnMobFromFactory(arena);
                }

            }

        }, 0 * Utils.TPS, 30 * Utils.TPS);
    }

    //spawnMobFromFactory() is used with above timer.
    public void spawnMobFromFactory(TugArena arena) {
        for (PersistInfo info : tug.activeInfo.values()) {
            if (info instanceof Spawner) {
                Spawner spawn = (Spawner) info;

                CustomZombie zombie = (CustomZombie) info.spawnMob(arena);
                spawn.addMob(zombie);
                zombie.whereTo(spawn.getPathDest(zombie));
            }
        }
    }

    public void killminions() {
        for (PersistInfo info : tug.activeInfo.values()) {
            if (info instanceof Spawner) {
                ((Spawner) info).killMobs();
            }
        }
    }


    /*
     * This method checks the pathfinding target of the zombie
     * every 5 seconds and if it has reached its current pathfining
     * goal it removes the current goal and adds a new one
     */
    public void startEntityChecker() {
        checkerId = Bukkit.getScheduler().scheduleSyncRepeatingTask(ArenaTOW.getSelf(), new Runnable() {
            @Override
            public void run() {
                for (PersistInfo info : tug.activeInfo.values()) {
                    if (info instanceof Spawner) {
                        Spawner spawn = (Spawner) info;
                        HashMap<TOWEntity, Integer> zombies = spawn.getZombies();
                        for (TOWEntity zombie : zombies.keySet()) {
                            if (isWithinRange(spawn, zombie)) {
                                ((CustomZombie) zombie).whereTo(spawn.newPathDest(zombie));
                            }
                        }
                    }
                }
            }
        }, 5 * Utils.TPS, 5 * Utils.TPS);
    }

    private boolean isWithinRange(Spawner s, TOWEntity e) {
        if (s.getPathDest(e) != null) {
            Location cLoc = s.getPathDest(e);
            Location entityloc = s.getcurrentLocation(e);
            if (entityloc == null) {
                return false;
            }
            return entityloc.distance(cLoc) < 3;
        }
        return false;
    }
}
