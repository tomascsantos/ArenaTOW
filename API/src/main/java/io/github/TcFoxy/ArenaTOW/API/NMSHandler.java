package io.github.TcFoxy.ArenaTOW.API;

import org.bukkit.event.Listener;

public interface NMSHandler {

    CustomEntityRegistrar getEntityRegistry();

    TOWEntityHandler getEntityHandler();

    Listener getListener();
}
