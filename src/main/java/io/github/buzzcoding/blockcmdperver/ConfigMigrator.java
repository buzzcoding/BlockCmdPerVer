package io.github.buzzcoding.blockcmdperver;

import com.google.common.io.Files;
import org.bukkit.configuration.Configuration;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.MemoryConfiguration;
import org.bukkit.configuration.file.FileConfiguration;

import java.io.*;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.*;
import java.util.function.Consumer;

public enum ConfigMigrator {
	V1((config) -> {}),

	V2((config) -> {
		// Move blocked commands into the "versions" section
		ConfigurationSection versions = config.getConfigurationSection("versions");
		if (versions == null) versions = BCPVConfig.fixPathSeperator(new MemoryConfiguration());

		Set<String> keys = config.getKeys(false);
		for (String key: keys) {
			if (!key.matches("[0-9]*\\.[0-9]*")) continue;

			versions.set(key, config.get(key));
			config.set(key, null);
		}

		config.set("versions", versions);
	});

	static final DateTimeFormatter dateFormat = DateTimeFormatter.ofPattern("yyyy-MM-dd-HH_mm_ss");
	final Consumer<Configuration> migrateTo;

	ConfigMigrator(Consumer<Configuration> migrateTo) {
		this.migrateTo = migrateTo;
	}

	static void backupConfig() {
		try {
			String date = dateFormat.format(LocalDateTime.now());

			File config = new File(BlockCmdPerVer.plugin.getDataFolder(), "config.yml");
			File backupFolder = new File(BlockCmdPerVer.plugin.getDataFolder(), "configbackups/");
			if (!backupFolder.exists()) backupFolder.mkdir();

			File backupfile = new File(backupFolder, "configbackup-" + date + ".yml");
			FileOutputStream writeBackup = new FileOutputStream(backupfile);

			Files.copy(config, writeBackup);
		} catch (IOException e) {
			BlockCmdPerVer.error(e, "Failed to backup old config file.");
			BlockCmdPerVer.disable();
		}
	}

	static void deleteOldConfig() {
		File config = new File(BlockCmdPerVer.plugin.getDataFolder(), "config.yml");
		config.delete();
	}

	static MemoryConfiguration duplicateConfig(MemoryConfiguration from) {
		MemoryConfiguration to = new MemoryConfiguration();
		BCPVConfig.fixPathSeperator(to);

		for (String key: from.getKeys(true)) {
			to.set(key, from.get(key));
			to.setComments(key, from.getComments(key));
			to.setInlineComments(key, from.getInlineComments(key));
		}

		return to;
	}

	static void migrateFrom(int from) {
		int to = BCPVConfig.latestConfig;
		if (from == to) return;
		backupConfig();

		// Handle newer configs than the plugin knows
		if (from > to) {
			BlockCmdPerVer.warn("Config is newer than the plugin knows. Backing up file and ignoring version difference.");
			BCPVConfig.config.set("version", to);

			return;
		}

		// Migrate the format of the config to the new format
		BlockCmdPerVer.log("Backing up config file then migrating from version " + from + " to latest version (" + to + ")");

		for (int i = from + 1; i <= to; i++) {
			try {
				valueOf("V" + i).migrateTo.accept(BCPVConfig.config);
			} catch (IllegalArgumentException e) {
				BlockCmdPerVer.warn("Failed to migrate config version " + from + " to latest " + to + " (Missing migration steps). Replacing old config.");
				deleteOldConfig();
				BlockCmdPerVer.plugin.saveDefaultConfig();
			}
		}

		// Apply the new positioning and comments to the old config
		MemoryConfiguration oldconfig = duplicateConfig(BCPVConfig.config);
		deleteOldConfig();
		BlockCmdPerVer.plugin.saveDefaultConfig();
		BCPVConfig.reloadConfig();
		FileConfiguration newconfig = BCPVConfig.config;

		for (String key: oldconfig.getKeys(true)) {
			newconfig.set(key, oldconfig.get(key));

			if (newconfig.getComments(key).isEmpty()) {
				List<String> oldComments = oldconfig.getComments(key);
				newconfig.setComments(key, oldComments);
			}

			if (newconfig.getInlineComments(key).isEmpty()) {
				List<String> oldComments = oldconfig.getInlineComments(key);
				newconfig.setInlineComments(key, oldComments);
			}
		}
		newconfig.set("version", to);
		BCPVConfig.saveConfig();
	}
}
