package io.github.TcFoxy.ArenaTOW.v1_11_R1;

import io.github.TcFoxy.ArenaTOW.API.*;
import net.minecraft.server.v1_11_R1.*;
import org.bukkit.Location;

import java.lang.reflect.Field;
import java.util.LinkedHashSet;
import java.util.LinkedList;
import java.util.UUID;

public abstract class MyEntityZombie extends EntityZombie implements TOWEntity, CustomZombie{

    protected TOWEntityHandler handler;
    private final int targetDistance = 3;
    private LinkedList<Location> path;

    public MyEntityZombie(World world, TOWEntityHandler handler) {
        super(world);

        getAttributeInstance(GenericAttributes.FOLLOW_RANGE).setValue(10.0D);//foloow range
        fireProof = true;
        this.handler = handler;
    }

    @Override
    public void addPath(LinkedList<Location> path) {
        this.path = (LinkedList<Location>) path.clone();
    }

    private Location getNextLoc() {
        return path.removeFirst();
    }

    @Override
    public TOWEntityHandler getHandler() {
        return handler;
    }

    @Override
    public org.bukkit.entity.Entity getMob() {
        return this.getBukkitEntity();
    }

    @Override
    public Location getLocation() {
        return new Location(this.world.getWorld(), this.locX, this.locY, this.locZ);
    }

    @Override
    public UUID getUID() {
        return this.getUniqueID();
    }

    @Override
    public MobType getMobType() {
        return MobType.ZOMBIE;
    }

    @Override
    protected void r() {
        this.goalSelector.a(1, new MyPathfinderGoalMelee(this, 1.2D));
        //this.targetSelector.a(5, new PathfinderGoalNearestAttackableTarget<EntityHuman>(this, EntityHuman.class, true));
        this.targetSelector.a(1, new MyPathfinderGoalHurtByTarget(this, false, new Class[0]));
    }

    public void whereTo() {
        clearWalk();
        r();
        if (path.peek() != null) {
            this.goalSelector.a(6, new MyPathfindingGoalWalk(this, 1.2D, getNextLoc(), targetDistance));
        }
    }

    @SuppressWarnings("rawtypes")
    public void clearWalk() {
        LinkedHashSet goalB = (LinkedHashSet) getPrivateField("b", PathfinderGoalSelector.class, goalSelector);
        goalB.clear();
        LinkedHashSet goalC = (LinkedHashSet) getPrivateField("c", PathfinderGoalSelector.class, goalSelector);
        goalC.clear();
        LinkedHashSet targetB = (LinkedHashSet) getPrivateField("b", PathfinderGoalSelector.class, targetSelector);
        targetB.clear();
        LinkedHashSet targetC = (LinkedHashSet) getPrivateField("c", PathfinderGoalSelector.class, targetSelector);
        targetC.clear();
    }

    public static Object getPrivateField(String fieldName, Class<PathfinderGoalSelector> clazz, Object object) {
        Field field;
        Object o = null;
        try {
            field = clazz.getDeclaredField(fieldName);

            field.setAccessible(true);

            o = field.get(object);
        } catch (NoSuchFieldException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        }

        return o;
    }
}


