package io.github.TcFoxy.ArenaTOW.v1_13_R1;

import io.github.TcFoxy.ArenaTOW.API.TOWEntity;
import net.minecraft.server.v1_13_R1.EntityCreature;
import net.minecraft.server.v1_13_R1.EntityLiving;
import net.minecraft.server.v1_13_R1.PathfinderGoalHurtByTarget;

class MyPathfinderGoalHurtByTarget extends PathfinderGoalHurtByTarget{

	EntityCreature attacker;
	
	public MyPathfinderGoalHurtByTarget(EntityCreature entitycreature, boolean flag, Class<?>[] aclass) {
		super(entitycreature, flag, aclass);
		
		this.attacker = entitycreature;
	}
	
	@Override
    public boolean b() {
        final EntityLiving goalTarget = this.e.getLastDamager();
        if (goalTarget == null) {
            return false;
        }
        if (!goalTarget.isAlive()) {
            return false;
        }
        if(goalTarget instanceof TOWEntity && ((TOWEntity) this).isSameTeam((TOWEntity) goalTarget)) {
            return  false;
        }
        return true;
    }

}
