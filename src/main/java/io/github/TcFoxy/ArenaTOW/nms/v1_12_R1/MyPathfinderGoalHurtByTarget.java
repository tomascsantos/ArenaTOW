package io.github.TcFoxy.ArenaTOW.nms.v1_12_R1;

import org.bukkit.Bukkit;

import io.github.TcFoxy.ArenaTOW.nms.v1_12_R1.interfaces.NMSConstants;
import net.minecraft.server.v1_12_R1.EntityCreature;
import net.minecraft.server.v1_12_R1.EntityLiving;
import net.minecraft.server.v1_12_R1.PathfinderGoalHurtByTarget;

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
        	Bukkit.broadcastMessage("goal hurt by taget is stopping it!");
        	return false;
        }
        return true;
    }

}
