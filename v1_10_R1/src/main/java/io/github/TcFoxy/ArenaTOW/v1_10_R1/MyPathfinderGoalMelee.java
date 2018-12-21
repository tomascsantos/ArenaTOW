package io.github.TcFoxy.ArenaTOW.v1_10_R1;

import io.github.TcFoxy.ArenaTOW.API.TOWEntity;
import net.minecraft.server.v1_10_R1.*;

public class MyPathfinderGoalMelee extends PathfinderGoalMeleeAttack {

    private boolean e;
    private TOWEntity attacker;

    public MyPathfinderGoalMelee(EntityCreature arg0, double speed) {
        super(arg0, speed, false);
        this.e = false;
        this.attacker = (TOWEntity) arg0;
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
            return !this.b.getNavigation().n();
        }
        System.out.println("Melee Attack!");
        if (goalTarget instanceof TOWEntity && attacker.isSameTeam(goalTarget)) {
            return  false;
        }
        return this.b.f(new BlockPosition(goalTarget)) && (!(goalTarget instanceof EntityHuman) || (!((EntityHuman) goalTarget).isSpectator() && !((EntityHuman) goalTarget).z()));
    }


}
