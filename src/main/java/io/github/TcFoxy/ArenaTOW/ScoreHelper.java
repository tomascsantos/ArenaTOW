package io.github.TcFoxy.ArenaTOW;

import java.util.HashMap;
import java.util.UUID;

import mc.alk.arena.objects.ArenaPlayer;
import mc.alk.arena.objects.scoreboard.ArenaScoreboard;
import mc.alk.scoreboardapi.api.SEntry;
import mc.alk.scoreboardapi.api.SObjective;
import mc.alk.scoreboardapi.scoreboard.SAPIDisplaySlot;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;


public class ScoreHelper {
	
	static TugArena tug;
	private static HashMap<UUID, ArenaScoreboard> boards;
//	private HashMap<UUID, Integer> cash;

	ScoreHelper(TugArena Tug){
		ScoreHelper.tug = Tug;

		boards = new HashMap<UUID, ArenaScoreboard>();
//		cash = new HashMap<UUID, Integer>();
	}
	
	public void onStart() {
		
		
		Bukkit.getScheduler().scheduleSyncDelayedTask(ArenaTOW.getSelf(), new Runnable(){
			@Override
			public void run(){
				for(ArenaPlayer ap: tug.arena.getMatch().getPlayers()){
					setupScoreboard(ap.getPlayer());
				}
			}
		});
		
	}
	


	public static void setupScoreboard(Player p){
		ArenaScoreboard newsb = new ArenaScoreboard(p.getName()); ///making a new scoreboard with player name as the scoreboard name;
		SObjective anobj = newsb.registerNewObjective("ArenaTOW", "dummy", ChatColor.WHITE.toString() + ChatColor.BOLD + "LeagueOfCrafter", SAPIDisplaySlot.SIDEBAR);
		makeSidebar(anobj, p);
		newsb.setScoreboard(p);
		boards.put(p.getUniqueId(), newsb);
	}
	
//	public static Integer getTowHealth(Tower t){
//		EntityCreature creature = t.getMob();
//		float health = Math.round(creature.getHealth());
//		float maxHealth = creature.getMaxHealth();
//		float percentHealth = (health/maxHealth);
//		percentHealth *= 100.0;
//		return  (int) Math.round(percentHealth); 
//	}

	
	SEntry redhealth, bluehealth, playermoney;

	private static void makeSidebar(SObjective obj, Player p) {
		
		//obj.addEntry(ChatColor.DARK_GREEN.toString() + ChatColor.BOLD + "Cash", ArenaEcon.getCash(p));

		//addTowerHealths(obj, p);
		
//		ArenaTeam team = BattleArena.toArenaPlayer(p).getTeam();
//		obj.addEntry(ChatColor.DARK_PURPLE.toString() + ChatColor.BOLD + "TeamLevel", tug.teamLevel.getTeamLev(team.getDisplayName()));
	}
	
//	private static void addTowerHealths(SObjective obj, Player p){
//		for(Tower tow: tug.towerteams.values()){
//			if (tow.getTeamColor() == Color.RED){
//				if(tow.getType().equals("Tower")){
//					obj.addEntry(ChatColor.DARK_RED.toString() + ChatColor.BOLD + "Red Tower", getTowHealth(tow));
//				}else if(tow.getType().equals("Nexus")){
//					obj.addEntry(ChatColor.DARK_RED.toString() + ChatColor.BOLD + "Red Nexus", getTowHealth(tow));
//				}
//			}else if (tow.getTeamColor() == Color.BLUE){
//				if(tow.getType().equals("Tower")){
//					obj.addEntry(ChatColor.BLUE.toString() + ChatColor.BOLD + "Blue Tower", getTowHealth(tow));
//				}else if(tow.getType().equals("Nexus")){
//					obj.addEntry(ChatColor.BLUE.toString() + ChatColor.BOLD + "Blue Nexus", getTowHealth(tow));
//				}
//			}
//		}
//	}
	
	public void refreshScore(Player p){
		ArenaScoreboard sb = boards.get(p.getUniqueId());
		SObjective obj = sb.getObjective("ArenaTOW");
		makeSidebar(obj, p);
		sb.setScoreboard(p);
		}
}















