package com.buzzthegamer23.blockcmdperver;

import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerCommandPreprocessEvent;

public class CommandDisabler implements Listener {
	@EventHandler
	public void onCommand(PlayerCommandPreprocessEvent e) {
		if (e.getPlayer().hasPermission("bcpver.bypass")) return;
		try {
			if (!Config.getBoolean("enabled")) return;
			if (Config.getList(BlockCmdPerVer.getVersionName(BlockCmdPerVer.getPlayerProto(e.getPlayer()))).contains(e.getMessage().replaceFirst("/", "").split(" ")[0])) {
				e.getPlayer().sendMessage("That command is disabled on this version. Please join on another version.");
				e.setCancelled(true);
			}
		} catch (Exception e2) {
			BlockCmdPerVer.error(e2, "Failed to detect player-sent command.");
		}
	}
}
