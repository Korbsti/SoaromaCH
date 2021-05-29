package me.korbsti.soaromach;

import me.clip.placeholderapi.PlaceholderAPI;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Set;

import org.bukkit.Bukkit;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;
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
	public Boolean hasPlaceholder = false;
	public Boolean enableGlobalChat = false;
	
	public File dataFile;
	public YamlConfiguration dataYaml;
	
	@Override
	public void onEnable() {
		PluginManager pm = Bukkit.getPluginManager();
		pm.registerEvents(new ChatChannel(this), this);
		pm.registerEvents(new Join(this), this);
		config.setConfig(this);
		getCommand("ch").setExecutor(new Commands(this));
		getCommand("chreload").setExecutor(new Commands(this));
		getCommand("chlist").setExecutor(new Commands(this));
		Set<String> allKeys = getConfig().getKeys(true);
		channels = new ArrayList<String>();
		for (String key : allKeys) {
			if (!key.endsWith("defaultGlobal") && !key.endsWith("defaultGlobalPermission")
					&& key.startsWith("channels.name.") && !key.endsWith(".permission") && !key.endsWith(".prefix")
					&& !key.endsWith(".sendRegardlessOfCurrentChannel") && !key.endsWith(".distanceMessage")
					&& !key.endsWith(".enableDistanceMessage") && !key.endsWith(".messageFormat")
					&& !key.endsWith(".chlistDisplayAll") && !key.endsWith(".channelExists")
					&& !key.endsWith(".defaultGlobalMessageFormat") && !key.endsWith(".enableGlobalMessageFormat")
					&& !key.endsWith(".channelUponJoining")) {
				channels.add(key.replace("channels.name.", ""));
			}
		}
		enableGlobalChat = getConfig().getBoolean("channels.name.enableGlobalMessageFormat");
		channels.add(getConfig().getString("channels.name.defaultGlobal"));
		if (Bukkit.getPluginManager().isPluginEnabled("PlaceholderAPI")) {
			hasPlaceholder = true;
		}
		int dd = channels.size() - 1;
		int holder = 0;
		while (dd != holder) {
			for (int x = 0; x != channels.size(); x++) {
				String channel = channels.get(x);
				if (!getConfig().getBoolean("channels.name." + channel + ".channelExists")
						&& !channel.equals(getConfig().getString("channels.name.defaultGlobal"))) {
					for (int x1 = 0; x1 != channels.size(); x1++) {
						if (channels.get(x).equals(channel)) {
							channels.remove(x);
							x1 = 0;
							x = 0;
						}
					}
				}
			}
			holder++;
		}
		
		for(Player p : Bukkit.getOnlinePlayers()) {
			currentChannel.put(p.getName(), getConfig().getString("channels.name.defaultGlobal"));
		}
		
		dataFile = new File(this.getDataFolder(), "data.yml");
		
		if (!dataFile.exists()) {
			try {
				dataFile.createNewFile();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		
		dataYaml = YamlConfiguration.loadConfiguration(dataFile);
		
	}

	@Override
	public void onDisable() {
		reloadConfig();
		saveConfig();
	}
}
