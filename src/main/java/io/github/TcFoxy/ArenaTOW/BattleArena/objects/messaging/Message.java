package io.github.TcFoxy.ArenaTOW.BattleArena.objects.messaging;


import java.util.HashSet;
import java.util.Set;

import io.github.TcFoxy.ArenaTOW.BattleArena.objects.messaging.MessageOptions.MessageOption;

public class Message {
    final private String msg;
    final private MessageOptions messageOptions;

	public Message(String msg, MessageOptions messageOptions) {
		this.msg = msg;
		this.messageOptions = messageOptions;
	}

	public Set<MessageOption> getOptions() {
		return messageOptions != null ? messageOptions.getOptions() : new HashSet<MessageOption>();
	}

	public String getMessage() {
		return msg;
	}

}
