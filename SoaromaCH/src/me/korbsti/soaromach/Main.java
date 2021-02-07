package me.korbsti.soaromach;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Set;

import org.bukkit.Bukkit;
import org.bukkit.event.Listener;
import org.bukkit.plugin.PluginManager;
import org.bukkit.plugin.java.JavaPlugin;


public class Main extends JavaPlugin implements Listener {
	public HashMap<String, Boolean> derp = new HashMap<String, Boolean>();
	public HashMap<String, String> currentChannel = new HashMap<String, String>();
	public MessageSender chatChannel = new MessageSender(this);
	public Commands commands = new Commands(this);
	public Configuration config = new Configuration();
	public Set<String> allKeys;
	public ArrayList<String> channels;
	@Override
	public void onEnable() {
		PluginManager pm = Bukkit.getPluginManager();
		pm.registerEvents(new ChatChannel(this), this);
		config.setConfig(this);
		getCommand("ch").setExecutor(new Commands(this));
		getCommand("chreload").setExecutor(new Commands(this));
		getCommand("chlist").setExecutor(new Commands(this));
		Set<String> allKeys = getConfig().getKeys(true);
		channels = new ArrayList<String>();
		for(String key : allKeys) {
			if(!key.endsWith("defaultGlobal") && !key.endsWith("defaultGlobalPermission") && key.startsWith("channels.name.") && !key.endsWith(".permission") && !key.endsWith(".prefix") && !key.endsWith(".sendRegardlessOfCurrentChannel")) {
				channels.add(key.replace("channels.name.", ""));
			}
		}
		channels.add(getConfig().getString("channels.name.defaultGlobal"));
	}
	@Override
	public void onDisable() {
		reloadConfig();
		saveConfig();
	}
}
