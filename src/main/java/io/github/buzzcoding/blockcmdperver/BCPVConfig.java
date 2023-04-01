package io.github.buzzcoding.blockcmdperver;

import io.github.buzzcoding.blockcmdperver.versions.VersionNameCompare;
import org.bukkit.configuration.Configuration;
import org.bukkit.configuration.InvalidConfigurationException;
import org.bukkit.configuration.MemoryConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.*;

// BuzzTheGamer23 Configuration Manager v2.0.0
public class BCPVConfig {
	public static final int latestConfig = 2;
	protected static YamlConfiguration config;
	protected static File configFile;

	public static final Map<String, Object> defaults = Collections.unmodifiableMap(new HashMap<String, Object>() {{
		put("version", latestConfig);
		put("enabled", true);
		put("bypass-permission", false);
		put("versions", fixPathSeperator(new MemoryConfiguration()));
	}});

	// Allow for a period in a config option key by changing the section separator to a slash
	public static Configuration fixPathSeperator(Configuration config) {
	 	config.getRoot().options().pathSeparator('/');
	 	return config;
	}

	public static void reloadConfig() {
		config = new YamlConfiguration();
		fixPathSeperator(config);

		try {
			config.load(configFile);
		} catch (FileNotFoundException e) {
		} catch (IOException | InvalidConfigurationException e) {
			BlockCmdPerVer.error(e, "Cannot load config file " + configFile);
		}

		configureDefaults();
	}

	public static void saveConfig() {
		try {
			config.save(configFile);
		} catch (IOException e) {
			BlockCmdPerVer.error(e, "Error saving config file " + configFile);
		}
	}

	// Add default option to config if no value is set
	public static void addDefaultOption(String key, Object value) {
		if (!config.contains(key))
			config.set(key, value);
		config.addDefault(key, value);
	}

	public static void clearDefaults() {
		config.setDefaults(fixPathSeperator(new MemoryConfiguration()));
	}

	public static void configureDefaults() {
		clearDefaults();
		defaults.forEach(BCPVConfig::addDefaultOption);

		Collection<String> versions = BlockCmdPerVer.protocols.values();
		TreeSet<String> versionSet = new TreeSet<>(new VersionNameCompare());
		versionSet.addAll(versions);

		for (String version: versionSet) {
			addDefaultOption("versions/" + version, new Vector<String>());
		}
	}

	public static void initConfig() {
		BlockCmdPerVer.plugin.saveDefaultConfig();

		configFile = new File(BlockCmdPerVer.plugin.getDataFolder(), "config.yml");
		reloadConfig();
//		printKeys(config);

		// Migrate from older config versions
		int version = config.getInt("version", latestConfig);
		if (version != latestConfig)
			ConfigMigrator.migrateFrom(version);

		saveConfig();
	}

	public static boolean isEnabled() {
		reloadConfig();

		return config.getBoolean("enabled", true);
	}

	public static boolean playerCanBypass(Player player) {
		reloadConfig();

		boolean bypassEnabled = config.getBoolean("bypass-permission", false);
		boolean hasPermission = player.hasPermission("bcpver.bypass");

		return bypassEnabled && hasPermission;
	}

	public static boolean commandDisabled(String version, String command) {
		reloadConfig();

		List<?> disabledCommands = config.getList("versions/" + version, new Vector<String>());
		return disabledCommands.contains(command.replaceFirst("/", ""));
	}
}