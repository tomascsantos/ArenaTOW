package io.github.TcFoxy.ArenaTOW.v1_12_R1;

import io.github.TcFoxy.ArenaTOW.API.TOWEntity;
import net.minecraft.server.v1_12_R1.EntityCreature;
import net.minecraft.server.v1_12_R1.EntityHuman;
import net.minecraft.server.v1_12_R1.EntityLiving;
import net.minecraft.server.v1_12_R1.PathfinderGoalMeleeAttack;

class MyPathfinderGoalMelee extends PathfinderGoalMeleeAttack{

    boolean e;
    EntityCreature attacker;
	
	public MyPathfinderGoalMelee(EntityCreature arg0, double speed) {
		super(arg0, speed, false);
		this.e = false;
		this.attacker = arg0;
	}
	
	@Override
    public boolean b() {
        final EntityLiving goalTarget = this.b.getGoalTarget();
        if (goalTarget == null) {
            return false;
        }
        if (!goalTarget.isAlive()) {
            return false;
        }
        if (!this.e) {
            return !this.b.getNavigation().o();
        }
        if(goalTarget instanceof TOWEntity && ((TOWEntity) this).isSameTeam((TOWEntity) goalTarget)) {
            return  false;
        }
        return ((goalTarget instanceof EntityHuman)) &&
                ((((EntityHuman)goalTarget).isSpectator()) ||
                        (((EntityHuman)goalTarget).z()));
    }
	

}
