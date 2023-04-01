package io.github.buzzcoding.blockcmdperver.versions.plugins;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

import java.util.HashMap;

public interface VersionPlugin {
	String pluginName();
	default boolean isInstalled() {
		return Bukkit.getPluginManager().isPluginEnabled(pluginName());
	}
	Integer getProtocolVersion(Player player);
	HashMap<Integer, String> getSupportedProtocols();
}