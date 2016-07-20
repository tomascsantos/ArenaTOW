package io.github.TcFoxy.ArenaTOW.playerModifiers.ArenaClass.Powers;

import io.github.TcFoxy.ArenaTOW.ArenaTOW;
import io.github.TcFoxy.ArenaTOW.Utils;
import io.github.TcFoxy.ArenaTOW.nms.v1_8ish.CustomEntityZombie;
import io.github.TcFoxy.ArenaTOW.nms.v1_8ish.interfaces.NMSUtils;
import io.github.TcFoxy.ArenaTOW.playerModifiers.ArenaClass.ArenaClassController;

import java.util.ArrayList;

import mc.alk.arena.BattleArena;
import mc.alk.arena.objects.teams.ArenaTeam;
import net.minecraft.server.v1_10_R1.AttributeInstance;
import net.minecraft.server.v1_10_R1.AttributeModifier;
import net.minecraft.server.v1_10_R1.EnumItemSlot;
import net.minecraft.server.v1_10_R1.GenericAttributes;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;
import org.bukkit.event.player.PlayerInteractEvent;

public class Necromancer {
	
	ArenaClassController ACC;
	
	public Necromancer(ArenaClassController controller){
		ACC = controller;
	}

	public static ArrayList<CustomEntityZombie> necro = new ArrayList<CustomEntityZombie>();
	
	public void mainEffect(PlayerInteractEvent event){
		Player p = event.getPlayer();
		if(ACC.checkCooldown(p, 25)){
			Block b = Utils.getTargetBlock(p, 15);
			Location loc = b.getLocation();
			Double x = loc.getX();
			Double y = loc.getY()+2;
			Double z = loc.getZ();
			ArenaTeam team = BattleArena.toArenaPlayer(p).getTeam();
			CustomEntityZombie zombie = null;
			for(int i =0; i<3; i++){
				if(team.getDisplayName() == "Red"){
					zombie = NMSUtils.spawnRedZombie(loc.getWorld(), x, y, z);
				}else if (team.getDisplayName() == "Blue"){
					zombie = NMSUtils.spawnBlueZombie(loc.getWorld(), x, y, z);
				}
				AttributeInstance attributes = zombie.getAttributeInstance(GenericAttributes.MOVEMENT_SPEED);
				AttributeModifier modifier = new AttributeModifier("ArenaTOW-speed", 1.3d, 1);
				attributes.b(modifier);
				attributes.a(modifier);
				AttributeInstance attributes1 = zombie.getAttributeInstance(GenericAttributes.maxHealth);
				AttributeModifier modifier1 = new AttributeModifier("ArenaTOW-health", 2d, 1);
				attributes1.b(modifier1);
				attributes1.a(modifier1);
				zombie.setCustomName(p.getName() + "'s zombie");
				zombie.setCustomNameVisible(true);
				zombie.setHealth(40);
				zombie.setEquipment(EnumItemSlot.HEAD, Utils.makeMobHelm("Black"));
				necro.add(zombie);
				killnecros(necro, 20);
			}
		}
	}
	public void killnecros(final ArrayList<CustomEntityZombie> necro, Integer time){
		Bukkit.getScheduler().runTaskLater(ArenaTOW.getSelf(), new Runnable() {
			@Override
			public void run() {
				for(CustomEntityZombie e:necro){
					e.setHealth(0);
				}
			}

		}, time*Utils.TPS);
	}
	public static void instakillnecros(ArrayList<CustomEntityZombie>necro){
		for(CustomEntityZombie e:necro){
			e.setHealth(0);
		}
	}
}
