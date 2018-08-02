package io.github.TcFoxy.ArenaTOW.nms.v1_13_R1;

import org.bukkit.Location;

import net.minecraft.server.v1_13_R1.EntityZombie;
import net.minecraft.server.v1_13_R1.GenericAttributes;
import net.minecraft.server.v1_13_R1.World;

class MyEntityZombie extends EntityZombie {

	public MyEntityZombie(World world) {
		super(world);
	}

	@Override
	protected void n(){
		this.goalSelector.a(1, new MyPathfinderGoalMelee(this, 1.2D));
		this.targetSelector.a(1, new MyPathfinderGoalHurtByTarget(this, false, new Class[0]));
	}

	public void whereTo(Location directions){
		NMSUtils.clearBehavior(goalSelector, targetSelector);
		n();
		this.goalSelector.a(6, new MyPathfindingGoalWalk(this, 1.2D, directions));
	}
	
	@Override
	public void initAttributes()
	{
		super.initAttributes();
		getAttributeInstance(GenericAttributes.ATTACK_DAMAGE).setValue(3.0D);
		getAttributeInstance(GenericAttributes.FOLLOW_RANGE).setValue(16.0D);
		getAttributeInstance(GenericAttributes.maxHealth).setValue(30.0D);
	}
}


