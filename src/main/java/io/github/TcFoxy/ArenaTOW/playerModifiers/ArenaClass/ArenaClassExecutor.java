package io.github.TcFoxy.ArenaTOW.playerModifiers.ArenaClass;import io.github.TcFoxy.ArenaTOW.ArenaTOW;
import io.github.TcFoxy.ArenaTOW.playerModifiers.ArenaClass.Powers.Archmage;
import io.github.TcFoxy.ArenaTOW.playerModifiers.ArenaClass.Powers.Bandit;
import io.github.TcFoxy.ArenaTOW.playerModifiers.ArenaClass.Powers.Berserker;
import io.github.TcFoxy.ArenaTOW.playerModifiers.ArenaClass.Powers.DragonBeast;
import io.github.TcFoxy.ArenaTOW.playerModifiers.ArenaClass.Powers.Necromancer;
import io.github.TcFoxy.ArenaTOW.playerModifiers.ArenaClass.Powers.Ninja;
import io.github.TcFoxy.ArenaTOW.playerModifiers.ArenaClass.Powers.Titan;
import io.github.TcFoxy.ArenaTOW.playerModifiers.ArenaClass.Powers.Wizard;
import io.github.TcFoxy.ArenaTOW.playerModifiers.ArenaClass.Powers.Wraith;

import java.util.ArrayList;

import mc.alk.arena.BattleArena;
import mc.alk.arena.competition.match.Match;
import mc.alk.arena.objects.ArenaPlayer;
import mc.alk.arena.objects.arenas.Arena;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.plugin.PluginManager;

public class ArenaClassExecutor {

	Arena arena;
	public ArenaClassController ACC;
	
	final String Bandit = "[ArenaClass Bandit]";
	final String Wizard = "[ArenaClass Wizard]";
	final String Berserk = "[ArenaClass Berserk]";
	final String Ninja = "[ArenaClass Ninja]";
	final String Necromancer = "[ArenaClass Necromancer]";
	final String Titan = "[ArenaClass Titan]";
	final String Wraith = "[ArenaClass Wraith]";
	final String Archmage = "[ArenaClass Archmage]";
	final String Dragonbeast = "[ArenaClass Dragonbeast]";
	
	Archmage archmage;
	Bandit bandit;
	Berserker berserker;
	DragonBeast dragonbeast;
	Necromancer necromancer;
	Ninja ninja;
	Titan titan;
	Wizard wizard;
	Wraith wraith;
	
	
	public ArenaClassExecutor(Arena arena, ArenaTOW plugin, Match match){
		this.arena = arena;
		ACC = new ArenaClassController(match, plugin);
		Bukkit.getServer().getPluginManager().registerEvents(ACC, plugin);
		ACC.xpCooldown();
		setupArenaPowers();
		registerPowerListeners(plugin);
		
		for(ArenaPlayer ap: arena.getMatch().getPlayers()){
			setClassStandards(ap);
		}
	}
	
	private void setupArenaPowers(){
		archmage = new Archmage(ACC);
		bandit = new Bandit(ACC);
		berserker = new Berserker(ACC);
		dragonbeast = new DragonBeast(ACC);
		necromancer = new Necromancer(ACC);
		ninja = new Ninja(ACC);
		titan = new Titan(ACC);
		wizard = new Wizard(ACC);
		wraith = new Wraith(ACC);
	}
	
