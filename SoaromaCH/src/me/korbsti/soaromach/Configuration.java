package me.korbsti.soaromach;

public class Configuration {
	Main plugin;
	
	public void setConfig(Main instance) {
		this.plugin = instance;
		plugin.saveDefaultConfig();
		plugin.getConfig().addDefault("channel-list", "&8[&3CH&8] &3Known channels are: {channels}");
		plugin.getConfig().addDefault("reloaded", "&8[&3CH&8] &3Reloaded Configuration");
		plugin.getConfig().addDefault("invalidArgs", "&8[&3CH&8] &3Invalid arguments!");
		plugin.getConfig().addDefault("noPerm", "&8[&3CH&8] &3No Permission");
		plugin.getConfig().addDefault("invalidChannel", "&8[&3CH&8] &3Invalid channel!");
		plugin.getConfig().addDefault("switchedChannel", "&8[&3CH&8] &3Channel set to &2{channel-name}");
		plugin.getConfig().addDefault("messageFormat", "{channel-prefix} {player} &8--> &3{message}");
		plugin.getConfig().addDefault("channels.name", "testChannel");
		plugin.getConfig().addDefault("channels.name.defaultGlobal", "global");
		plugin.getConfig().addDefault("channels.name.defaultGlobalPermission", "ch.defaultGlobal.use");
		plugin.getConfig().addDefault("channels.name.testChannel.permission", "ch.use.testChannel");
		plugin.getConfig().addDefault("channels.name.testChannel.prefix", "&8[&4testChannel&8]");
		plugin.getConfig().addDefault("channels.name.testChannel.sendRegardlessOfCurrentChannel", true);
		plugin.getConfig().addDefault("channels.name.testChannel.enableDistanceMessage", false);
		plugin.getConfig().addDefault("channels.name.testChannel.distanceMessage", 25);
		plugin.getConfig().options().copyDefaults(true);
		plugin.saveConfig();
	}
}
