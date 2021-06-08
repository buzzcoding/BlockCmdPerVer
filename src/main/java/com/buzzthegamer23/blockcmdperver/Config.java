package com.buzzthegamer23.blockcmdperver;

import java.io.File;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import org.bukkit.Bukkit;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.util.Vector;

//BuzzTheGamer23 Configuration Manager v1.0.3
@SuppressWarnings("unused")
public class Config {
	//Bringing over main class stuff
	private static JavaPlugin plugin = BlockCmdPerVer.plugin;

	//Config of the config manager
	public static final List<Integer> knownconfigs = Arrays.asList(new Integer[] {1});
	public static final List<String> optionslist = Arrays.asList(new String[] {"version", "enabled", "1.4", "1.5", "1.6", "1.7", "1.8", "1.9", "1.10", "1.11", "1.12", "1.13", "1.14", "1.15", "1.16"});
	public static final Integer latestconfig = 1;

	
	protected static FileConfiguration getConfig() {
		return plugin.getConfig();
	}

	public static void reloadConfig() {
		plugin.reloadConfig();
	}

	protected static void saveDefaultConfig() {
		plugin.saveDefaultConfig();
	}

	protected static void handleInvalidConfig() {
		BlockCmdPerVer.warn("Invalid config: " + getConfigFile());
		String uuid = UUID.randomUUID().toString();
		Integer iend = uuid.indexOf("-");
		String id = null;
		if (iend != -1) {
			id = uuid.substring(0, iend);
		}
		if (id != null) {
			String name = getConfigFile().getName() + id;
		} else {
			BlockCmdPerVer.error("Failed to generate random broken config name for config file: " + getConfigFile());
			BlockCmdPerVer.disable();
		}
		BlockCmdPerVer.log("WARNING: Invalid config. Renaming it to " + getConfigFile());
	}

	protected static File getConfigFile() {
		File configFile = new File(getConfigFolder(), "config.yml");
		return configFile;
	}

	protected static File getConfigFolder() {
		return plugin.getDataFolder();
	}

	public static String getString(String option) {
		String value;
		reloadConfig();
		try {
			value = getConfig().getString(option);
		} catch (NullPointerException e) {
			value = "null";
		} 
		return value;
	}

	public static Boolean getBoolean(String option) {
		Boolean value;
		reloadConfig();
		try {
			value = getConfig().getBoolean(option);
		} catch (NullPointerException e) {
			value = false;
		} 
		return value;
	}

	public static List<?> getList(String option) {
		List<?> value;
		reloadConfig();
		try {
			value = getConfig().getList(option);
		} catch (NullPointerException e) {
			value = Collections.EMPTY_LIST;
		} 
		return value;
	}

	protected static void setOption(String option, String value) {
		reloadConfig();
		getConfig().set(option, value);
		reloadConfig();
	}

	protected static void checkConfig() {
		String confvers = getString("version");
		Integer confver = Integer.valueOf(Integer.parseInt(confvers));
		if (knownconfigs.contains(confver)) {
			if (confver.compareTo(latestconfig) > 0) {
				Map<String, String> options = new HashMap<String, String>();
				for (String option : optionslist) {
					if (optionslist.contains(option)) {
						String value = getString(option);
						options.put(option, value);
					} 
				} 
				File configFile = getConfigFile();
				configFile.delete();
				saveDefaultConfig();
				reloadConfig();
				for (String option : options.keySet()) {
					String value = options.get(option);
					setOption(option, value);
				} 
				reloadConfig();
			} 
		} else if (confver.compareTo(latestconfig) < 0) {
			Boolean missingvalues = Boolean.valueOf(false);
			for (String option : optionslist) {
				if (!getConfig().contains(option))
					missingvalues = Boolean.valueOf(true); 
			} 
			if (missingvalues.booleanValue() == true) {
				handleInvalidConfig();
			} else {
				BlockCmdPerVer.log("WARNING: The plugin has detected a newer config than plugin understands. This may cause issues.");
			} 
		} else {
			Boolean missingvalues = Boolean.valueOf(false);
			for (String option : optionslist) {
				if (!getConfig().contains(option))
					missingvalues = Boolean.valueOf(true); 
			} 
			if (missingvalues.booleanValue() == true) {
				handleInvalidConfig();
			} 
		} 
	}
}