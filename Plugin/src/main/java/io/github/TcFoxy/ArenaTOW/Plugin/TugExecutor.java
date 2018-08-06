package io.github.TcFoxy.ArenaTOW.Plugin;

import java.util.HashMap;

import mc.alk.arena.executors.CustomCommandExecutor;
import mc.alk.arena.executors.MCCommand;
import org.bukkit.ChatColor;
import org.bukkit.Color;
import org.bukkit.Location;
import org.bukkit.entity.Player;

import io.github.TcFoxy.ArenaTOW.Plugin.Serializable.AbstractStructure;
import io.github.TcFoxy.ArenaTOW.Plugin.Serializable.AbstractStructure.BaseType;

public class TugExecutor extends CustomCommandExecutor {

	static ArenaTOW atInstance;

	public TugExecutor(){
	}

	public TugExecutor(ArenaTOW instance) {
		atInstance = instance;
	}

	/*
	 * Util Commands
	 */

	private static Color getColor(String color, Player sender){
		if(color.equalsIgnoreCase("Red")){
			return Color.RED;
		}else if(color.equalsIgnoreCase("Blue")){
			return Color.BLUE;
		}else{
			sendMessage(sender, ChatColor.DARK_RED + "tower color must be either <Red> or <Blue>");
			return null;
		}
	}
	
	public static void initSaves(TugArena arena){
		if(arena.savedInfo != null){
			arena.activeInfo = AbstractStructure.getObject(arena.savedInfo);
		}else{
			arena.savedInfo = new HashMap<String, String>();
			arena.activeInfo = AbstractStructure.getObject(arena.savedInfo);
		}
	}
	
	/*
	 * 
	 * Removal Commands
	 * 
	 */
	
	public static boolean remove(Player sender, String color, Integer index, TugArena arena, BaseType type){
		Color col = getColor(color, sender);
		if(col ==null){
			return false;
		}
		Location loc = sender.getLocation();
		
		initSaves(arena);
		
		switch(type){
		case TOWER:
			return arena.removeTower(sender, loc, col, index);
		case NEXUS:
			return arena.removeNexus(sender, loc, col);
		case SPAWNER:
			return arena.removeSpawner(sender, loc, col, index);
		case DEATHROOM:
			return arena.removeDeathroom(sender, loc, col);
		default:
			return false;
		}
	}
	
	@MCCommand(cmds="remove", subCmds={"help"}, admin=true, usage = "show available remove commands")
	public static boolean removeHelp(Player sender){
		sendMessage(sender, "remove Tower <Blue/Red> <Tower#> <Arena_Name>");
		sendMessage(sender, "remove spawner <Blue/Red> <Spawner#> <Arena_Name>");
		sendMessage(sender, "remove deathroom <Blue/Red> <Arena_Name>");
		sendMessage(sender, "remove Nexus <Blue/Red> <Arena_Name>");
		return true;
	}

	@MCCommand(cmds={"remove", "r"}, subCmds={"tower"}, admin=true, usage="remove tower <Blue/Red> <Tower#> <Arena_Name>")
	public static boolean removeTower(Player sender, String color, Integer index, TugArena arena){
		return remove(sender, color, index, arena, BaseType.TOWER);
	}
	
	@MCCommand(cmds={"remove", "r"}, subCmds={"spawner"}, admin=true, usage="remove spawner <Blue/Red> <Tower#> <Arena_Name>")
	public static boolean removeSpawner(Player sender, String color, Integer index, TugArena arena){
		return remove(sender, color, index, arena, BaseType.SPAWNER);
	}
	
	@MCCommand(cmds={"remove", "r"}, subCmds={"nexus"}, admin=true, usage="remove nexus <Blue/Red> <Arena_Name>")
	public static boolean removeNexus(Player sender, String color, TugArena arena){
		return remove(sender, color, null, arena, BaseType.NEXUS);
	}
	
	@MCCommand(cmds={"remove", "r"}, subCmds={"deathroom"}, admin=true, usage="remove deathroom <Blue/Red> <Arena_Name>")
	public static boolean removeDeathRoom(Player sender, String color, TugArena arena){
		return remove(sender, color, null, arena, BaseType.DEATHROOM);
	}
	
	/*
	 * 
	 * clearCommands
	 * 
	 */
	
	public static boolean clear(Player sender, TugArena arena, BaseType type){
		initSaves(arena);
		return arena.clearInfo(type, sender);
	}

