package me.korbsti.soaromach;

import java.io.IOException;

import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;

public class Join implements Listener {
	Main plugin;

	public Join(Main instance) {
		this.plugin = instance;
	}

	@EventHandler
	public void onJoin(PlayerJoinEvent event) {
		Player p = event.getPlayer();
		
		plugin.currentChannel.put(event.getPlayer().getName(), plugin.getConfig().getString("channels.name.channelUponJoining"));
		
		if(plugin.dataYaml.getString(p.getUniqueId().toString() + ".channel") != null) {
			plugin.currentChannel.put(event.getPlayer().getName(), plugin.dataYaml.getString(p.getUniqueId().toString() + ".channel"));
		} else {
			
			plugin.dataYaml.set(p.getUniqueId().toString() + ".channel", plugin.getConfig().getString("channels.name.channelUponJoining"));
			
			
			try {
				plugin.dataYaml.save(plugin.dataFile);
			} catch (IOException e) {
				// TODO Auto-generated catch block
				;
			}
		}
	}
}
