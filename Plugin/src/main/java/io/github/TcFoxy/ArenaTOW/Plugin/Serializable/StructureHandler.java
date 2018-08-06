package io.github.TcFoxy.ArenaTOW.Plugin.Serializable;

import org.bukkit.*;

import java.util.*;

public class StructureHandler {

    public static ArrayList<String> getTypeObject(StructureType type, HashMap<String, AbstractStructure> dic){

        ArrayList<String> towers = new ArrayList<String>();
        ArrayList<String> nexus = new ArrayList<String>();
        ArrayList<String> spawners = new ArrayList<String>();
        ArrayList<String> deathrooms = new ArrayList<String>();

        for(Map.Entry<String, AbstractStructure> entry : dic.entrySet()){
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


    public static Deathroom getDeathroom(String color, HashMap<String, AbstractStructure> dic){
        for(AbstractStructure base : dic.values()){
            if((base instanceof Deathroom) && AbstractStructure.getTeamColorStringReadable(base.getKey()).equalsIgnoreCase(color)){
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
}
