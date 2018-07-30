package io.github.TcFoxy.ArenaTOW.nms.v1_13_R1;

import io.github.TcFoxy.ArenaTOW.nms.v1_13_R1.interfaces.NMSConstants;
import net.minecraft.server.v1_13_R1.EntityCreature;
import net.minecraft.server.v1_13_R1.EntityHuman;
import net.minecraft.server.v1_13_R1.EntityLiving;
import net.minecraft.server.v1_13_R1.PathfinderGoalMeleeAttack;

public class MyPathfinderGoalMelee extends PathfinderGoalMeleeAttack{

    boolean e;
    EntityCreature attacker;
	
	public MyPathfinderGoalMelee(EntityCreature arg0, double speed) {
		super(arg0, speed, false);
		this.e = false;
		this.attacker = arg0;
	}
	
	@Override
    public boolean b() {
        final EntityLiving goalTarget = this.a.getGoalTarget();
        if (goalTarget == null) {
            return false;
        }
        if (!goalTarget.isAlive()) {
            return false;
        }
        if (!this.e) {
            return !this.a.getNavigation().p();
        }
        if(NMSConstants.isSameTeam(attacker, goalTarget)){
        	return false;
        }
        return /*this.b.f(new BlockPosition(goalTarget)) &&*/
                        (!(goalTarget instanceof EntityHuman) ||
                        (!((EntityHuman)goalTarget).isSpectator() &&
                        !((EntityHuman)goalTarget).u()));
    }
	

}
