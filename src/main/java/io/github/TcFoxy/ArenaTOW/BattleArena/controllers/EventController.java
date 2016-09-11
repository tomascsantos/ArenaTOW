package io.github.TcFoxy.ArenaTOW.BattleArena.controllers;



import java.util.HashMap;

import io.github.TcFoxy.ArenaTOW.BattleArena.objects.ArenaPlayer;
import io.github.TcFoxy.ArenaTOW.BattleArena.objects.teams.ArenaTeam;
import mc.alk.arena.competition.events.Event;
import mc.alk.arena.executors.EventExecutor;


public class EventController {
	static HashMap<String, Event> registeredEvents = new HashMap<String,Event>();
	static HashMap<String, EventExecutor> registeredExecutors = new HashMap<String,EventExecutor>();

	public EventController(){}

	public static Event insideEvent(ArenaPlayer p) {
		for (Event evt : registeredEvents.values()){
			ArenaTeam t = evt.getTeam(p);
			if (t != null)
				return evt;
		}
		return null;
	}

	public static void addEvent(Event event){
		registeredEvents.put(event.getName().toLowerCase(),event);
		registeredEvents.put(event.getCommand().toLowerCase(),event);
	}

	public void cancelAll() {
		for (Event evt : registeredEvents.values()){
			if (evt.isClosed())
				continue;
			evt.cancelEvent();
		}
	}

	public static void addEventExecutor(String name, String command, EventExecutor executor) {
		registeredExecutors.put(name.toLowerCase(), executor);
		registeredExecutors.put(command.toLowerCase(),executor);
	}

	public static EventExecutor getEventExecutor(Event event){
		return registeredExecutors.get(event.getName().toLowerCase());
	}

	public static EventExecutor getEventExecutor(String eventType){
		return registeredExecutors.get(eventType.toLowerCase());
	}

	public static boolean isEventType(String name) {
		return ParamController.getEventParamCopy(name) != null;
	}
}
