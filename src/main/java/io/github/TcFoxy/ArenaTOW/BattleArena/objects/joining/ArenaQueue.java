package io.github.TcFoxy.ArenaTOW.BattleArena.objects.joining;


import java.util.Iterator;
import java.util.LinkedList;

import io.github.TcFoxy.ArenaTOW.BattleArena.objects.arenas.Arena;


class ArenaQueue extends LinkedList<Arena> {
	private static final long serialVersionUID = 1L;

	@Override
	public void addLast(Arena arena){
		Iterator<Arena> iter = this.iterator();
		while (iter.hasNext() ){
			if (iter.next().getName().equals(arena.getName())){
				iter.remove();}
		}
		super.addLast(arena);
	}

	@Override
	public boolean remove(Object obj){
		if (!(obj instanceof Arena))
			return false;
		Arena arena = (Arena) obj;
		Iterator<Arena> iter = this.iterator();
		while (iter.hasNext() ){
			if (iter.next().getName().equals(arena.getName())){
				iter.remove();
				return true;
			}
		}
		return false;
	}

}