	private void registerPowerListeners(ArenaTOW tow){
		PluginManager PM = Bukkit.getServer().getPluginManager();
		PM.registerEvents(archmage, tow);
		PM.registerEvents(bandit, tow);
		PM.registerEvents(dragonbeast, tow);
		PM.registerEvents(ninja, tow);
		PM.registerEvents(wraith, tow);
	}
	
	
	public void determineEffect(PlayerInteractEvent event){
		Player p = event.getPlayer();
		ArenaPlayer ap = BattleArena.toArenaPlayer(p);
		String curclass = ap.getCurrentClass().toString();
		
		if(event.getItem().getType() == Material.NETHER_STAR){
			ACC.Hearthstone(event);
			return;
		}
		
		switch(curclass){
		case Bandit:
			bandit.mainEffect(event);
			break;
		case Wizard:
			wizard.mainEffect(event);
			break;
		case Berserk:
			berserker.mainEffect(event);
			break;
		case Ninja:
			ninja.mainEffect(event);
			break;
		case Necromancer:
			necromancer.mainEffect(event);
			break;
		case Titan:
			titan.mainEffect(event);
			break;
		case Wraith:
			wraith.mainEffect(event);
			break;
		case Archmage:
			archmage.mainEffect(event);
			break;
		case Dragonbeast:
			dragonbeast.mainEffect(event);
			break;
		default:
			return;
		}
	}
	
	
	public void giveStarterItems(Player p){
		
		ItemStack sword = new ItemStack(Material.WOOD_SWORD);
		setName(sword, "Clunky Starter Weapon");
		setLoreNoBreak(sword, ChatColor.YELLOW + "Use this like any other sword... to lop off heads!");
		
		ItemStack axe = new ItemStack(Material.WOOD_AXE);
		setName(axe, "Ability Caster");
		setLoreNoBreak(axe, ChatColor.DARK_RED + "Right CLick to use Abilities!");
		
		ItemStack hearthstone = new ItemStack(Material.NETHER_STAR);
		setName(hearthstone, "HearthStone");
		setLoreNoBreak(hearthstone, ChatColor.DARK_PURPLE + "Right Click to teleport home!");
		
		p.getInventory().setItem(0, sword);
		p.getInventory().setItem(1, axe);
		p.getInventory().setItem(8, hearthstone);
	}
	
	
	public void setClassStandards(ArenaPlayer ap){
		Player p = ap.getPlayer();
		String curclass = ap.getCurrentClass().toString();
				
		ItemStack boots;
		ItemStack legs;
		ItemStack chest;
		ItemStack helm;
		
		switch(curclass){
		case Bandit:
			boots = new ItemStack(Material.LEATHER_BOOTS);
			legs = new ItemStack(Material.LEATHER_LEGGINGS);
			chest = new ItemStack(Material.LEATHER_CHESTPLATE);
			helm = new ItemStack(Material.LEATHER_HELMET);
			
			p.getInventory().setBoots(boots);
			p.getInventory().setLeggings(legs);
			p.getInventory().setChestplate(chest);
			p.getInventory().setHelmet(helm);

			giveStarterItems(p);
			
			break;
			
			
		case Wizard:
			boots = new ItemStack(Material.LEATHER_BOOTS);
			legs = new ItemStack(Material.LEATHER_LEGGINGS);
			chest = new ItemStack(Material.LEATHER_CHESTPLATE);
			helm = new ItemStack(Material.LEATHER_HELMET);
			
			p.getInventory().setBoots(boots);
			p.getInventory().setLeggings(legs);
			p.getInventory().setChestplate(chest);
			p.getInventory().setHelmet(helm);

			giveStarterItems(p);
			break;
			
			
		case Berserk:
			
			boots = new ItemStack(Material.IRON_BOOTS);
			legs = new ItemStack(Material.LEATHER_LEGGINGS);
			chest = new ItemStack(Material.LEATHER_CHESTPLATE);
			helm = new ItemStack(Material.IRON_HELMET);
			
			p.getInventory().setBoots(boots);
			p.getInventory().setLeggings(legs);
			p.getInventory().setChestplate(chest);
			p.getInventory().setHelmet(helm);

			giveStarterItems(p);
			break;
			
			
		case Ninja:
			boots = new ItemStack(Material.LEATHER_BOOTS);
			legs = new ItemStack(Material.LEATHER_LEGGINGS);
			chest = new ItemStack(Material.IRON_CHESTPLATE);
			helm = new ItemStack(Material.LEATHER_HELMET);
			
			p.getInventory().setBoots(boots);
			p.getInventory().setLeggings(legs);
			p.getInventory().setChestplate(chest);
			p.getInventory().setHelmet(helm);

			giveStarterItems(p);
			break;
			
			
		case Necromancer:
			boots = new ItemStack(Material.LEATHER_BOOTS);
			legs = new ItemStack(Material.IRON_LEGGINGS);
			chest = new ItemStack(Material.LEATHER_CHESTPLATE);
			helm = new ItemStack(Material.IRON_HELMET);
			
			p.getInventory().setBoots(boots);
			p.getInventory().setLeggings(legs);
			p.getInventory().setChestplate(chest);
			p.getInventory().setHelmet(helm);

			giveStarterItems(p);
			break;
			
			
		case Titan:
			boots = new ItemStack(Material.IRON_BOOTS);
			legs = new ItemStack(Material.IRON_LEGGINGS);
			chest = new ItemStack(Material.IRON_CHESTPLATE);
			helm = new ItemStack(Material.IRON_HELMET);
			
			p.getInventory().setBoots(boots);
			p.getInventory().setLeggings(legs);
			p.getInventory().setChestplate(chest);
			p.getInventory().setHelmet(helm);

			giveStarterItems(p);
			break;
			
			
		case Wraith:
			boots = new ItemStack(Material.LEATHER_BOOTS);
			legs = new ItemStack(Material.LEATHER_LEGGINGS);
			chest = new ItemStack(Material.LEATHER_CHESTPLATE);
			helm = new ItemStack(Material.LEATHER_HELMET);
			
			p.getInventory().setBoots(boots);
			p.getInventory().setLeggings(legs);
			p.getInventory().setChestplate(chest);
			p.getInventory().setHelmet(helm);

			giveStarterItems(p);
			break;
			
			
		case Archmage:
			boots = new ItemStack(Material.LEATHER_BOOTS);
			legs = new ItemStack(Material.LEATHER_LEGGINGS);
			chest = new ItemStack(Material.LEATHER_CHESTPLATE);
			helm = new ItemStack(Material.LEATHER_HELMET);
			
			p.getInventory().setBoots(boots);
			p.getInventory().setLeggings(legs);
			p.getInventory().setChestplate(chest);
			p.getInventory().setHelmet(helm);

			giveStarterItems(p);
			break;
			
			
		case Dragonbeast:
			boots = new ItemStack(Material.DIAMOND_BOOTS);
			legs = new ItemStack(Material.IRON_LEGGINGS);
			chest = new ItemStack(Material.IRON_CHESTPLATE);
			helm = new ItemStack(Material.DIAMOND_HELMET);
			
			p.getInventory().setBoots(boots);
			p.getInventory().setLeggings(legs);
			p.getInventory().setChestplate(chest);
			p.getInventory().setHelmet(helm);

			giveStarterItems(p);
		}
	}
	
	
	public ItemStack setLoreNoBreak(ItemStack is, String string){
		ArrayList<String> lore = new ArrayList<String>();
		lore.add(string);
		ItemMeta meta = is.getItemMeta();
		meta.setLore(lore);
		meta.spigot().setUnbreakable(true);
		meta.addItemFlags(ItemFlag.HIDE_UNBREAKABLE);
		is.setItemMeta(meta);
		return is;
	}
	
	public ItemStack setName(ItemStack is, String name){
        ItemMeta m = is.getItemMeta();
        m.setDisplayName(name);
        is.setItemMeta(m);
        return is;
    }
}
