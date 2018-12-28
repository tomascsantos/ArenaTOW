package io.github.TcFoxy.ArenaTOW.Plugin.Serializable;

import io.github.TcFoxy.ArenaTOW.API.CustomZombie;
import io.github.TcFoxy.ArenaTOW.API.MobType;
import io.github.TcFoxy.ArenaTOW.API.TOWEntity;
import io.github.TcFoxy.ArenaTOW.Plugin.ArenaTOW;
import io.github.TcFoxy.ArenaTOW.Plugin.TugArena;
import io.github.TcFoxy.ArenaTOW.Plugin.Utils;
import org.bukkit.*;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;

import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.Map.Entry;

public class Spawner extends PersistInfo {

    private LinkedList<Location> paths = new LinkedList<>();
    private HashMap<TOWEntity, Integer> zombies;


    Spawner(String key, Color teamColor, Location loc, String info) {
        super(key, teamColor, loc, info);
        getSpawnerInfo();
        zombies = new HashMap<>();
    }

    @Override
    public TOWEntity spawnMob(TugArena arena) {
        Location spawn = getSpawnLoc();

        TOWEntity mob = arena.getEntityHandler().spawnMob(arena.getEntityHandler(), MobType.ZOMBIE, getTeamColor(),
                spawn.getWorld(), spawn.getX(), spawn.getY(), spawn.getZ());
        setMob(mob);
        ((CustomZombie) mob).addPath(paths);
        LivingEntity en = (LivingEntity) mob.getMob();
        en.getEquipment().setHelmet(Utils.makeMobHelm(getTeamColor()));
        return mob;
    }

    public void addPp(Location loc, Player p) {
        if (paths.contains(loc)) {
            p.sendMessage(ChatColor.DARK_RED + "Already created a pathpoint in this position. No duplicate created");
            return;
        }
        if (ppIsTooFar(loc)) {
            p.sendMessage(ChatColor.DARK_RED + "PathPoint is too far from previous point. Get closer and try again");
            return;
        }
        paths.addLast(loc);
        this.saveSpawnerInfo();
        p.sendMessage("Pathpoint #" + paths.size() + " created for " +
                PersistInfo.getTeamColorStringReadable(this.getKey()) + " Spawner #" +
                PersistInfo.getObjectId(this));

    }

    private boolean ppIsTooFar(Location loc) {
        Location origin;
        if (paths.isEmpty()) {
            origin = this.getSpawnLoc();
        } else {
            origin = paths.get(paths.size() - 1);
        }
        return origin.distance(loc) > 10;
    }

    public boolean hasPps() {
        return paths.size() > 0;
    }

    public boolean containsPps(Location loc) {
        return paths.contains(loc);
    }


    public void addMob(TOWEntity ent) {
        zombies.put(ent, 0);
    }

    public void killMobs() {
        for (TOWEntity zombie : zombies.keySet()) {
            if (zombie.isAlive()) {
                zombie.setHealth(0);
            }
        }
    }

    private void getSpawnerInfo() {
        if (getInfo().equalsIgnoreCase("nopaths")) {
            return;
        }
        String[] rawparts = getInfo().split(";");
        for (int i = 0; i < rawparts.length; i++) {
            paths.add(stringToLocation(rawparts[i]));
        }
    }

    private void saveSpawnerInfo() {
        String buf = "nopaths";
        if (paths != null) {
            buf = "";
            Iterator iter = paths.iterator();
            while (iter.hasNext()) {
                buf += locationToString((Location) iter.next());
                buf += ";";
            }
        }
        setInfo(buf);
    }

}
