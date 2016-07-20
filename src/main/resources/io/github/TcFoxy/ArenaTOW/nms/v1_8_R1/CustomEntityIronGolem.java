package io.github.TcFoxy.ArenaTOW.nms.v1_8_R1;

import java.lang.reflect.Field;

import org.bukkit.Bukkit;
import org.bukkit.craftbukkit.v1_8_R1.util.UnsafeList;

import net.minecraft.server.v1_8_R1.DamageSource;
import net.minecraft.server.v1_8_R1.EntityHuman;
import net.minecraft.server.v1_8_R1.EntityInsentient;
import net.minecraft.server.v1_8_R1.EntityIronGolem;
import net.minecraft.server.v1_8_R1.GenericAttributes;
import net.minecraft.server.v1_8_R1.Navigation;
import net.minecraft.server.v1_8_R1.PathfinderGoalDefendVillage;
import net.minecraft.server.v1_8_R1.PathfinderGoalHurtByTarget;
import net.minecraft.server.v1_8_R1.PathfinderGoalLookAtPlayer;
import net.minecraft.server.v1_8_R1.PathfinderGoalMeleeAttack;
import net.minecraft.server.v1_8_R1.PathfinderGoalMoveThroughVillage;
import net.minecraft.server.v1_8_R1.PathfinderGoalMoveTowardsRestriction;
import net.minecraft.server.v1_8_R1.PathfinderGoalMoveTowardsTarget;
import net.minecraft.server.v1_8_R1.PathfinderGoalNearestAttackableTarget;
import net.minecraft.server.v1_8_R1.PathfinderGoalOfferFlower;
import net.minecraft.server.v1_8_R1.PathfinderGoalRandomLookaround;
import net.minecraft.server.v1_8_R1.PathfinderGoalRandomStroll;
import net.minecraft.server.v1_8_R1.PathfinderGoalSelector;
import net.minecraft.server.v1_8_R1.Village;
import net.minecraft.server.v1_8_R1.World;

public class CustomEntityIronGolem extends EntityIronGolem{
	private int b;
	  Village a;
	  private int c;
	  private int bk;

	  public CustomEntityIronGolem(World world){
		  
	    super(world);
	    
	    try {//This "Try" thing deletes all the old pathfinding goals.
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
	    	Bukkit.broadcastMessage("catchstatement customirongolem goals/targets");
	    	// This means that the name of one of the fields changed names or declaration and will have to be re-examined.
	    	}
	    a(1.4F, 2.9F);
	    this.fireProof = true;
		this.goalSelector.a(4, new PathfinderGoalGolemFireball(this));
		this.goalSelector.a(5, new PathfinderGoalMeleeAttack(this, EntityHuman.class, 1.3D, false));
		this.targetSelector.a(5, new PathfinderGoalNearestCustomTarget(this, EntityHuman.class, true, 7));
	  }
	  
	  @Override
	  public void move(double d0, double d1, double d2){
	  }


	protected void aW(){
	    super.aW();
	    getAttributeInstance(GenericAttributes.maxHealth).setValue(300.0D);
	    getAttributeInstance(GenericAttributes.d).setValue(0.0D);//movement speed
	    getAttributeInstance(GenericAttributes.c).setValue(500.0D);//KnockBack resist
	    getAttributeInstance(GenericAttributes.b).setValue(7.0D);//sight
	  }
	
}
