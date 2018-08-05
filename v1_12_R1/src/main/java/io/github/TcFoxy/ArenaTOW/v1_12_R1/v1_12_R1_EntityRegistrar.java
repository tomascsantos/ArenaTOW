package io.github.TcFoxy.ArenaTOW.v1_12_R1;

import io.github.TcFoxy.ArenaTOW.API.CustomEntityRegistrar;
import net.minecraft.server.v1_12_R1.*;
import org.bukkit.Bukkit;
import org.bukkit.Chunk;
import org.bukkit.World;
import org.bukkit.craftbukkit.v1_12_R1.CraftWorld;
import org.bukkit.craftbukkit.v1_12_R1.entity.CraftEntity;
import org.bukkit.event.entity.CreatureSpawnEvent;

import java.io.*;
import java.lang.reflect.Field;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

public class v1_12_R1_EntityRegistrar implements CustomEntityRegistrar{

    public final Map<String, v1_12_R1_Entities> registryEntries = new HashMap<String, v1_12_R1_Entities>();
    File dir;

    v1_12_R1_EntityRegistrar(File dir) {
        this.dir = dir;
    }

    public void registerEntities() {
        for (v1_12_R1_Entities entity : v1_12_R1_Entities.values()) {
            register(entity);
        }
    }

    /**
     * Unregister all registered entities.<br>
     */
    public void unregisterEntities() {
        if (registryEntries.isEmpty()) return;
        for (v1_12_R1_Entities cre : registryEntries.values()) unregister(cre);

        registryEntries.clear();
    }

    /**
     * Register a new custom entity.<br>
     * If the entity is already registered, does nothing.<br><br>
     *
     * When an entity is registered, it will load every already spawned instance of this entity, so it may or may not create a little lag spike.
     *
     * @param cre
     * The registry entry to register.
     */
    @SuppressWarnings("unchecked")
    public void register(final v1_12_R1_Entities cre) {
        if (registryEntries.containsKey(cre.getName())) return;
        final String paramString = cre.getName();
        try {
            MinecraftKey key = new MinecraftKey(paramString);
            ((Set<MinecraftKey>) getPrivateStatic(EntityTypes.class, "d")).add(key);
            ((RegistryMaterials<MinecraftKey,Class<?>>) getPrivateStatic(EntityTypes.class, "b")).a(cre.getID(), key, cre.getCustomClass());
            registryEntries.put(paramString, cre);
        } catch (Exception e) {
            e.printStackTrace();
        }

        for (World w : Bukkit.getWorlds()) {
            final File wFile = new File(dir, w.getName());
            if (wFile.exists()) {
                for (Chunk c : w.getLoadedChunks()) {
                    if (new File(wFile, ""+c.getX()).exists() && new File(wFile, c.getX()+System.getProperty("file.separator")+c.getZ()).exists()) {
                        final File cFile = new File(wFile, c.getX()+System.getProperty("file.separator")+c.getZ());

                        for (File data : cFile.listFiles(new FilenameFilter() {
                            public boolean accept(File dir, String name) {
                                return name.toLowerCase().endsWith(".tbp");
                            }
                        })) if (data.getName().contains(cre.getName())) loadEntity(data);

                    }
                }
            }
        }
    }

    /**
     * Unregister a custom entity, using its registry entry.<br>
     * <b>Will not work if you create a new instance of v1_12_R1_Entities, even if it has the same parameters.</b><br><br>
     *
     * Unregistering an entity will save and remove it, and may cause lag spikes - bigger than if you registered one.
     *
     * @param cre
     * The registry entry to unregister.
     */
    @SuppressWarnings("unchecked")
    public void unregister(final v1_12_R1_Entities cre) {
        if (!registryEntries.containsValue(cre)) return;
        final String paramString = cre.getName();
        try {
            ((Set<MinecraftKey>) getPrivateStatic(EntityTypes.class, "d")).remove(new MinecraftKey(paramString));
            registryEntries.remove(cre);
        } catch (Exception e) {
            e.printStackTrace();
        }

        for (World w : Bukkit.getWorlds()) {
            for (org.bukkit.entity.Entity e : w.getEntities()) {
                Entity nmsEntity = ((CraftEntity)e).getHandle();
                if (nmsEntity instanceof EntityInsentient) if (registryEntries.containsKey(EntityTypes.b(nmsEntity))) {
                    saveEntity((EntityInsentient) nmsEntity);
                    e.remove();
                }
            }
        }
    }

    private static Object getPrivateStatic(final Class<?> clazz, final String f) throws Exception {
        Field field = clazz.getDeclaredField(f);
        field.setAccessible(true);
        return field.get(null);
    }

    protected void saveEntity(EntityInsentient e) {
        try {
            final Chunk c = e.getBukkitEntity().getLocation().getChunk();
            final File cDir = new File(dir +System.getProperty("file.separator")+c.getWorld().getName()+System.getProperty("file.separator")+c.getX()+System.getProperty("file.separator")+c.getZ());
            cDir.mkdirs();

            final NBTTagCompound nbt = new NBTTagCompound();
            e.e(nbt);
            nbt.setString("cid", EntityTypes.b(e));
            e.getBukkitEntity().remove();

            NBTCompressedStreamTools.a(nbt, (OutputStream)(new FileOutputStream(new File(cDir, (cDir.list().length+1)+" - "+nbt.getString("cid")+".tbp"))));
        } catch (Exception e1) { e1.printStackTrace(); }
    }

    @SuppressWarnings("unchecked")
    protected boolean loadEntity(NBTTagCompound nbt, World w) {
        if (!registryEntries.containsKey(nbt.getString("cid"))) return false;
        try {
            final Class<? extends EntityInsentient> clazz = (Class<? extends EntityInsentient>) registryEntries.get(nbt.getString("cid")).getCustomClass();
            final net.minecraft.server.v1_12_R1.World nmsW = ((CraftWorld)w).getHandle();
            EntityInsentient e = clazz.getConstructor(net.minecraft.server.v1_12_R1.World.class).newInstance(nmsW);
            NBTTagList pos = nbt.getList("Pos", 6);
            NBTTagList rot = nbt.getList("Rotation", 5);

            e.setPositionRotation(pos.e(0), pos.e(1), pos.e(2), rot.f(0), rot.f(1));
            nmsW.addEntity(e, CreatureSpawnEvent.SpawnReason.NATURAL);

            e.f(nbt);
            return true;
        } catch (Exception e1) { e1.printStackTrace(); }

        return false;
    }

    protected void loadEntity(File entityFile) {
        try {
            String[] pathElements = entityFile.getAbsolutePath().split(System.getProperty("file.separator"));
            if (loadEntity(NBTCompressedStreamTools.a((InputStream)(new FileInputStream(entityFile))), Bukkit.getWorld(pathElements[pathElements.length-4]))) entityFile.delete();
        } catch (Exception e1) { e1.printStackTrace(); }
    }

    protected boolean chunkHasCustomEntities(Chunk c) {
        final File cDir = new File(dir + System.getProperty("file.separator")+c.getWorld().getName()+System.getProperty("file.separator")+c.getX()+System.getProperty("file.separator")+c.getZ());
        if (cDir.mkdirs()) return false;
        else {
            if (cDir.listFiles(new FilenameFilter() {
                public boolean accept(File dir, String name) {
                    return name.toLowerCase().endsWith(".tbp");
                }
            }).length <= 0) return false;
            else return true;
        }
    }
}

