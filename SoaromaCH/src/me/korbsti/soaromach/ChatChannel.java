package me.korbsti.soaromach;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.AsyncPlayerChatEvent;

import me.clip.placeholderapi.PlaceholderAPI;

public class ChatChannel implements Listener {
	Main plugin;

	public ChatChannel(Main instance) {
		plugin = instance;
	}

	@EventHandler(ignoreCancelled = true)
	public void chatEvent(AsyncPlayerChatEvent e) {
		String playerName = e.getPlayer().getName();
		if (plugin.currentChannel.get(playerName) == null) {
			plugin.currentChannel.put(playerName, plugin.getConfig().getString("channels.name.defaultGlobal"));
		}
		String perm = "permission";
		if (plugin.currentChannel.get(playerName) != plugin.getConfig().getString("channels.name.defaultGlobal")) {
			perm = plugin.getConfig()
					.getString("channels.name." + plugin.currentChannel.get(playerName) + ".permission");
			if (perm == null) {
				return;
			}
			plugin.chatChannel.messageChannelSender(e.getPlayer(), e.getMessage(), perm, false);
			e.setCancelled(true);
			return;
		}
		if (plugin.currentChannel.get(playerName).equalsIgnoreCase(plugin.getConfig().getString("channels.name.defaultGlobal"))
				&& plugin.enableGlobalChat) {
			String displayMessage = plugin.getConfig().getString("channels.name.defaultGlobalMessageFormat").replace("{player}", e.getPlayer().getDisplayName()).replace("{message}", e.getMessage());
			for (Player p : Bukkit.getOnlinePlayers()) {
				if (plugin.hasPlaceholder) {
					p.sendMessage(ChatColor.translateAlternateColorCodes('&', PlaceholderAPI.setPlaceholders(e.getPlayer(), displayMessage)));
				} else {
					p.sendMessage(ChatColor.translateAlternateColorCodes('&', displayMessage));
				}
			}
			Bukkit.getLogger().info(ChatColor.translateAlternateColorCodes('&', displayMessage));
			e.setCancelled(true);
		}
	}
}
