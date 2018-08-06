package io.github.TcFoxy.ArenaTOW.v1_13_R1;

import net.minecraft.server.v1_13_R1.*;
import org.bukkit.entity.EntityType;

import java.util.function.Function;


//Written by Arektor https://github.com/Arektor
public enum v1_13_R1_Entities {

    REDIRON_GOLEM("VillagerGolem", 99,EntityType.IRON_GOLEM, EntityIronGolem.class, MyRedGolem.class, MyRedGolem::new),
    BLUEIRON_GOLEM("VillagerGolem", 99,EntityType.IRON_GOLEM, EntityIronGolem.class, MyBlueGolem.class, MyBlueGolem::new),
    BLUEGUARDIAN("elder_guardian", 4,EntityType.ELDER_GUARDIAN, EntityGuardianElder.class, MyBlueGuardian.class, MyBlueGuardian::new),
    REDGUARDIAN("elder_guardian", 4, EntityType.ELDER_GUARDIAN, EntityGuardianElder.class, MyRedGuardian.class, MyRedGuardian::new),
    BLUEMINION("Zombie", 54,EntityType.ZOMBIE, EntityZombie.class, MyBlueZombie.class, MyBlueZombie::new),
    REDMINION("Zombie", 54, EntityType.ZOMBIE, EntityZombie.class, MyRedZombie.class, MyRedZombie::new);


    private String name;
    private int id;
    private EntityType entityType;
    private Class<?> nmsClass;
    private Class<?> customClass;
    private Function<? super World, ? extends EntityInsentient> function;

     v1_13_R1_Entities(String name, int id,
                       EntityType entityType,
                       Class<?> nmsClass,
                       Class<?> customClass,
                       Function<? super World, ? extends EntityInsentient> function) {
        this.name = name;
        this.id = id;
        this.entityType = entityType;
        this.nmsClass = nmsClass;
        this.customClass = customClass;
        this.function = function;
    }

    public String getName() {
        return name;
    }

    public int getID() {
        return id;
    }

    public EntityType getEntityType() {
        return entityType;
    }

    public Class<?> getNMSClass() {
        return nmsClass;
    }

    public Class<?> getCustomClass() {
        return customClass;
    }

    public Function<? super World, ? extends EntityInsentient> getFunction() {
        return function;
    }


}
