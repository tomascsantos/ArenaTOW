package io.github.TcFoxy.ArenaTOW.BattleArena.objects.messaging.plugins;



//import com.dthielke.herochat.Herochat;

import io.github.TcFoxy.ArenaTOW.BattleArena.objects.messaging.Channel;
import io.github.TcFoxy.ArenaTOW.BattleArena.objects.messaging.ChatPlugin;

public class HerochatPlugin implements ChatPlugin{

	@Override
	public Channel getChannel(String value) {
		//com.dthielke.herochat.Channel channel = Herochat.getChannelManager().getChannel(value);
		//return new HerochatChannel(channel);
		return null;
	}

}
