package io.github.TcFoxy.ArenaTOW.v1_11_R1;


import java.lang.reflect.Field;
import java.util.UUID;

import io.github.TcFoxy.ArenaTOW.API.Events.CustomEntityTakeDamageEvent;
import io.github.TcFoxy.ArenaTOW.API.MobType;
import io.github.TcFoxy.ArenaTOW.API.TOWEntity;
import io.github.TcFoxy.ArenaTOW.API.TOWEntityHandler;
import net.minecraft.server.v1_11_R1.DamageSource;
import net.minecraft.server.v1_11_R1.DataWatcherObject;
import net.minecraft.server.v1_11_R1.EntityGuardian;
import net.minecraft.server.v1_11_R1.EntityGuardianElder;
import net.minecraft.server.v1_11_R1.EntityHuman;
import net.minecraft.server.v1_11_R1.EntityLiving;
import net.minecraft.server.v1_11_R1.EnumMoveType;
import net.minecraft.server.v1_11_R1.GenericAttributes;
import net.minecraft.server.v1_11_R1.PathfinderGoal;
import net.minecraft.server.v1_11_R1.PathfinderGoalLookAtPlayer;
import net.minecraft.server.v1_11_R1.PathfinderGoalNearestAttackableTarget;
import net.minecraft.server.v1_11_R1.PathfinderGoalRandomLookaround;
import net.minecraft.server.v1_11_R1.PathfinderGoalRandomStroll;
import net.minecraft.server.v1_11_R1.World;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.entity.Entity;

public abstract class MyEntityGuardian extends EntityGuardianElder implements TOWEntity{

    protected TOWEntityHandler handler;

    public MyEntityGuardian(World world, TOWEntityHandler handler) {
        super(world);
        this.handler = handler;

    }

    @Override
    protected void r(){
        this.targetSelector.a(5, new PathfinderGoalNearestAttackableTarget<EntityHuman>(this, EntityHuman.class, true));
        this.goalSelector.a(8, new PathfinderGoalLookAtPlayer(this, EntityHuman.class, 8.0F));
        this.goalSelector.a(8, new PathfinderGoalRandomLookaround(this));
        this.goalSelector.a(4, new PathfinderGoalGuardianAttack(this, 16F));

        //We have to include these for the beam particles
        this.goalRandomStroll = new PathfinderGoalRandomStroll(this, 1.0, 80);
        this.goalRandomStroll.a(3);
    }

    @Override
    public void initAttributes()
    {
        super.initAttributes();
        getAttributeInstance(GenericAttributes.ATTACK_DAMAGE).setValue(10.0D);
        getAttributeInstance(GenericAttributes.FOLLOW_RANGE).setValue(16.0D);
        getAttributeInstance(GenericAttributes.maxHealth).setValue(300.0D);
    }

    @Override
    public void move(EnumMoveType type, double d0, double d1, double d2){
    }

    @Override
    public boolean damageEntity(DamageSource damageSource, float f) {
        CustomEntityTakeDamageEvent e = new CustomEntityTakeDamageEvent(this, damageSource.getEntity().getBukkitEntity());
        Bukkit.getPluginManager().callEvent(e);
        return !e.isCancelled() && super.damageEntity(damageSource, f);
    }

    @Override
    protected void M() {//no effects. Eventually make team-specific effects?
    }


    /*
     * This gets the DataWatcherObject<Integer> called bA
     * in the entityguardian class which is private
     */
    @SuppressWarnings("unchecked")
    public DataWatcherObject<Integer> getFieldB() throws Exception{
        Field f=EntityGuardian.class.getDeclaredField("bA");
        f.setAccessible(true);
        DataWatcherObject<Integer> temp = (DataWatcherObject<Integer>) f.get((EntityGuardian)this);
        return temp;
    }

    private void a(final int n) {
        DataWatcherObject<Integer> bA;
        try {
            bA = getFieldB();
            this.datawatcher.set(bA, n);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public int cooldown() {
        return 30; //20 ticks per second
    }

    @Override
    public TOWEntityHandler getHandler() {
        return handler;
    }

    @Override
    public Entity getMob() {
        return this.getBukkitEntity();
    }

    @Override
    public MobType getMobType() {
        return MobType.NEXUS;
    }

    @Override
    public Location getLocation() {
        return new Location(this.world.getWorld(), this.locX, this.locY, this.locZ);
    }

    @Override
    public UUID getUID() {
        return this.getUniqueID();
    }


    static class PathfinderGoalGuardianAttack extends PathfinderGoal
    {
        private final MyEntityGuardian a;
        private int b;
        private final boolean c;
        private float damage;

        public PathfinderGoalGuardianAttack(final MyEntityGuardian a, float damage) {
            this.a = a;
            this.c = (a instanceof EntityGuardianElder);
            this.a(3);
            this.damage = damage;
        }

        @Override
        public boolean a() {
            final EntityLiving goalTarget = this.a.getGoalTarget();
            return goalTarget != null && goalTarget.isAlive();
        }

        @Override
        public boolean b() {
            return super.b() && (this.c || this.a.h(this.a.getGoalTarget()) > 9.0);
        }

        @Override
        public void c() {
            this.b = -10;
            this.a.getNavigation().o();
            this.a.getControllerLook().a(this.a.getGoalTarget(), 90.0f, 90.0f);
            this.a.impulse = true;
        }

        @Override
        public void d() {
            this.a.a(0);
            this.a.setGoalTarget(null);
            this.a.goalRandomStroll.i();
        }

        @Override
        public void e() {
            final EntityLiving goalTarget = this.a.getGoalTarget();
            this.a.getNavigation().o();
            this.a.getControllerLook().a(goalTarget, 90.0f, 90.0f);
            if (!this.a.hasLineOfSight(goalTarget) || this.a.isSameTeam(goalTarget)) {
                this.a.setGoalTarget(null);
                return;
            }
            ++this.b;
            if (this.b == 0) {
                this.a.a(this.a.getGoalTarget().getId());
                this.a.world.broadcastEntityEffect(this.a, (byte)21);
            }
            else if (this.b >= this.a.cooldown()) {
                float f = damage;
                goalTarget.damageEntity(DamageSource.b(this.a, this.a), f);
                goalTarget.damageEntity(DamageSource.mobAttack(this.a), (float)this.a.getAttributeInstance(GenericAttributes.ATTACK_DAMAGE).getValue());
                this.a.setGoalTarget(null);
            }
            super.e();
        }
    }
}