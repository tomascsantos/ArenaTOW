package io.github.TcFoxy.ArenaTOW.nms.v1_13_R1;

import net.minecraft.server.v1_13_R1.EntityCreature;
import net.minecraft.server.v1_13_R1.EntityLiving;
import net.minecraft.server.v1_13_R1.PathfinderGoalHurtByTarget;

public class MyPathfinderGoalHurtByTarget extends PathfinderGoalHurtByTarget{

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
        if(NMSConstants.isSameTeam(attacker, goalTarget)){
        	return false;
        }
        return true;
    }

}
