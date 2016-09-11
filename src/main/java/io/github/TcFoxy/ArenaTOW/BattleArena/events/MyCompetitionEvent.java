package io.github.TcFoxy.ArenaTOW.BattleArena.events;

import io.github.TcFoxy.ArenaTOW.BattleArena.competition.MyCompetition;


public class MyCompetitionEvent extends MyBAEvent {
	protected MyCompetition competition;

	public void setCompetition(MyCompetition competition){
		this.competition = competition;
	}

	public MyCompetition getCompetition(){
		return competition;
	}
}
