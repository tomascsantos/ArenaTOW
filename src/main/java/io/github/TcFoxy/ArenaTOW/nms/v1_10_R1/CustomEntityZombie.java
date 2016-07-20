package io.github.TcFoxy.ArenaTOW.nms.v1_10_R1;

import java.lang.reflect.Field;

import net.minecraft.server.v1_10_R1.EntityHuman;
import net.minecraft.server.v1_10_R1.EntityZombie;
import net.minecraft.server.v1_10_R1.GenericAttributes;
import net.minecraft.server.v1_10_R1.PathfinderGoalHurtByTarget;
import net.minecraft.server.v1_10_R1.PathfinderGoalMeleeAttack;
import net.minecraft.server.v1_10_R1.PathfinderGoalNearestAttackableTarget;
import net.minecraft.server.v1_10_R1.PathfinderGoalSelector;
import net.minecraft.server.v1_10_R1.World;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.craftbukkit.v1_10_R1.util.UnsafeList;
	 
	public class CustomEntityZombie extends EntityZombie {
	 
	//private Location location;
	//private CustomPathfindingGoalWalk myGoals;
	
	public CustomEntityZombie(World world) {
		super(world);

		removePaths();
		a(0.6F, 1.95F); // the bounding box, if you set it to 0 he dies.

		getAttributeInstance(GenericAttributes.FOLLOW_RANGE).setValue(50.0D);//foloow range
	}
	
	
	private void removePaths() {
		try {
			Field bField = PathfinderGoalSelector.class.getDeclaredField("b");
			bField.setAccessible(true);
			Field cField = PathfinderGoalSelector.class.getDeclaredField("c");
			cField.setAccessible(true);
			bField.set(goalSelector, new UnsafeList<PathfinderGoalSelector>());
			bField.set(targetSelector, new UnsafeList<PathfinderGoalSelector>());
			cField.set(goalSelector, new UnsafeList<PathfinderGoalSelector>());
			cField.set(targetSelector, new UnsafeList<PathfinderGoalSelector>());
		} catch (Exception exc) {
			exc.printStackTrace();
			Bukkit.broadcastMessage("catchstatement broke i thinkl");
		}
	}

	public void whereTo(Location directions){
		removePaths();
		this.goalSelector.a(2, new PathfinderGoalMeleeAttack(this, 1.3D, false));
		this.targetSelector.a(2, new PathfinderGoalNearestAttackableTarget(this, EntityHuman.class, true));
		this.goalSelector.a(3, new CustomPathfindingGoalWalk(this, 1.7D, directions));
	    this.targetSelector.a(1, new PathfinderGoalHurtByTarget(this, false, new Class[0]));
	}
}

