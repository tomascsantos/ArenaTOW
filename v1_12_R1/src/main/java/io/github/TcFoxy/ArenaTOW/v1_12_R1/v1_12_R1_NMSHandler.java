package io.github.TcFoxy.ArenaTOW.v1_12_R1;

import io.github.TcFoxy.ArenaTOW.API.NMSHandler;
import io.github.TcFoxy.ArenaTOW.API.TOWEntityHandler;
import org.bukkit.event.Listener;

import java.io.File;

public class NMSHandler implements io.github.TcFoxy.ArenaTOW.API.NMSHandler {

    private v1_12_R1_MobHandler mobHandler;
    private v1_12_R1_EntityRegistrar entityRegistrar;
    private v1_12_R1_Listener listener;

    public NMSHandler(File saveDirectory) {
        this.mobHandler = new v1_12_R1_MobHandler();
        this.entityRegistrar = new v1_12_R1_EntityRegistrar(saveDirectory);
        this.listener = new v1_12_R1_Listener(entityRegistrar, saveDirectory);
    }

    @Override
    public v1_12_R1_EntityRegistrar getEntityRegistry() {
        return entityRegistrar;
    }

    @Override
    public TOWEntityHandler getEntityHandler() {
        return mobHandler;
    }

    @Override
    public v1_12_R1_Listener getListener() {
        return listener;
    }
}
