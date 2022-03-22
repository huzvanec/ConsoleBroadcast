package cz.jeme.consolebroadcast;

import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandException;
import org.bukkit.command.CommandSender;
import org.bukkit.plugin.java.JavaPlugin;

public class ConsoleBroadcast extends JavaPlugin {

	@Override
	public void onEnable() {
	}

	@Override
	public void onDisable() {
	}

	@Override
	public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
		if (command.getName().equalsIgnoreCase("console")) {
			if (args.length < 2) {
				sender.sendMessage("This command needs 2 arguments! (Usage: /console <recipient> <message>)");
			} else {
				String recipient = args[0];
				StringBuffer message = new StringBuffer(30);
				for (int i = 1; i < args.length; i++) {
					message.append(args[i]);
					if (i != args.length - 1) {
						message.append(" ");
					}
				}
				try {
					Bukkit.getServer().dispatchCommand(Bukkit.getConsoleSender(), "tellraw " + recipient
							+ " [\"\",{\"text\":\"[\",\"color\":\"dark_gray\"},{\"text\":\"Terminal\",\"color\":\"green\"},{\"text\":\"] \",\"color\":\"dark_gray\"},{\"text\":\"Console\",\"bold\":true,\"color\":\"light_purple\"},{\"text\":\": " + message.toString() +"\"}]");
				} catch (CommandException e) {
					sender.sendMessage(
							"This command threw exeption, you probably wrote recipent wrongly. (Usage: /console <recipient> <message>)");
				}

			}
			return true;
		}
		return false;

	}

	public void registerEvents() {

	}

}