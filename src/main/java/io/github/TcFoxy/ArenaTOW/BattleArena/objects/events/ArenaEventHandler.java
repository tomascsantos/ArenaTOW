package io.github.TcFoxy.ArenaTOW.BattleArena.objects.events;


import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

import io.github.TcFoxy.ArenaTOW.BattleArena.objects.MatchState;


@Retention(RetentionPolicy.RUNTIME)
public @interface ArenaEventHandler {
	MatchState begin() default MatchState.NONE;
	MatchState end() default MatchState.NONE;
	EventPriority priority() default EventPriority.NORMAL;
	boolean needsPlayer() default true;
	String entityMethod() default "";
    boolean suppressCastWarnings() default false;
    boolean suppressWarnings() default false;
	org.bukkit.event.EventPriority bukkitPriority() default org.bukkit.event.EventPriority.HIGHEST;
}
