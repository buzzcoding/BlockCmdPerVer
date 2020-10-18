package com.buzzthegamer23.blockcmdperver;

import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerCommandPreprocessEvent;

public class CommandDisabler implements Listener {
	@EventHandler
	public void onTeleport(PlayerCommandPreprocessEvent e) {
		try {
			if (!Config.getBoolean("enabled")) return;
			if (Config.getList(Main.getVersionName(Main.getPlayerProto(e.getPlayer()))).contains(e.getMessage().replaceFirst("/", "").split(" ")[0])) {
				e.getPlayer().sendMessage("That command is disabled on this version. Please join on another version.");
				e.setCancelled(true);
			}
		} catch (Exception e2) {
			Main.warn(Main.prefix + "[" + Main.pluginname + "] Failed to detect player-sent command. The full error is below.");
			e2.printStackTrace();
		}
	}
}