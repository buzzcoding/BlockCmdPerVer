package com.buzzthegamer23.blockcmdperver;

import java.util.HashMap;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;

import protocolsupport.api.ProtocolSupportAPI;

public class BlockCmdPerVer extends JavaPlugin {
	public static final String pluginname = "BlockCmdPerVer";
	protected static HashMap<Integer, String> protocols = new HashMap<Integer, String>();
	protected static String protplugin = null;
	protected static JavaPlugin plugin;

	public static class BCPVerException extends Exception {
		private static final long serialVersionUID = 1L;
		
		//Bring over constructors from Exception class
		public BCPVerException() { super(); }
		public BCPVerException(String message) { super(message); }
		public BCPVerException(String message, Throwable cause) { super(message, cause); }
		public BCPVerException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) { super(message, cause, enableSuppression, writableStackTrace); }
		public BCPVerException(Throwable cause) { super(cause); }
	}

	public void onEnable() {
		plugin = this;
		if (Bukkit.getPluginManager().isPluginEnabled("ViaVersion")) {
			try {
				Class.forName("com.viaversion.viaversion.api.ViaManager");
				protplugin = "ViaVersion";
			} catch (Exception e) {
				warn("You are using a version of ViaVersion below 4.0.0");
				protplugin = "ViaVersion-Legacy";
			}
		} else if (Bukkit.getPluginManager().isPluginEnabled("ProtocolSupport")) {
			protplugin = "ProtocolSupport";
		} else {
			error("ViaVersion or ProtocolSupport not found. Please install one of them or both.");
			onDisable();
		}
		Config.saveDefaultConfig();
		addProtocols("1.4", 47, 49, 51);
		addProtocols("1.5", 60, 61);
		addProtocols("1.6", 73, 74, 78);
		addProtocols("1.7", 3, 4, 5);
		addProtocols("1.8", 47); //Wow, Mojang put all of 1.8 on one protocol number...
		addProtocols("1.9", 107, 108, 109, 110);
		addProtocols("1.10", 210);
		addProtocols("1.11", 315, 316);
		addProtocols("1.12", 335, 338, 340);
		addProtocols("1.13", 393, 401, 404);
		addProtocols("1.14", 477, 480, 485, 490, 498);
		addProtocols("1.15", 573, 575, 578);
		addProtocols("1.16", 735, 736, 751, 753, 754);
		addProtocols("1.17", 755);
		Bukkit.getPluginManager().registerEvents(new CommandDisabler(), plugin);
	}

    protected static void addProtocols(String versionname, Integer... protocolnumbers) {
        for (Integer protocol: protocolnumbers) {
            protocols.put(protocol, versionname);
        }
    }

	public static String getVersionName(Integer protocol) {
		return protocols.get(protocol);
	}

	public static Integer getPlayerProto(Player player) {
		switch(protplugin) {
			case "ViaVersion":
				return com.viaversion.viaversion.api.Via.getAPI().getPlayerVersion(player.getUniqueId());
			case "ViaVersion-Legacy":
				return us.myles.ViaVersion.api.Via.getAPI().getPlayerVersion(player.getUniqueId());
			case "ProtocolSupport":
				return ProtocolSupportAPI.getProtocolVersion(player).getId();
		}
		return -2;
	}

	public static String getProtocolPlugin() {
		return protplugin;
	}

	public void onDisable() {
		plugin = null;
	}

	protected static void disable() {
		Bukkit.getPluginManager().disablePlugin(plugin);
	}

	protected static void log(String text) {
		plugin.getLogger().info(text);
	}

	protected static void warn(String text) {
		plugin.getLogger().warning(text);
	}

	protected static void error(Throwable error, String message) {
		error(new BCPVerException(message, error));
	}

	protected static void error(Throwable error) {
		try {
			throw error;
		} catch (Throwable e) {
			e.printStackTrace();
		}
	}

	protected static void error(String message) {
		error(new BCPVerException(message));
	}
}
