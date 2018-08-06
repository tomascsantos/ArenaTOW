package io.github.TcFoxy.ArenaTOW.v1_13_R1;


import io.github.TcFoxy.ArenaTOW.API.TOWEntity;
import net.minecraft.server.v1_13_R1.World;

import java.awt.*;

class MyRedGuardian extends MyEntityGuardian implements TOWEntity {
		
	public MyRedGuardian(World paramWorld)
	{
		super(paramWorld);
		//this.targetSelector.a(2, new PathfinderGoalNearestAttackableTarget(this, EntityBlueZombie.class, true));
	}

	@Override
	public Color getTeam() {
	    return Color.red;
	}

//	public boolean damageEntity(DamageSource damagesource, float f){
//		if(damagesource.getEntity() != null){
//			if(damagesource.getEntity().getClass().getName() == NMSConstants.entityPlayer){
//				Player p = (Player) damagesource.getEntity().getBukkitEntity();
//				ArenaPlayer ap = BattleArena.toArenaPlayer(p);
//				String arenateam = ap.getTeam().getDisplayName();
//				if(arenateam.equals(Utils.toSimpleColor(Color.RED))){
//					return false;
//				}else{ super.damageEntity(damagesource, f);}
//			} else {super.damageEntity(damagesource, f);}
//		}super.damageEntity(damagesource, f);
//		return true;
//	}
}
