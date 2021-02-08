package me.korbsti.soaromach;

import me.clip.placeholderapi.PlaceholderAPI;
import org.bukkit.Bukkit;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;

import net.md_5.bungee.api.ChatColor;

public class MessageSender {

	Main plugin;

	public MessageSender(Main instance) {
		this.plugin = instance;
	}

	public String format(String channelPrefix, Player p, String message) {
		String format = plugin.getConfig()
				.getString("channels.name." + plugin.currentChannel.get(p.getName()) + ".messageFormat");
		String format1 = format.replace("{channel-prefix}", channelPrefix);
		String format2 = format1.replace("{player}", p.getDisplayName());
		String format3 = format2.replace("{message}", message);
		if (plugin.hasPlaceholder) {
			return (PlaceholderAPI.setPlaceholders(p.getPlayer(), format3));
		}
		return format3;
	}

	public void messageChannelSender(Player player, String message, String permission) {
		String prefixChannel = null;
		for (Player p : Bukkit.getOnlinePlayers()) {
			String name = p.getName();
			if (plugin.currentChannel.get(name) == null) {
				plugin.currentChannel.put(name, plugin.getConfig().getString("channels.name.defaultGlobal"));
			}
			if (p.hasPermission(permission)) {
				if (!plugin.getConfig().getBoolean(
						"channels.name." + plugin.currentChannel.get(player.getName()) + ".enableDistanceMessage")) {

					if (permission == plugin.getConfig().getString(
							"channels.name." + plugin.currentChannel.get(player.getName()) + ".permission")) {
						prefixChannel = plugin.getConfig()
								.getString("channels.name." + plugin.currentChannel.get(player.getName()) + ".prefix");
					}
					if (plugin.getConfig().getBoolean("channels.name." + plugin.currentChannel.get(player.getName())
							+ ".sendRegardlessOfCurrentChannel") == true) {
						p.sendMessage(
								ChatColor.translateAlternateColorCodes('&', format(prefixChannel, player, message)));
					} else {
						if (plugin.currentChannel.get(p.getName()) == plugin.currentChannel.get(player.getName())) {
							p.sendMessage(ChatColor.translateAlternateColorCodes('&',
									format(prefixChannel, player, message)));
						}
					}

				} else {
					Double dis = plugin.getConfig().getDouble(
							"channels.name." + plugin.currentChannel.get(player.getName()) + ".distanceMessage");
					Bukkit.getScheduler().runTask(plugin, new Runnable() {

						@Override
						public void run() {
							String prefix = null;
							for (Entity entity : player.getNearbyEntities(dis, dis, dis)) {
								if (entity instanceof Player) {
									Player playerd = (Player) entity;
									if (permission == plugin.getConfig().getString("channels.name."
											+ plugin.currentChannel.get(player.getName()) + ".permission")) {
										prefix = plugin.getConfig().getString("channels.name."
												+ plugin.currentChannel.get(player.getName()) + ".prefix");
									}
									if (plugin.getConfig()
											.getBoolean("channels.name." + plugin.currentChannel.get(player.getName())
													+ ".sendRegardlessOfCurrentChannel") == true) {
										playerd.sendMessage(ChatColor.translateAlternateColorCodes('&',
												format(prefix, player, message)));
									} else {
										if (plugin.currentChannel.get(playerd.getName()) == plugin.currentChannel
												.get(player.getName())) {
											playerd.sendMessage(
													ChatColor.translateAlternateColorCodes('&', format(
															plugin.getConfig()
																	.getString("channels.name." + plugin.currentChannel
																			.get(player.getName()))
																	+ ".prefix",
															player, message)));
										}
									}

								}
								player.sendMessage(
										ChatColor.translateAlternateColorCodes('&', format(prefix, player, message)));
								Bukkit.getLogger().info(ChatColor.translateAlternateColorCodes('&', format(
										plugin.getConfig()
												.getString("channels.name."
														+ plugin.currentChannel.get(player.getName()) + ".prefix"),
										player, message)));

							}
						}

					});
					return;
				}
			}

		}
		Bukkit.getLogger().info(ChatColor.translateAlternateColorCodes('&', format(prefixChannel, player, message)));
	}
}
