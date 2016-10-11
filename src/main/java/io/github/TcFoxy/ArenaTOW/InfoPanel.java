package io.github.TcFoxy.ArenaTOW;

import java.util.HashMap;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Color;
import org.bukkit.entity.Player;

import io.github.TcFoxy.ArenaTOW.BattleArena.objects.ArenaPlayer;
import io.github.TcFoxy.ArenaTOW.BattleArena.objects.scoreboard.ArenaScoreboard;
import io.github.TcFoxy.ArenaTOW.Serializable.Nexus;
import io.github.TcFoxy.ArenaTOW.Serializable.PersistInfo;
import io.github.TcFoxy.ArenaTOW.Serializable.Tower;
import io.github.TcFoxy.ArenaTOW.scoreboard.scoreboard.SAPIDisplaySlot;
import io.github.TcFoxy.ArenaTOW.scoreboard.scoreboard.api.SObjective;

public class InfoPanel {

	TugArena tug;
	HashMap<String, PersistInfo> activeInfo;

	public void onStart(TugArena tug, HashMap<String, PersistInfo> activeInfo) {

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
		for(PersistInfo info : activeInfo.values()){
			if(info instanceof Tower || info instanceof Nexus){
				
			}
		}
	}
	
	
	private String getScoreName(PersistInfo info){
		String[] rawparts = info.getKey().split("_");
		String scoreName = "";
		if(info.getTeamColor().equals(Color.RED)){
			scoreName += ChatColor.DARK_RED;
		}else{
			scoreName += ChatColor.BLUE;
		}
		scoreName+= PersistInfo.getTeamColorStringReadable(info.getKey()) + rawparts[2];
		if(!(info instanceof Nexus)){
			return scoreName += rawparts[4];
		}
		return scoreName;
		
	}
}
