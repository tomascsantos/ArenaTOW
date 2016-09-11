package io.github.TcFoxy.ArenaTOW.BattleArena.objects.spawns;


public interface Spawnable {
    /**
     * Spawn this Spawnable
     */
	public void spawn();

    /**
     * Despawn this Spawnable
     */
	public void despawn();
}
