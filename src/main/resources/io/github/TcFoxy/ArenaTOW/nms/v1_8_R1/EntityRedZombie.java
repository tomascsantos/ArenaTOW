package io.github.TcFoxy.ArenaTOW.nms.v1_8_R1;

import mc.alk.arena.BattleArena;
import mc.alk.arena.objects.ArenaPlayer;
import net.minecraft.server.v1_8_R1.DamageSource;
import net.minecraft.server.v1_8_R1.EntityHuman;
import net.minecraft.server.v1_8_R1.GenericAttributes;
import net.minecraft.server.v1_8_R1.PathfinderGoalMeleeAttack;
import net.minecraft.server.v1_8_R1.PathfinderGoalNearestAttackableTarget;
import net.minecraft.server.v1_8_R1.World;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.entity.Player;

public class EntityRedZombie extends CustomEntityZombie {
	
	private String zombieteam = "Red";
	
	public EntityRedZombie(World world) {
		super(world);
		whereTo(null);
	}
	
	@Override
	public void whereTo(Location directions) {
		super.whereTo(directions);
		this.goalSelector.a(2, new PathfinderGoalMeleeAttack(this, EntityBlueZombie.class, 1.0D, false));
		this.goalSelector.a(2, new PathfinderGoalMeleeAttack(this, CustomBlueGolem.class, 1.0D, false));
		this.goalSelector.a(2, new PathfinderGoalMeleeAttack(this, CustomBlueGuardian.class, 1.0D, false));
		this.targetSelector.a(2, new PathfinderGoalNearestCustomTarget(this, EntityBlueZombie.class, true, 7));
		this.targetSelector.a(2, new PathfinderGoalNearestCustomTarget(this, CustomBlueGolem.class, true, 7));
		this.targetSelector.a(2, new PathfinderGoalNearestCustomTarget(this, CustomBlueGuardian.class, true, 7));
	}

	public boolean damageEntity(DamageSource damagesource, float f){
		if(damagesource.getEntity() != null){
			if(damagesource.getEntity().getClass().getName() == "net.minecraft.server.v1_8_R1.EntityPlayer"){
				Player p = (Player) damagesource.getEntity().getBukkitEntity();
				ArenaPlayer ap = BattleArena.toArenaPlayer(p);
				String arenateam = ap.getTeam().getDisplayName();
				if(arenateam == "Red"){
					return false;
				}else{
					super.damageEntity(damagesource, f);
				}
			} else {
				super.damageEntity(damagesource, f);
			}
		}
		super.damageEntity(damagesource, f);
		return true;
		}

	public String getTeam(){
		return zombieteam;
	}
}
