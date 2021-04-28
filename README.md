# BlockCmdPerVer
A Spigot plugin that lets you block commands per version. Made as a custom plugin for someone.

# Credit/Sources
* CraftBukkit: https://hub.spigotmc.org/stash/projects/spigot/repos/craftbukkit/browse
* Spigot: https://hub.spigotmc.org/stash/projects/SPIGOT/repos/spigot/browse
* ViaVersion: https://github.com/ViaVersion/ViaVersion
* ProtocolSupport: https://github.com/ProtocolSupport/ProtocolSupport

# Plugin Requirements
You must have either ViaVersion or ProtocolSupport for this plugin to work (Having both also works).

# Usage
1. Place the plugin jar in your server's plugins folder.
2. Start up the server.
3. Open the config.yml in the BlockCmdPerVer folder in the plugins folder.
4. Put the commands you want to block in the brackets of the version name, seperated by a comma. (And don't forget the quotes.)

# Building (Advanced)
## Windows
1. Download Java SE JDK (Not JRE. JDK) if you don't have it.
2. Download maven3 for Windows if you don't have it.
3. Download Git for Windows if you don't have it.
4. Open Git Bash (type it in the start menu and press enter)
5. Type ``git clone https://github.com/BuzzTheGamer23/BlockCmdPerVer.git && cd BlockCmdPerVer && mvn.cmd install`` into the window that pops up.
6. If the building suceeded, open file explorer and type ``%appdata%\..\..\.m2\repository\com\buzzthegamer23\blockcmdperver`` into the address bar.
7. Open the subfolder with the higest version number and the jar file will be there.

## Mac/Linux
1. Download Java SE JDK (Not JRE. JDK) if you don't have it.
2. Download maven3 if you don't have it.
3. Download Git if you don't have it.
4. Open a terminal and type ``git clone https://github.com/BuzzTheGamer23/BlockCmdPerVer.git && cd BlockCmdPerVer && mvn install``
5. If the building suceeded, either open this directory in the terminal or your file manager: ``~/.m2/repository/com/buzzthegamer23/blockcmdperver`
6. Open the subfolder with the higest version number and the jar file will be there.
