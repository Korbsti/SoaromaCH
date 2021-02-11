package me.korbsti.soaromach;

import java.util.ArrayList;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;

public class Commands implements CommandExecutor {
	Main plugin;

	public Commands(Main instance) {
		this.plugin = instance;
	}

	public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
		if (label.equalsIgnoreCase("chlist")) {
			if (sender.hasPermission("ch.list")) {
				ArrayList<String> send = new ArrayList<>();
				for (String channel : plugin.channels) {
					send.add(channel);
				}
				int dd = send.size() - 1;
				int holder = 0;
				while (holder != dd) {
					for (int x = 0; x != send.size(); x++) {
						String channel = send.get(x);
						if (plugin.getConfig().getBoolean("channels.name." + channel + ".chlistDisplayAll") == false
								&& !channel.equals(plugin.getConfig().getString("channels.name.defaultGlobal"))) {
							for (int x1 = 0; x1 != send.size(); x1++) {
								if (!sender.hasPermission("channels.name." + channel + ".permission")) {
									if (send.get(x).equals(channel)) {
										send.remove(x);
										x = 0;
										x1 = 0;
									}
								}
							}
						}
					}
					holder++;
				}
				sender.sendMessage(ChatColor.translateAlternateColorCodes('&',
						(plugin.getConfig().getString("channel-list").replace("{channels}", send.toString()))));
				return true;
			} else {
				sender.sendMessage(ChatColor.translateAlternateColorCodes('&', plugin.getConfig().getString("noPerm")));
			}
		}
		if (label.equalsIgnoreCase("chreload")) {
			if (sender.hasPermission("ch.reload")) {
				plugin.reloadConfig();
				plugin.saveConfig();
				sender.sendMessage(
						ChatColor.translateAlternateColorCodes('&', plugin.getConfig().getString("reloaded")));
				return true;
			} else {
				sender.sendMessage(ChatColor.translateAlternateColorCodes('&', plugin.getConfig().getString("noPerm")));
			}
		}
		if (label.equalsIgnoreCase("ch")) {
			if (!(sender.hasPermission("ch.use.channels"))) {
				sender.sendMessage(ChatColor.translateAlternateColorCodes('&', plugin.getConfig().getString("noPerm")));
				return true;
			}
			if (args.length == 0 || args.length >= 2) {
				sender.sendMessage(
						ChatColor.translateAlternateColorCodes('&', plugin.getConfig().getString("invalidArgs")));
				return true;
			}
			if (plugin.derp.get(sender.getName()) == null) {
				plugin.derp.put(sender.getName(), false);
			}
			if (plugin.currentChannel.get(sender.getName()) == null) {
				plugin.currentChannel.put(sender.getName(),
						plugin.getConfig().getString("channels.name.defaultGlobal"));
			}
			plugin.channels.forEach(channel -> {
				if (args[0].toString().equalsIgnoreCase(channel)) {
					plugin.currentChannel.put(sender.getName(), channel);
					if (channel == plugin.getConfig().getString("channels.name.defaultGlobal")) {
						if (sender
								.hasPermission(plugin.getConfig().getString("channels.name.defaultGlobalPermission"))) {
							sender.sendMessage(ChatColor.translateAlternateColorCodes('&', plugin.getConfig()
									.getString("switchedChannel").replace("{channel-name}", channel)));
							plugin.derp.put(sender.getName(), true);
						} else {
							sender.sendMessage(ChatColor.translateAlternateColorCodes('&',
									plugin.getConfig().getString("noPerm")));
							plugin.derp.put(sender.getName(), true);
						}
						return;
					}
					if (!sender.hasPermission(plugin.getConfig().getString(
							"channels.name." + plugin.currentChannel.get(sender.getName()) + ".permission"))) {

						sender.sendMessage(
								ChatColor.translateAlternateColorCodes('&', plugin.getConfig().getString("noPerm")));
						plugin.derp.put(sender.getName(), true);
						return;
					}
					sender.sendMessage(ChatColor.translateAlternateColorCodes('&',
							plugin.getConfig().getString("switchedChannel").replace("{channel-name}", channel)));
					plugin.derp.put(sender.getName(), true);
					return;
				}

			});
			if (plugin.derp.get(sender.getName()) == false) {
				sender.sendMessage(
						ChatColor.translateAlternateColorCodes('&', plugin.getConfig().getString("invalidChannel")));
			}
			plugin.derp.put(sender.getName(), false);
			return true;
		}
		return true;
	}
}
