package io.github.TcFoxy.ArenaTOW.BattleArena.controllers;



import org.bukkit.entity.Player;

import io.github.TcFoxy.ArenaTOW.BattleArena.objects.ArenaPlayer;
import io.github.TcFoxy.ArenaTOW.BattleArena.objects.teams.ArenaTeam;
import mc.alk.arena.events.prizes.ArenaPrizeEvent;
import mc.alk.arena.events.prizes.Reward;
import mc.alk.arena.util.EffectUtil;
import mc.alk.arena.util.ExpUtil;
import mc.alk.arena.util.InventoryUtil;


public class RewardController {

	ArenaPrizeEvent event;
	PlayerStoreController psc;

	public RewardController(ArenaPrizeEvent event,PlayerStoreController playerStoreController) {
		this.event = event;
		this.psc = playerStoreController;
	}


	public void giveRewards() {
		for (ArenaTeam t: event.getTeams()){
			for (ArenaPlayer player: t.getPlayers()){
				if (t.hasLeft(player))
					continue;
				Player p = player.getPlayer();
				if (event.getMoney() != null && MoneyController.hasEconomy()){
					MoneyController.add(p.getName(), event.getMoney());}
				if (!p.isOnline())
					continue;
				if (event.getExp() != null){
					ExpUtil.giveExperience(p, event.getExp());}
				if (event.getEffects() != null){
					EffectUtil.enchantPlayer(p, event.getEffects());}
				if (event.getItems() != null){
					InventoryUtil.addItemsToInventory(p, event.getItems(), true);}
			}
			if (event.getRewards() != null){
				for (Reward reward : event.getRewards()){
					reward.reward(t);}
			}
		}

	}

}
