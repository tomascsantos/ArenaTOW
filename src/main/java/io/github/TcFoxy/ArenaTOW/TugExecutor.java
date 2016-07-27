package io.github.TcFoxy.ArenaTOW;

import mc.alk.arena.BattleArena;
import mc.alk.arena.executors.CustomCommandExecutor;
import mc.alk.arena.executors.MCCommand;

import org.bukkit.ChatColor;
import org.bukkit.Color;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class TugExecutor extends CustomCommandExecutor{

	static ArenaTOW atInstance;

	public TugExecutor(){
	}

	public TugExecutor(ArenaTOW instance) {
		atInstance = instance;
	}

//	@MCCommand(cmds={"listTowers"}, admin=true)
//	public static boolean listTowers(CommandSender sender, TugArena arena) {
//		return sendMessage(sender, arena.getTowerList());
//	}

	@MCCommand(cmds={"removeTower"}, admin=true)
	public static boolean removeTowers(CommandSender sender, TugArena arena, String towername){
		if (arena.removeTower(towername)){
			return sendMessage(sender, towername + "has been removed");
		} else {
			return sendMessage(sender, towername + "does not exist");
		}

	}

	@MCCommand(cmds={"removeSpawner"}, admin=true)
	public static boolean removeSpawners(CommandSender sender, TugArena arena, String spawnername){
		if (arena.removeTower(spawnername)){
			return sendMessage(sender, spawnername + "has been removed");
		} else {
			return sendMessage(sender, spawnername + "does not exist");
		}

	}

	@MCCommand(cmds={"clearTowers"}, admin=true)
	public static boolean clearTowers(CommandSender sender, TugArena arena) {
		arena.clearTowers();
		BattleArena.saveArenas(ArenaTOW.getSelf());
		return sendMessage(sender,"&2Towers cleared for &6"+arena.getName());

	}


	@MCCommand(cmds={"clearSpawners"}, admin=true)
	public static boolean clearSpawners(CommandSender sender, TugArena arena) {
		arena.clearSpawners();
		BattleArena.saveArenas(ArenaTOW.getSelf());
		return sendMessage(sender,"&2Towers cleared for &6"+arena.getName());

	}


	@MCCommand(cmds={"add"}, subCmds ={"tower"}, admin=true, usage = "add tower <[Blue] or [Red]> <Tower#> <Arena_Name>")
	public static boolean addTower(Player sender, String color, Integer index, TugArena arena){
		if (index < 1 || index > 100){
			return sendMessage(sender,"&2index must be between [1-100]!");}
		if (color.equalsIgnoreCase("Red")){
			arena.addTower(index, sender.getLocation(), sender, Color.RED, arena);
			BattleArena.saveArenas(ArenaTOW.getSelf());
			return true; 

		}else if(color.equalsIgnoreCase("Blue")){
			arena.addTower(index, sender.getLocation(), sender, Color.BLUE, arena);
			BattleArena.saveArenas(ArenaTOW.getSelf());
			return true; 

		}else{
			sendMessage(sender, ChatColor.DARK_RED + "tower color must be either <Red> or <Blue>");
			return false; 
		}
	}
	
	@MCCommand(cmds={"add"}, subCmds ={"nexus"}, admin=true, usage = "add nexus <[Blue] or [Red]> <Arena_Name>")
	public static boolean addNexus(Player sender, String color, TugArena arena){

		if (color.equalsIgnoreCase("Red")){
			arena.addNexus(sender.getLocation(), sender, Color.RED, arena);
			BattleArena.saveArenas(ArenaTOW.getSelf());
			return true; 

		}else if(color.equalsIgnoreCase("Blue")){
			arena.addNexus(sender.getLocation(), sender, Color.BLUE, arena);
			BattleArena.saveArenas(ArenaTOW.getSelf());
			return true; 

		}else{
			sendMessage(sender, ChatColor.DARK_RED + "tower color must be either <Red> or <Blue>");
			return false; 
		}
	}
	
	@MCCommand(cmds={"add"}, subCmds={"spawner"}, admin=true, usage = "add spawner <[Blue] or [Red]> <Spawner#> <Arena_Name>")
	public static boolean addSpawner(Player sender,  String color, Integer index, TugArena arena){
		if (index < 1 || index > 100){
			return sendMessage(sender,"&2index must be between [1-100]!");}
		if (color.equalsIgnoreCase("Red")){
			arena.addSpawner(sender, sender.getLocation(), "Red", arena, index);
			BattleArena.saveArenas(ArenaTOW.getSelf());
			return true; 
		}else if(color.equalsIgnoreCase("Blue")){
			arena.addSpawner(sender, sender.getLocation(), "Blue", arena, index);
			BattleArena.saveArenas(ArenaTOW.getSelf());
			return true; 
		}else{
			sendMessage(sender, ChatColor.DARK_RED + "Spawner color must be either <Red> or <Blue>");
			return false; 
		}
	}
	
	@MCCommand(cmds={"add"}, subCmds={"pathpoint", "pp"}, admin=true, usage = "add pathpoint "
																	   + "<[Blue] or [Red]>\n &4(What color spawner they come from)\n "
																	   + "&6<Spawner#> \n &4(what number spawner they spawn at)\n"
																	   + "&6<Arena_Name>")
	public static boolean addPathPoints(Player sender, String color, Integer index, TugArena arena) {
		if (color.equalsIgnoreCase("Red")){
			arena.addPathPoints(sender, sender.getLocation(), "Red", index);
			BattleArena.saveArenas(ArenaTOW.getSelf());
			return true; 
		}else if(color.equalsIgnoreCase("Blue")){
			arena.addPathPoints(sender, sender.getLocation(), "Blue", index);
			BattleArena.saveArenas(ArenaTOW.getSelf());
			return true; 
		}else{
			sendMessage(sender, ChatColor.DARK_RED + "PathPointColor color must be either <Red> or <Blue>");
			return false; 
		}
	}


	@MCCommand(cmds={"add"}, subCmds={"deathroom"}, admin=true, usage = "add deathroom " + "<[Blue] or [Red]> (team) " + "&6<Arena_Name>")
	public static boolean deathroom(Player sender, String color, TugArena arena) {
		if (color.equalsIgnoreCase("Red")){
			arena.addDeathroom(sender, sender.getLocation(), "Red");
			BattleArena.saveArenas(ArenaTOW.getSelf());
			return true; 
		}else if(color.equalsIgnoreCase("Blue")){
			arena.addDeathroom(sender, sender.getLocation(), "Blue");
			BattleArena.saveArenas(ArenaTOW.getSelf());
			return true; 
		}else{
			sendMessage(sender, ChatColor.DARK_RED + "DeathRoom color must be either <Red> or <Blue>");
			return false; 
		}
	}
	

}



	
	
	
