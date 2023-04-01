package io.github.buzzcoding.blockcmdperver;

import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerCommandPreprocessEvent;

public class CommandDisabler implements Listener {
	@EventHandler
	public void onCommand(PlayerCommandPreprocessEvent e) {
		if (!BCPVConfig.isEnabled()) return;
		if (BCPVConfig.playerCanBypass(e.getPlayer())) return;

		try {
			int protocol = BlockCmdPerVer.getPlayerProto(e.getPlayer());
			String version = BlockCmdPerVer.getVersionName(protocol);
			String command = e.getMessage().split(" ")[0];

			if (BCPVConfig.commandDisabled(version, command)) {
				e.getPlayer().sendMessage("That command is disabled on " + version + ". Please join on another version.");
				e.setCancelled(true);
			}
		} catch (Exception error) {
			BlockCmdPerVer.error(error, "Failed to detect player-sent command.");
		}
	}
}