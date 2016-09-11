package io.github.TcFoxy.ArenaTOW.BattleArena.events;

import io.github.TcFoxy.ArenaTOW.BattleArena.competition.Competition;


public class CompetitionEvent extends BAEvent {
	protected Competition competition;

	public void setCompetition(Competition competition){
		this.competition = competition;
	}

	public Competition getCompetition(){
		return competition;
	}
}
