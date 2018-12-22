package io.github.TcFoxy.ArenaTOW.API;

import org.bukkit.Location;

import java.util.LinkedList;

public interface CustomZombie extends TOWEntity {

    void whereTo();

    void addPath(LinkedList<Location> path);
}
