package io.github.TcFoxy.ArenaTOW.Plugin;

import java.util.HashMap;

import mc.alk.arena.objects.ArenaPlayer;
import mc.alk.arena.objects.scoreboard.ArenaScoreboard;
import mc.alk.scoreboardapi.api.SObjective;
import mc.alk.scoreboardapi.scoreboard.SAPIDisplaySlot;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Color;
import org.bukkit.entity.Player;

import io.github.TcFoxy.ArenaTOW.Plugin.Serializable.Nexus;
import io.github.TcFoxy.ArenaTOW.Plugin.Serializable.AbstractStructure;
import io.github.TcFoxy.ArenaTOW.Plugin.Serializable.Tower;

public class InfoPanel {

	TugArena tug;
	HashMap<String, AbstractStructure> activeInfo;

	public void onStart(TugArena tug, HashMap<String, AbstractStructure> activeInfo) {

		this.tug = tug;
		this.activeInfo = activeInfo;
		initBoard();
	}
	
	private void initBoard(){
		Bukkit.getScheduler().scheduleSyncDelayedTask(ArenaTOW.getSelf(), new Runnable(){
			@Override
			public void run(){
				for(ArenaPlayer ap: tug.arena.getMatch().getPlayers()){
					setupScoreboard(ap.getPlayer());
				}
			}
		});
	}



	public void setupScoreboard(Player p){
				
		ArenaScoreboard myScore = new ArenaScoreboard(tug.getName());
		SObjective obj = myScore.registerNewObjective("myScore", "dummy", "Game Info", SAPIDisplaySlot.SIDEBAR);
		
		setupObjective(obj);
		
		///making a new scoreboard with player name as the scoreboard name;
//		ArenaScoreboard newsb = new ArenaScoreboard(p.getName()); 
//		SObjective anobj = newsb.registerNewObjective("ArenaTOW", "dummy",
//				ChatColor.WHITE.toString() + ChatColor.BOLD + "LeagueOfCrafter",
//				SAPIDisplaySlot.SIDEBAR);
//		makeSidebar(anobj, p);
//		newsb.setScoreboard(p);
//		boards.put(p.getUniqueId(), newsb);
	}
	
	
	private void setupObjective(SObjective obj){
		for(AbstractStructure info : activeInfo.values()){
			if(info instanceof Tower || info instanceof Nexus){
				
			}
		}
	}
	
	
	private String getScoreName(AbstractStructure info){
		String[] rawparts = info.getKey().split("_");
		String scoreName = "";
		if(info.getTeamColor().equals(Color.RED)){
			scoreName += ChatColor.DARK_RED;
		}else{
			scoreName += ChatColor.BLUE;
		}
		scoreName+= AbstractStructure.getTeamColorStringReadable(info.getKey()) + rawparts[2];
		if(!(info instanceof Nexus)){
			return scoreName += rawparts[4];
		}
		return scoreName;
		
	}
}
