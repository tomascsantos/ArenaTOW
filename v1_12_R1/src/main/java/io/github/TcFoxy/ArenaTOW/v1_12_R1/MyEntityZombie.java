package io.github.TcFoxy.ArenaTOW.v1_12_R1;

import io.github.TcFoxy.ArenaTOW.API.TOWEntity;
import net.minecraft.server.v1_12_R1.DamageSource;
import net.minecraft.server.v1_12_R1.EntityZombie;
import net.minecraft.server.v1_12_R1.GenericAttributes;
import net.minecraft.server.v1_12_R1.World;
import org.bukkit.Location;

abstract class MyEntityZombie extends EntityZombie implements TOWEntity{

	public MyEntityZombie(World world) {
		super(world);
	}

	@Override
	public void n(){
		this.goalSelector.a(1, new MyPathfinderGoalMelee(this, 1.2D));
		this.targetSelector.a(1, new MyPathfinderGoalHurtByTarget(this, false, new Class[0]));
	}

	public void whereTo(Location directions){
		v1_12_R1_MobHandler.clearBehavior(goalSelector, targetSelector);
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

	@Override
	public boolean damageEntity(DamageSource d, float f) {
		if (d.getEntity() != null && (d.getEntity() instanceof TOWEntity)) {
			TOWEntity e = (TOWEntity) d.getEntity();
			if (e.isSameTeam(this)) {
				return false;
			}
		}
		return super.damageEntity(d, f);
	}
}


