package io.github.TcFoxy.ArenaTOW.BattleArena.objects.teams;

import io.github.TcFoxy.ArenaTOW.BattleArena.objects.ArenaPlayer;

public interface TeamHandler {

	/**
	 * Can this person exit out of this object gracefully(can include death)
	 *
	 * @param p
	 * @return
	 */
	public boolean canLeave(ArenaPlayer p);

	/**
	 * Player is disconnect/quit/left
	 *
     * @param p
     * @return
	 */
	public boolean leave(ArenaPlayer p);
}
