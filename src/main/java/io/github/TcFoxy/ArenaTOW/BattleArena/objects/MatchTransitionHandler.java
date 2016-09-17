package io.github.TcFoxy.ArenaTOW.BattleArena.objects;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

import io.github.TcFoxy.ArenaTOW.BattleArena.objects.events.EventPriority;



@Retention(RetentionPolicy.RUNTIME)
public @interface MatchTransitionHandler {
	EventPriority priority() default EventPriority.NORMAL;
}