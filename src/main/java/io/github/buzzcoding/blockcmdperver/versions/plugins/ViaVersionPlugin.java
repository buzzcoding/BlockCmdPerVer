package io.github.buzzcoding.blockcmdperver.versions.plugins;

import com.viaversion.viaversion.api.Via;
import com.viaversion.viaversion.api.protocol.version.ProtocolVersion;
import org.bukkit.entity.Player;

import java.util.HashMap;
import java.util.List;

public class ViaVersionPlugin implements VersionPlugin {

	@Override
	public String pluginName() {
		return "ViaVersion";
	}

	@Override
	public Integer getProtocolVersion(Player player) {
		if (!isInstalled()) return -1;
		return Via.getAPI().getPlayerVersion(player.getUniqueId());
	}

	@Override
	public HashMap<Integer, String> getSupportedProtocols() {
		if (!isInstalled()) return null;
		HashMap<Integer, String> protocols = new HashMap<>();
		List<ProtocolVersion> viaprotocols = ProtocolVersion.getProtocols();

		for (ProtocolVersion protocol: viaprotocols) {
			String[] name = protocol.getName().split("[./]");
			if (name.length < 2) continue;
			
			String major = name[0];
			String release = name[1];
			int version = protocol.getVersion();

			protocols.put(version, major + "." + release);
		}

		return protocols;
	}
}