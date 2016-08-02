package io.github.TcFoxy.ArenaTOW.nms.v1_10_R1;

import java.lang.reflect.Field;
import java.util.LinkedHashSet;

import net.minecraft.server.v1_10_R1.EntityZombie;
import net.minecraft.server.v1_10_R1.GenericAttributes;
import net.minecraft.server.v1_10_R1.PathfinderGoalSelector;
import net.minecraft.server.v1_10_R1.World;

import org.bukkit.Location;

public class MyEntityZombie extends EntityZombie {

	public MyEntityZombie(World world) {
		super(world);

		getAttributeInstance(GenericAttributes.FOLLOW_RANGE).setValue(10.0D);//foloow range
		fireProof = true;
	}

	@Override
	protected void r(){
		this.goalSelector.a(1, new MyPathfinderGoalMelee(this, 1.2D));
		//this.targetSelector.a(5, new PathfinderGoalNearestAttackableTarget<EntityHuman>(this, EntityHuman.class, true));
		this.targetSelector.a(1, new MyPathfinderGoalHurtByTarget(this, false, new Class[0]));
	}

	public void whereTo(Location directions){
		clearWalk();
		r();
		this.goalSelector.a(6, new MyPathfindingGoalWalk(this, 1.2D, directions));
	}

	@SuppressWarnings("rawtypes")
	public void clearWalk(){
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


