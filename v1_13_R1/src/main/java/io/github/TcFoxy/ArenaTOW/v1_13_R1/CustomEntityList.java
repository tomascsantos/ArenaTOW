package io.github.TcFoxy.ArenaTOW.v1_13_R1;

import com.google.common.collect.BiMap;
import com.google.common.collect.HashBiMap;
import net.minecraft.server.v1_13_R1.EntityInsentient;
import net.minecraft.server.v1_13_R1.EntityTypes;

class CustomEntityList {
    public static final BiMap<String, EntityTypes<? extends EntityInsentient>> REGISTRY = HashBiMap.create();

}
