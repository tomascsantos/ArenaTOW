package io.github.TcFoxy.ArenaTOW.nms.v1_8_R1;

import java.lang.reflect.Field;

import org.bukkit.Bukkit;
import org.bukkit.craftbukkit.v1_8_R1.util.UnsafeList;

import net.minecraft.server.v1_8_R1.EntityGuardian;
import net.minecraft.server.v1_8_R1.EntityHuman;
import net.minecraft.server.v1_8_R1.EntityLiving;
import net.minecraft.server.v1_8_R1.GenericAttributes;
import net.minecraft.server.v1_8_R1.NBTTagCompound;
import net.minecraft.server.v1_8_R1.PathfinderGoalLookAtPlayer;
import net.minecraft.server.v1_8_R1.PathfinderGoalMeleeAttack;
import net.minecraft.server.v1_8_R1.PathfinderGoalMoveTowardsRestriction;
import net.minecraft.server.v1_8_R1.PathfinderGoalNearestAttackableTarget;
import net.minecraft.server.v1_8_R1.PathfinderGoalRandomLookaround;
import net.minecraft.server.v1_8_R1.PathfinderGoalRandomStroll;
import net.minecraft.server.v1_8_R1.PathfinderGoalSelector;
import net.minecraft.server.v1_8_R1.World;

public class CustomEntityGuardian extends EntityGuardian{

	public CustomEntityGuardian(World paramWorld)
	  {
	    super(paramWorld);
	    	    
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
	    
	    this.a(true); //This makes it elder guardian
	    this.b_ = 10;
	    a(0.85F, 0.85F);

	    this.goalSelector.a(4, new PathfinderGoalGuardianAttack(this));
		this.goalSelector.a(8, new PathfinderGoalMeleeAttack(this, EntityHuman.class, 1.3D, false));
		this.targetSelector.a(8, new PathfinderGoalNearestCustomTarget(this, EntityHuman.class, true, 10));
	    
	    //this.goalSelector.a(5, localPathfinderGoalMoveTowardsRestriction = new PathfinderGoalMoveTowardsRestriction(this, 1.0D));
	    //this.goalSelector.a(7, this.bq = new PathfinderGoalRandomStroll(this, 1.0D, 80));
	    //this.goalSelector.a(8, new PathfinderGoalLookAtPlayer(this, EntityGuardian.class, 12.0F, 0.01F));
	    //this.goalSelector.a(9, new PathfinderGoalRandomLookaround(this));
	    //this.bq.a(3);
	    //localPathfinderGoalMoveTowardsRestriction.a(3);
	    //this.moveController = new ControllerMoveGuardian(this);
	    //this.c = (this.b = this.random.nextFloat());
	  }

	public void aW()
	  {
	    super.aW();
	    getAttributeInstance(GenericAttributes.e).setValue(6.0D);//Damage (sorta? (damage is modified in the pathfinding goal))
	    getAttributeInstance(GenericAttributes.d).setValue(0.0D);//mov speed
	    getAttributeInstance(GenericAttributes.b).setValue(10.0D);//Sight
	    getAttributeInstance(GenericAttributes.maxHealth).setValue(700.0D);//Health
	  
	  }
	
	@Override
	public void a(NBTTagCompound paramNBTTagCompound)
	{
		super.a(paramNBTTagCompound);
		// This is always an elder guardian
		a(true);
	}

	@Override
	public void b(NBTTagCompound paramNBTTagCompound)
	{
		super.b(paramNBTTagCompound);
		paramNBTTagCompound.setBoolean("Elder", true);
	}

	// I am an elder guardian
	@Override
	public boolean cl()
	{
		return true;
	}
	
	// I am always water
	@Override
	public boolean V()
	{
		return true;
	}
	  
	  @Override
	  public void move(double d0, double d1, double d2){
	  }

	/*
	private boolean a(int paramInt) {
	    return (this.datawatcher.getInt(16) & paramInt) != 0;
	  }

	  private void a(int paramInt, boolean paramBoolean) {
	    int i = this.datawatcher.getInt(16);
	    if (paramBoolean)
	      this.datawatcher.watch(16, Integer.valueOf(i | paramInt));
	    else
	      this.datawatcher.watch(16, Integer.valueOf(i & (paramInt ^ 0xFFFFFFFF)));
	  }
*/
	  private void b(int i)
	  {
	    this.datawatcher.watch(17, Integer.valueOf(i));
	  }
	  
	  static void a(CustomEntityGuardian entityguardian, int i)
	  {
	    entityguardian.b(i);
	  }
	  
	  protected void E(){
		  //Dont do shit, i dont like the effects.
	  }
}