	@MCCommand(cmds="clear", subCmds={"help"}, admin=true, usage = "show available clear commands")
	public static boolean clearHelp(Player sender){
		sendMessage(sender, "remove Tower <Blue/Red> <Tower#> <Arena_Name>");
		sendMessage(sender, "remove spawner <Blue/Red> <Spawner#> <Arena_Name>  (Removes pathpoints for that spawner as well)");
		sendMessage(sender, "remove deathroom <Blue/Red> <Arena_Name>");
		sendMessage(sender, "remove Nexus <Blue/Red> <Arena_Name>");
		return true;
	}

	@MCCommand(cmds={"clear"}, subCmds={"tower"}, admin=true, usage="clear tower")
	public static boolean clearTower(Player sender, TugArena arena){
		return clear(sender, arena, BaseType.TOWER);
	}
	
	@MCCommand(cmds={"clear"}, subCmds={"spawner"}, admin=true, usage="clear spawner")
	public static boolean clearSpawner(Player sender, TugArena arena){
		return clear(sender, arena, BaseType.SPAWNER);
	}
	
	@MCCommand(cmds={"clear"}, subCmds={"nexus"}, admin=true, usage="clear nexus")
	public static boolean clearNexus(Player sender, TugArena arena){
		return clear(sender, arena, BaseType.SPAWNER);
	}
	
	/*
	 * 
	 * Add commands
	 * 
	 */
	
	public static boolean add(Player sender, String color, Integer index, TugArena arena, BaseType type){
		Color col = getColor(color, sender);
		if(col ==null){
			return false;
		}
		initSaves(arena);
		
		Location loc = sender.getLocation();
		
		switch(type){
		case TOWER:
			return arena.addTower(loc, sender, col, index);
		case NEXUS:
			return arena.addNexus(loc, sender, col);
		case SPAWNER:
			return arena.addSpawner(loc, sender, col, index);
		case PATHP:
			return arena.addPathPoints(loc, sender, col, index);
		case DEATHROOM:
			return arena.addDeathroom(loc, sender, col);
		default:
			return false;
		}
	}

	@MCCommand(cmds={"add"}, subCmds={"help"}, admin=true, usage = "show available add commands")
	public static boolean add(Player sender){
		sendMessage(sender, "add Tower <Blue/Red> <Tower#> <Arena_Name>");
		sendMessage(sender, "add spawner <Blue/Red> <Spawner#> <Arena_Name>");
		sendMessage(sender, "add pathpoint <Blue/Red> <Spawner#> <Arena_Name>");
		sendMessage(sender, "add deathroom <Blue/Red> <Arena_Name>");
		sendMessage(sender, "add Nexus <Blue/Red> <Arena_Name>");
		return true;
	}


	@MCCommand(cmds={"add"}, subCmds ={"tower"}, admin=true, usage = "add tower <Blue/Red> <Tower#> <Arena_Name>")
	public static boolean addTower(Player sender, String color, Integer index, TugArena arena){
		if (index < 1 || index > 10){
			return sendMessage(sender,"&2index must be between [1-10]!");}
		return add(sender, color, index, arena, BaseType.TOWER);
	}

	@MCCommand(cmds={"add"}, subCmds ={"nexus"}, admin=true, usage = "add nexus <Blue/Red> <Arena_Name>")
	public static boolean addNexus(Player sender, String color, TugArena arena){
		return add(sender, color, null, arena, BaseType.NEXUS);
	}

	@MCCommand(cmds={"add"}, subCmds={"spawner"}, admin=true, usage = "add spawner <Blue/Red> <Spawner#> <Arena_Name>")
	public static boolean addSpawner(Player sender,  String color, Integer index, TugArena arena){
		if (index < 1 || index > 100){
			return sendMessage(sender,"&2index must be between [1-100]!");}
		return add(sender, color, index, arena, BaseType.SPAWNER);
	}

	@MCCommand(cmds={"add"}, subCmds={"pathpoint", "pp"}, admin=true, usage = "add pathpoint <Blue/Red> <Spawner#> <Arena_Name>")
	public static boolean addPathPoints(Player sender, String color, Integer index, TugArena arena) {
		return add(sender, color, index, arena, BaseType.PATHP);
	}


	@MCCommand(cmds={"add"}, subCmds={"deathroom"}, admin=true, usage = "add deathroom <Blue/Red> <Arena_Name>")
	public static boolean deathroom(Player sender, String color, TugArena arena) {
		return add(sender, color, null, arena, BaseType.DEATHROOM);
	}


	/*
	 * 
	 * Verify Commands
	 * 
	 */
	
	@MCCommand(cmds="check", admin=true, usage = "check <Arena_Name>")
	public static boolean verifyArena(Player sender, TugArena arena){
		initSaves(arena);
		arena.verify(sender);
		return true;
	}
}






