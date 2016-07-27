package io.github.TcFoxy.ArenaTOW;

import java.io.File;

public class Messages {
	
	ArenaTOW main = ArenaTOW.getSelf();

	public Messages(){
		createMsgConfig();
	}
	
	private void createMsgConfig() {
        try {
            if (!main.getDataFolder().exists()) {
                main.getDataFolder().mkdirs();
            }
            File file = new File(main.getDataFolder(), "Messages.yml");
            if (!file.exists()) {
                main.getLogger().info("Config.yml not found, creating!");
                main.saveDefaultConfig();
            } else {
                main.getLogger().info("Config.yml found, loading!");
            }
        } catch (Exception e) {
            e.printStackTrace();

        }

    }
}
