package io.github.TcFoxy.ArenaTOW.nms.v1_8_R1;

import net.minecraft.server.v1_8_R1.EntityCreature;
import net.minecraft.server.v1_8_R1.PathfinderGoalNearestAttackableTarget;

public class PathfinderGoalNearestCustomTarget extends
		PathfinderGoalNearestAttackableTarget {

	double sight;
	
	public PathfinderGoalNearestCustomTarget(EntityCreature entitycreature, Class oclass, boolean flag, double sight ) {
		super(entitycreature, oclass, flag);
		this.sight = sight;
	}
	
	@Override
	protected double f() {
		return sight;
	}

}
