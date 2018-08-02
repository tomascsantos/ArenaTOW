package io.github.TcFoxy.ArenaTOW.NMS.v1_12_R1;

import io.github.TcFoxy.ArenaTOW.nms.v1_13_R1.PathfinderGoalGolemFireball;
import net.minecraft.server.v1_13_R1.*;

class MyEntityGolem extends EntityIronGolem{
	Village a;
	public MyEntityGolem(World world){
		super(world);

		this.fireProof = true;
	}

	@Override
	protected void n(){
		//put pathfinders here.
		this.goalSelector.a(4, new PathfinderGoalGolemFireball(this));
		this.goalSelector.a(5, new PathfinderGoalMeleeAttack(this, 1.3D, false));
		this.targetSelector.a(5, new PathfinderGoalNearestAttackableTarget<EntityHuman>(this, EntityHuman.class, true));
	    this.goalSelector.a(8, new PathfinderGoalLookAtPlayer(this, EntityHuman.class, 8.0F));
	    this.goalSelector.a(8, new PathfinderGoalRandomLookaround(this));
	}
	
	@Override
	public void move(EnumMoveType type, double d0, double d1, double d2){
	}

	@Override
	protected void initAttributes(){
		super.initAttributes();
		getAttributeInstance(GenericAttributes.maxHealth).setValue(300.0D);
		getAttributeInstance(GenericAttributes.FOLLOW_RANGE).setValue(7.0D);//sight
	}

}
