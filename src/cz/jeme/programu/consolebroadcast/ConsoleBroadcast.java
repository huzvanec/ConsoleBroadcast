package cz.jeme.programu.consolebroadcast;

import java.io.File;
import java.io.IOException;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.logging.Level;

import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandException;
import org.bukkit.command.CommandSender;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;

import net.md_5.bungee.api.ChatColor;

public class ConsoleBroadcast extends JavaPlugin {

	private Map<String, String> messageConfig = new HashMap<String, String>();

	@Override
	public void onEnable() {
		File f = getDataFolder();
		if (!f.exists()) {
			f.mkdir();
		}
		prepareConfig();
	}

	@Override
	public void onDisable() {
	}

	@Override
	public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
		if (command.getName().equalsIgnoreCase("console")) {
			if (sender instanceof Player) {
				sender.sendMessage(ChatColor.RED + "This command is only runnable from console!");
				return true;
			}
			if (args.length < 2) {
				sender.sendMessage(ChatColor.RED + "This command needs 2 arguments!");
				return false;
			} else {
				String recipient = args[0];
				Collection<? extends Player> onlinePlayers = Bukkit.getOnlinePlayers();
				boolean foundHim = false;
				for (Player player : onlinePlayers) {
					if (player.getName().equals(recipient)) {
						foundHim = true;
						break;
					}
				}
				if (!foundHim && recipient.toLowerCase().equals("@everyone")) {
					recipient = "@a";
				} else if (!foundHim) {
					if (recipient.charAt(0) == '@') {
						sender.sendMessage(ChatColor.RED + "Wrong selector \"" + ChatColor.GOLD + recipient + ChatColor.RED + "\"! To broadcast to all players use " + ChatColor.DARK_RED + "@everyone" + ChatColor.RED + "!");
					} else {
						sender.sendMessage(ChatColor.RED + "Player not found.");
					}
					return true;
				}
				StringBuffer message = new StringBuffer(30);
				for (int i = 1; i < args.length; i++) {
					message.append(args[i]);
					if (i != args.length - 1) {
						message.append(" ");
					}
				}
				String prefix = messageConfig.get("Prefix");
				String name = messageConfig.get("Name");
				String suffix = messageConfig.get("Suffix");
				String colon = messageConfig.get("Colon");
				String messageColor = messageConfig.get("MessageColor");
				String prefixSpace = "";
				String nameSpace = "";
				String colonSpace = "";
					
				if (prefix == null || name == null || suffix == null || colon == null || messageColor == null) {
					serverLog(Level.SEVERE,
							"ConsoleBroadcast: Config file is corrupted! Delete it and restart your server to generate it.");
					return true;
				}
				
				if (!prefix.equals("") && (!name.equals("") || !suffix.equals(""))) {
					prefixSpace = "{\"text\":\" \"},";
				}
				if (!name.equals("") && !suffix.equals("")) {
					nameSpace = "{\"text\":\" \"},";
				}
				if (!prefix.equals("")) {
					prefix = prefix + ",";
				}
				if (!name.equals("")) {
					name = name + ",";
				}
				if (!suffix.equals("")) {
					suffix = suffix + ",";
				}
				if (!colon.equals("")) {
					colon = colon + ",";
					colonSpace = "{\"text\":\" \"},";
				}
				if (messageColor.equals("")) {
					messageColor = "#ffffff";
				}

				String msg = "{\"text\":\"" + message.toString().replace('\\', '/') + "\",\"color\":\"" + messageColor
						+ "\"}";

				StringBuffer tell = new StringBuffer();
				tell.append("[").append(prefix).append(prefixSpace).append(name).append(nameSpace).append(suffix)
						.append(colon).append(colonSpace).append(msg).append("]");

				try {
					Bukkit.getServer().dispatchCommand(Bukkit.getConsoleSender(),
							"tellraw" + " " + recipient + " " + tell.toString());
					if (recipient.equals("@a")) {
						recipient = ChatColor.DARK_RED + "everyone";
					}
					sender.sendMessage(ChatColor.GREEN + "Sent \"" + ChatColor.GOLD + message.toString().replace('\\', '/') + ChatColor.GREEN + "\" to " + ChatColor.AQUA + recipient + ChatColor.GREEN + ".");
				} catch (CommandException e) {
					sender.sendMessage(
							ChatColor.RED + "This command threw exeption, you probably wrote recipent wrongly.");
					return false;
				}
			}
			return true;
		}
		return false;

	}

	private void prepareConfig() {
		String configFileName = "config.yml";
		File configFile = new File(getDataFolder(), configFileName);
		if (!(configFile.exists())) {
			try {
				configFile.createNewFile();
			} catch (IOException e) {
				serverLog(Level.SEVERE, "ConsoleBroadcast: Cannot create file \"" + configFileName + "\"!" + e);
			}
		}
		FileConfiguration configFileYaml = YamlConfiguration.loadConfiguration(configFile);
		String sectionName = "Messages";
		ConfigurationSection section = configFileYaml.getConfigurationSection(sectionName);
		if (section == null) {
			section = configFileYaml.createSection(sectionName);
		}
		Set<String> sectionKeys = section.getKeys(false);
		if (sectionKeys.size() == 0) {
			section.set("Prefix",
					"[{\"text\":\"[\",\"color\":\"dark_gray\"},{\"text\":\"Console\",\"italic\":true,\"color\":\"#FF00FF\"},{\"text\":\"]\",\"color\":\"dark_gray\"}]");
			section.set("Name", "{\"text\":\"Server\",\"color\":\"#A80001\"}");
			section.set("Suffix", "");
			section.set("Colon", "{\"text\":\":\",\"color\":\"#ffffff\"}");
			section.set("MessageColor", "#ffffff");
			try {
				configFileYaml.save(configFile);
			} catch (IOException e) {
				serverLog(Level.SEVERE, "ConsoleBroadcast: Cannot save file \"" + configFileName + "\"!" + e);
			}
		}
		sectionKeys = section.getKeys(false);
		for (String key : sectionKeys) {
			messageConfig.put(key, section.getString(key));
		}
	}

	private void serverLog(Level lvl, String msg) {
		Bukkit.getServer().getLogger().log(lvl, msg);
	}
}