package io.github.buzzcoding.blockcmdperver.versions.plugins;

import org.bukkit.entity.Player;
import protocolsupport.api.ProtocolSupportAPI;
import protocolsupport.api.ProtocolVersion;

import java.util.HashMap;

public class ProtocolSupportPlugin implements VersionPlugin {

	@Override
	public String pluginName() {
		return "ProtocolSupport";
	}

	@Override
	public Integer getProtocolVersion(Player player) {
		if (!isInstalled()) return -1;
		return ProtocolSupportAPI.getProtocolVersion(player).getId();
	}

	@Override
	public HashMap<Integer, String> getSupportedProtocols() {
		if (!isInstalled()) return null;
		HashMap<Integer, String> protocols = new HashMap<>();
		ProtocolVersion[] psprotocols = ProtocolVersion.getAllSupported();

		for (ProtocolVersion protocol: psprotocols) {
			String[] name = protocol.getName().split("[.-]");
			String major = name[0];
			String release = name[1];
			int version = protocol.getId();

			protocols.put(version, major + "." + release);
		}

		return protocols;
	}
}