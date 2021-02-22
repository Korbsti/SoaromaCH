package me.korbsti.soaromach;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.AsyncPlayerChatEvent;

public class ChatChannel implements Listener {
	Main plugin;

	public ChatChannel(Main instance) {
		plugin = instance;
	}

	@EventHandler
	public void chatEvent(AsyncPlayerChatEvent e) {
		String playerName = e.getPlayer().getName();
		if (plugin.currentChannel.get(playerName) == null) {
			plugin.currentChannel.put(playerName, plugin.getConfig().getString("channels.name.defaultGlobal"));
		}
		String perm = "permission";
		if (plugin.currentChannel.get(playerName) != plugin.getConfig().getString("channels.name.defaultGlobal")) {
			if (plugin.currentChannel.get(playerName).contains(plugin.currentChannel.get(playerName))) {
				perm = plugin.getConfig()
						.getString("channels.name." + plugin.currentChannel.get(playerName) + ".permission");
			}
			plugin.chatChannel.messageChannelSender(e.getPlayer(), e.getMessage(), perm);
			e.setCancelled(true);
			return;
		}
		if (plugin.currentChannel.get(playerName) == plugin.getConfig().getString("channels.name.defaultGlobal")
				&& plugin.enableGlobalChat) {
			String displayMessage = plugin.getConfig().getString("channels.name.defaultGlobalMessageFormat")
					.replace("{player}", e.getPlayer().getDisplayName()).replace("{message}", e.getMessage());
			for (Player p : Bukkit.getOnlinePlayers()) {
				p.sendMessage(ChatColor.translateAlternateColorCodes('&', displayMessage));
			}
			Bukkit.getLogger().info(ChatColor.translateAlternateColorCodes('&', displayMessage));
			e.setCancelled(true);
		}
	}
}
