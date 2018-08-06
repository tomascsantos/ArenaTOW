package io.github.TcFoxy.ArenaTOW.v1_12_R1;

import io.github.TcFoxy.ArenaTOW.API.TOWEntity;
import io.github.TcFoxy.ArenaTOW.API.TOWEntityHandler;
import net.minecraft.server.v1_12_R1.*;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.entity.Player;

import java.awt.*;

abstract class MyEntityGolem extends EntityIronGolem implements TOWEntity{
	Village a;
	public MyEntityGolem(World world){
		super(world);

		this.fireProof = true;
	}

	@Override
	public void n(){
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

    @Override
    public Location getLocation() {
        org.bukkit.World world = Bukkit.getWorld(this.world.toString());
	    Location loc = new Location(world, this.locX, this.locY, this.locZ)
	    return loc;
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
