package io.github.TcFoxy.ArenaTOW.nms.v1_13_R1;


import io.github.TcFoxy.ArenaTOW.Plugin.ArenaTOW;
import net.minecraft.server.v1_13_R1.*;
import org.bukkit.Chunk;
import org.bukkit.craftbukkit.v1_13_R1.entity.CraftEntity;
import org.bukkit.entity.Entity;
import org.bukkit.event.EventHandler;
import org.bukkit.event.world.ChunkLoadEvent;
import org.bukkit.event.world.ChunkUnloadEvent;

import java.io.File;
import java.io.FilenameFilter;

import static io.github.TcFoxy.ArenaTOW.nms.v1_13_R1.EntityRegistrar.getCustomEntityID;

public class v13_Listener implements org.bukkit.event.Listener {

    @EventHandler
    public void onChunkLoad(ChunkLoadEvent evt) {
        if (!EntityRegistrar.chunkHasCustomEntities(evt.getChunk())) {
            return;
        }
        Chunk c = evt.getChunk();
        File cDir = new File(ArenaTOW.getSelf().getSaveDir() + System.getProperty("file.separator") + c.getWorld().getName() + System.getProperty("file.separator") + c.getX() + System.getProperty("file.separator") + c.getZ());
        File[] arrfile = cDir.listFiles(new FilenameFilter(){

            @Override
            public boolean accept(File dir, String name) {
                return name.toLowerCase().endsWith(".tbp");
            }
        });
        int n = arrfile.length;
        int n2 = 0;
        while (n2 < n) {
            File f = arrfile[n2];
            EntityRegistrar.loadEntity(f);
            ++n2;
        }
    }

    @EventHandler
    public void onChunkUnload(ChunkUnloadEvent evt) {
        Chunk c = evt.getChunk();
        Entity[] arrentity = c.getEntities();
        int n = arrentity.length;
        int n2 = 0;
        while (n2 < n) {
            Entity e = arrentity[n2];
            net.minecraft.server.v1_13_R1.Entity nmsEntity = ((CraftEntity)e).getHandle();
            if (nmsEntity instanceof EntityInsentient && CustomEntityList.REGISTRY.keySet().contains(getCustomEntityID((EntityInsentient)nmsEntity))) {
                EntityRegistrar.saveEntity((EntityInsentient)nmsEntity);
            }
            ++n2;
        }
    }
}
