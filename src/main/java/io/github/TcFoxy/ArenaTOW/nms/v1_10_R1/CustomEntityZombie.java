package io.github.TcFoxy.ArenaTOW.nms.v1_10_R1;

import java.lang.reflect.Field;
import java.util.LinkedHashSet;

import net.minecraft.server.v1_10_R1.EntityZombie;
import net.minecraft.server.v1_10_R1.GenericAttributes;
import net.minecraft.server.v1_10_R1.PathfinderGoalSelector;
import net.minecraft.server.v1_10_R1.World;

import org.bukkit.Location;

public class CustomEntityZombie extends EntityZombie {

	public CustomEntityZombie(World world) {
		super(world);

		getAttributeInstance(GenericAttributes.FOLLOW_RANGE).setValue(50.0D);//foloow range
	}

	@Override
	protected void r(){
		//		this.goalSelector.a(2, new PathfinderGoalMeleeAttack(this, 1.3D, false));
		//		this.targetSelector.a(2, new PathfinderGoalNearestAttackableTarget<EntityHuman>(this, EntityHuman.class, true));
		//		this.targetSelector.a(1, new PathfinderGoalHurtByTarget(this, false, new Class[0]));
	}

	public void whereTo(Location directions){
		clearGoals();
		r();
		this.goalSelector.a(3, new CustomPathfindingGoalWalk(this, 1, directions));
	}

	@SuppressWarnings("rawtypes")
	public void clearGoals(){
		LinkedHashSet goalB = (LinkedHashSet)getPrivateField("b", PathfinderGoalSelector.class, goalSelector); goalB.clear();
		LinkedHashSet goalC = (LinkedHashSet)getPrivateField("c", PathfinderGoalSelector.class, goalSelector); goalC.clear();
		LinkedHashSet targetB = (LinkedHashSet)getPrivateField("b", PathfinderGoalSelector.class, targetSelector); targetB.clear();
		LinkedHashSet targetC = (LinkedHashSet)getPrivateField("c", PathfinderGoalSelector.class, targetSelector); targetC.clear();
	}

	public static Object getPrivateField(String fieldName, Class<PathfinderGoalSelector> clazz, Object object){
		Field field;
		Object o = null;
		try {
			field = clazz.getDeclaredField(fieldName);

			field.setAccessible(true);

			o = field.get(object);
		}
		catch(NoSuchFieldException e){
			e.printStackTrace();
		}
		catch(IllegalAccessException e){
			e.printStackTrace();
		}

		return o;
	}
}


