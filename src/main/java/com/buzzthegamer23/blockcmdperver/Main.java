package com.buzzthegamer23.blockcmdperver;

import java.util.HashMap;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;

import protocolsupport.api.ProtocolSupportAPI;

public class Main extends JavaPlugin {
	public static final String pluginname = "BlockCmdPerVer";
	public static final String prefix = "[BlockCmdPerVer] ";
	private static HashMap<Integer, String> protocols = new HashMap<Integer, String>();
	private static String protplugin = null;
	public static JavaPlugin plugin;

	public static class ProtocolPluginNotFoundException extends Exception { private static final long serialVersionUID = 1L; }

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
			error("ViaVersion or ProtocolSupport not found. Please install one of them or both.", new ProtocolPluginNotFoundException());
			onDisable();
		}
		Config.saveDefaultConfig();
        addProtocols("1.4", new Integer[] {47, 49, 51});
        addProtocols("1.5", new Integer[] {60, 61});
        addProtocols("1.6", new Integer[] {73, 74, 78});
        addProtocols("1.7", new Integer[] {3, 4, 5});
        addProtocols("1.8", new Integer[] {47}); //Wow, Mojang put all of 1.8 on one protocol number...
        addProtocols("1.9", new Integer[] {107, 108, 109, 110});
        addProtocols("1.10", new Integer[] {210});
        addProtocols("1.11", new Integer[] {315, 316});
        addProtocols("1.12", new Integer[] {335, 338, 340});
        addProtocols("1.13", new Integer[] {393, 401, 404});
        addProtocols("1.14", new Integer[] {477, 480, 485, 490, 498});
        addProtocols("1.15", new Integer[] {573, 575, 578});
        addProtocols("1.16", new Integer[] {735, 736, 751, 753, 754});
		Bukkit.getPluginManager().registerEvents(new CommandDisabler(), plugin);
	}

    private static void addProtocols(String versionname, Integer[] protocolnumbers) {
        for (Integer protocol: protocolnumbers) {
            protocols.put(protocol, versionname);
        }
    }

	public static String getVersionName(Integer protocol) {
		return protocols.get(protocol);
	}

	public static Integer getPlayerProto(Player player) {
		if (protplugin.equals("ViaVersion")) {
			return com.viaversion.viaversion.api.Via.getAPI().getPlayerVersion(player.getUniqueId());
		} else if (protplugin.equals("ViaVersion-Legacy")) {
			return us.myles.ViaVersion.api.Via.getAPI().getPlayerVersion(player.getUniqueId());
		} else if (protplugin.equals("ProtocolSupport")) {
			return ProtocolSupportAPI.getProtocolVersion(player).getId();
		} else {
		    warn("Failed to get protocol id of " + player.getName());
		    return -2;
		}
	}

	public static String getProtocolPlugin() {
		return protplugin;
	}

	public void onDisable() {
		plugin = null;
	}

	public static void disable() {
		Bukkit.getPluginManager().disablePlugin(plugin);
	}

	public static void log(String text) {
		Bukkit.getLogger().info(text);
	}

	public static void warn(String text) {
		Bukkit.getLogger().warning(text);
	}

	public static void error(String text, Throwable error) {
		Bukkit.getLogger().warning(text);
		try {
			throw error;
		} catch (Throwable e) {
			e.printStackTrace();
		}
	}
}
