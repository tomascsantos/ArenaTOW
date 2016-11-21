package io.github.TcFoxy.ArenaTOW.nms.v1_11_R1;


import org.bukkit.Color;
import org.bukkit.Location;
import org.bukkit.entity.Player;

import io.github.TcFoxy.ArenaTOW.Utils;
import io.github.TcFoxy.ArenaTOW.BattleArena.BattleArena;
import io.github.TcFoxy.ArenaTOW.BattleArena.objects.ArenaPlayer;
import io.github.TcFoxy.ArenaTOW.nms.v1_11_R1.interfaces.NMSConstants;
import net.minecraft.server.v1_11_R1.DamageSource;
import net.minecraft.server.v1_11_R1.PathfinderGoalNearestAttackableTarget;
import net.minecraft.server.v1_11_R1.World;

public class MyRedZombie extends MyEntityZombie {
	
	private String zombieteam = "Red";
	
	public MyRedZombie(World world) {
		super(world);
	}
	
	@Override
	protected void r(){
		super.r();
		this.targetSelector.a(2, new PathfinderGoalNearestAttackableTarget<MyBlueZombie>(this, MyBlueZombie.class, true));
		this.targetSelector.a(3, new PathfinderGoalNearestAttackableTarget<MyBlueGolem>(this, MyBlueGolem.class, true));
		this.targetSelector.a(4, new PathfinderGoalNearestAttackableTarget<MyBlueGuardian>(this, MyBlueGuardian.class, true));
	}
	
	@Override
	public void whereTo(Location directions) {
		super.whereTo(directions);
	}
	
	public boolean damageEntity(DamageSource damagesource, float f){
		if(damagesource.getEntity() != null){
			if(damagesource.getEntity().getClass().getName() == NMSConstants.entityPlayer){
				Player p = (Player) damagesource.getEntity().getBukkitEntity();
				ArenaPlayer ap = BattleArena.toArenaPlayer(p);
				String arenateam = ap.getTeam().getDisplayName();
				if(arenateam.equals(Utils.toSimpleColor(Color.RED))){
					return false;
				}else{ super.damageEntity(damagesource, f);}
			} else {super.damageEntity(damagesource, f);}
		}super.damageEntity(damagesource, f);
		return true;
	}

	public String getTeam(){
		return zombieteam;
	}
}
