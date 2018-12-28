package io.github.TcFoxy.ArenaTOW.v1_12_R1;

import net.minecraft.server.v1_12_R1.*;
import org.bukkit.entity.EntityType;


//Written by Arektor https://github.com/Arektor
public enum v1_12_R1_Entities {

    REDIRON_GOLEM("VillagerGolem", 99, EntityType.IRON_GOLEM, EntityIronGolem.class, MyRedGolem.class),
    BLUEIRON_GOLEM("VillagerGolem", 99, EntityType.IRON_GOLEM, EntityIronGolem.class, MyBlueGolem.class),
    BLUEGUARDIAN("elder_guardian", 4,EntityType.ELDER_GUARDIAN, EntityGuardianElder.class, MyBlueGuardian.class),
    REDGUARDIAN("elder_guardian", 4,EntityType.ELDER_GUARDIAN, EntityGuardianElder.class, MyRedGuardian.class),
    BLUEMINION("Zombie",54,EntityType.ZOMBIE, EntityZombie.class, MyBlueZombie.class),
    REDMINION("Zombie",54,EntityType.ZOMBIE, EntityZombie.class, MyRedZombie.class);


    private String name;
    private int id;
    private EntityType entityType;
    private Class<?> nmsClass;
    private Class<?> customClass;

    v1_12_R1_Entities(String name, int id,
                      EntityType entityType,
                      Class<?> nmsClass,
                      Class<?> customClass)
    {
        this.name = name;
        this.id = id;
        this.entityType = entityType;
        this.nmsClass = nmsClass;
        this.customClass = customClass;
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

    }
