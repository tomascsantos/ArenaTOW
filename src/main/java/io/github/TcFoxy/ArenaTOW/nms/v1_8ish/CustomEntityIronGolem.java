package io.github.TcFoxy.ArenaTOW.nms.v1_8ish;

import java.lang.reflect.Field;
import java.util.LinkedHashSet;

import net.minecraft.server.v1_10_R1.EntityHuman;
import net.minecraft.server.v1_10_R1.EntityIronGolem;
import net.minecraft.server.v1_10_R1.GenericAttributes;
import net.minecraft.server.v1_10_R1.PathfinderGoalMeleeAttack;
import net.minecraft.server.v1_10_R1.PathfinderGoalNearestAttackableTarget;
import net.minecraft.server.v1_10_R1.PathfinderGoalSelector;
import net.minecraft.server.v1_10_R1.Village;
import net.minecraft.server.v1_10_R1.World;

import org.bukkit.Bukkit;
import org.bukkit.craftbukkit.v1_10_R1.util.UnsafeList;

public class CustomEntityIronGolem extends EntityIronGolem{
	Village a;
	  public CustomEntityIronGolem(World world){
		  
	    super(world);
	    
	    try {//This "Try" thing deletes all the old pathfinding goals.
	    	Field bField = PathfinderGoalSelector.class.getDeclaredField("b");
	    	bField.setAccessible(true);
	    	Field cField = PathfinderGoalSelector.class.getDeclaredField("c");
	    	cField.setAccessible(true);
	    	bField.set(goalSelector, new LinkedHashSet<PathfinderGoalSelector>());
	    	bField.set(targetSelector, new LinkedHashSet<PathfinderGoalSelector>());
	    	cField.set(goalSelector, new LinkedHashSet<PathfinderGoalSelector>());
	    	cField.set(targetSelector, new LinkedHashSet<PathfinderGoalSelector>());
	    	} catch (Exception exc) {
	    	exc.printStackTrace();
	    	Bukkit.broadcastMessage("catchstatement customirongolem goals/targets");
	    	// This means that the name of one of the fields changed names or declaration and will have to be re-examined.
	    	}
	    a(1.4F, 2.9F);
	    this.fireProof = true;
		this.goalSelector.a(4, new PathfinderGoalGolemFireball(this));
		this.goalSelector.a(5, new PathfinderGoalMeleeAttack(this, 1.3D, false));
		this.targetSelector.a(5, new PathfinderGoalNearestAttackableTarget(this, EntityHuman.class, true));
	  }
	  
	  @Override
	  public void move(double d0, double d1, double d2){
	  }


	protected void initAttributes(){
	    super.initAttributes();
	    getAttributeInstance(GenericAttributes.maxHealth).setValue(300.0D);
	    getAttributeInstance(GenericAttributes.MOVEMENT_SPEED).setValue(0.0D);//movement speed
	    getAttributeInstance(GenericAttributes.c).setValue(500.0D);//KnockBack resist
	    getAttributeInstance(GenericAttributes.FOLLOW_RANGE).setValue(7.0D);//sight
	  }
	
}
