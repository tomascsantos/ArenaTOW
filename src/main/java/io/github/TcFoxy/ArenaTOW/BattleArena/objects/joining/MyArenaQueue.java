package io.github.TcFoxy.ArenaTOW.BattleArena.objects.joining;


import io.github.TcFoxy.ArenaTOW.BattleArena.objects.arenas.MyArena;

import java.util.Iterator;
import java.util.LinkedList;


class MyArenaQueue extends LinkedList<MyArena> {
	private static final long serialVersionUID = 1L;

	@Override
	public void addLast(MyArena arena){
		Iterator<MyArena> iter = this.iterator();
		while (iter.hasNext() ){
			if (iter.next().getName().equals(arena.getName())){
				iter.remove();}
		}
		super.addLast(arena);
	}

	@Override
	public boolean remove(Object obj){
		if (!(obj instanceof MyArena))
			return false;
		MyArena arena = (MyArena) obj;
		Iterator<MyArena> iter = this.iterator();
		while (iter.hasNext() ){
			if (iter.next().getName().equals(arena.getName())){
				iter.remove();
				return true;
			}
		}
		return false;
	}

}
