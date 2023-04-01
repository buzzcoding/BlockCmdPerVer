package io.github.buzzcoding.blockcmdperver;

import io.github.buzzcoding.blockcmdperver.versions.plugins.ProtocolSupportPlugin;
import io.github.buzzcoding.blockcmdperver.versions.plugins.VersionPlugin;
import io.github.buzzcoding.blockcmdperver.versions.plugins.ViaVersionPlugin;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.HashMap;
import java.util.Vector;
import java.util.logging.Level;

public class BlockCmdPerVer extends JavaPlugin {
	public static final VersionPlugin[] supportedVerPlugins = new VersionPlugin[] { new ViaVersionPlugin(), new ProtocolSupportPlugin() };
	protected static final HashMap<Integer, String> protocols = new HashMap<>();
	protected static final Vector<VersionPlugin> verPlugins = new Vector<>();
	protected static JavaPlugin plugin;

	public void onEnable() {
		plugin = this;

		for (VersionPlugin plugin: supportedVerPlugins)
			if (plugin.isInstalled()) verPlugins.add(plugin);

		if (verPlugins.isEmpty()) {
			Vector<String> supported = new Vector<>();
			for (VersionPlugin plugin: supportedVerPlugins)
				supported.add(plugin.pluginName());

			error(	"There are no version-adding plugins installed. Supported plugins: " + String.join(", ", supported)
				+	" (Note: They cannot be installed on the proxy or themselves be a proxy. This plugin only can detect versions from plugins on the Spigot server)");
			disable();
		}

		for (VersionPlugin plugin: verPlugins)
			protocols.putAll(plugin.getSupportedProtocols());

		BCPVConfig.initConfig();
		Bukkit.getPluginManager().registerEvents(new CommandDisabler(), plugin);
	}

	public static String getVersionName(Integer protocol) {
		return protocols.get(protocol);
	}

	public static Integer getPlayerProto(Player player) {
		for (VersionPlugin plugin: verPlugins) {
			int protocol = plugin.getProtocolVersion(player);
			if (protocol > -1) return protocol;
		}

		return -2;
	}

	public static VersionPlugin[] getVersionPlugins() {
		return verPlugins.toArray(new VersionPlugin[] {});
	}

	public void onDisable() {
		plugin = null;
		verPlugins.clear();
		protocols.clear();
	}

	protected static void disable() {
		Bukkit.getPluginManager().disablePlugin(plugin);
	}

	protected static void log(String text) {
		plugin.getLogger().log(Level.INFO, text);
	}

	protected static void warn(String text) {
		plugin.getLogger().log(Level.WARNING, text);
	}

	protected static void error(Throwable error, String message) {
		plugin.getLogger().log(Level.SEVERE, message, new BCPVException(error));
	}

	protected static void error(Throwable error) {
		error(error, "An error occurred");
	}

	protected static void error(String message) {
		error(new BCPVException(), message);
	}
}