package io.github.TcFoxy.ArenaTOW.BattleArena.events.players;

import java.util.List;



import org.bukkit.inventory.ItemStack;

import io.github.TcFoxy.ArenaTOW.BattleArena.events.CompetitionEvent;
import io.github.TcFoxy.ArenaTOW.BattleArena.objects.ArenaClass;

public class ArenaPlayerClassSelectedEvent extends CompetitionEvent{
	ArenaClass arenaClass;
	List<ItemStack> items = null;
	public ArenaPlayerClassSelectedEvent(ArenaClass arenaClass) {
		this.arenaClass = arenaClass;
	}
	public ArenaClass getArenaClass() {
		return arenaClass;
	}
	public void setArenaClass(ArenaClass arenaClass) {
		this.arenaClass = arenaClass;
	}
	public List<ItemStack> getItems() {
		return items;
	}
	public void setItems(List<ItemStack> items) {
		this.items = items;
	}
}
