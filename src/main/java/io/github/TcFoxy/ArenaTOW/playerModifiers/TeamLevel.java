package io.github.TcFoxy.ArenaTOW.playerModifiers;

import java.util.HashMap;
import java.util.Set;

import mc.alk.arena.objects.teams.ArenaTeam;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

public class TeamLevel {
	
	public HashMap<String, Level> TeamLevs = new HashMap<String, Level>();;

	public void setTeamLev(ArenaTeam team){
		if(TeamLevs == null){
			Bukkit.broadcastMessage("its null");
			TeamLevs = new HashMap<String, Level>();
		}else if(team == null){
			Bukkit.broadcastMessage("team is null");
		}
		TeamLevs.put(team.getDisplayName(), new Level(team));
	}
	
	public void resetTeams(){
		if(TeamLevs == null){
			TeamLevs = new HashMap<String, Level>();
		}
		TeamLevs.clear();
	}
	
	public void addTeamPoint(Integer quantity, ArenaTeam team){
		if(TeamLevs.containsKey(team.getDisplayName())){
			Level l = TeamLevs.get(team.getDisplayName());
			l.addPoints(quantity);
			Level newlev = checkLev(l, team);
			TeamLevs.remove(team.getDisplayName());
			TeamLevs.put(team.getDisplayName(), newlev);
		}else{
			Bukkit.broadcastMessage("Not a valid team to levelup");
		}
		
	}

	public Integer getTeamLev(String team){
		if(TeamLevs.containsKey(team)){
			Level l = TeamLevs.get(team);
			return l.getLev();
		}
		Bukkit.broadcastMessage("not a valid team to get");
		return null;
	}
	
	public Level checkLev(Level l, ArenaTeam team) {
		Integer currentl = l.getLev();
		Level newLev = l;
		if(l.getPoints() >= currentl*20){
			newLev = new Level(l.getName(), l.getLev()+1);
			Set<Player> players = team.getBukkitPlayers();
			for(Player p:players){
				p.setMaxHealth(p.getMaxHealth()+1);
			}
		}
		return newLev;
	}

	public class Level{
		private Integer lev, points;
		private String teamname;
		private ArenaTeam ateam;
		
		public Level(String team){
			teamname = team;
			lev = 1;
			points = 0;
		}
		public Level(ArenaTeam team){
			ateam = team;
			teamname = team.getDisplayName();
			lev = 1;
			points = 0;
		}
		public Level(String team, Integer lev){
			teamname = team;
			this.lev = lev;
			points = 0;
		}
		
		public String getName(){
			return teamname;
		}
		public Integer getLev(){
			return lev;
		}
		public Integer getPoints(){
			return points;
		}
		public void addPoints(Integer num){
			points += num;
		}
	}

}
