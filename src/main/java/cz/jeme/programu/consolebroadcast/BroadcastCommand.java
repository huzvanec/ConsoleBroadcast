package cz.jeme.programu.consolebroadcast;

import net.kyori.adventure.audience.Audience;
import net.kyori.adventure.text.Component;
import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.command.ConsoleCommandSender;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class BroadcastCommand extends Command {

    private static final String EVERYONE_SELECTOR = "@everyone";
    private static final String MESSAGE_PLACEHOLDER = "{MESSAGE}";

    protected BroadcastCommand() {
        super(
                "broadcast",
                "Broadcast from console to chat",
                "false",
                List.of("console")
        );
        setPermission("consolebroadcast.broadcast");
        register();
    }

    private void register() {
        Bukkit.getCommandMap().register("consolebroadcast", this);
    }

    @Override
    public boolean execute(@NotNull CommandSender sender, @NotNull String commandLabel, @NotNull String[] args) {
        if (args.length < 2) {
            sender.sendMessage(Messages.prefix("<red>Not enough arguments!</red>"));
            return true;
        }
        List<Audience> recipients;
        if (args[0].equals(EVERYONE_SELECTOR)) {
            recipients = new ArrayList<>(Bukkit.getOnlinePlayers());
        } else {
            String playerName = args[0];
            Player recipient = Bukkit.getPlayerExact(playerName);
            if (recipient == null) {
                sender.sendMessage(Messages.prefix("<red>That player is not online!</red>"));
                return true;
            }
            recipients = List.of(recipient);
        }
        List<String> argsList = List.of(args);
        String messageContent = String.join(" ", argsList.subList(1, argsList.size()));

        String message = ConsoleBroadcast.config.getString("message");
        if (message == null) throw new NullPointerException("Message is null!");
        if (!sender.hasPermission("consolebroadcast.message-formatting")) {
            messageContent = Messages.strip(messageContent);
        }
        message = message.replace(MESSAGE_PLACEHOLDER, messageContent);

        Component component = Messages.from(message);

        for (Audience recipient : recipients) {
            recipient.sendMessage(component);
        }

        if (!(sender instanceof ConsoleCommandSender)) {
            sender.sendMessage(Messages.from("<italic><green>You -> " + args[0] + ": </green>" + message + "</italic>"));
        }
        Bukkit.getConsoleSender().sendMessage(Messages.from("<green>" + sender.getName() + " -> " + args[0] + ": </green>" + message));
        return true;
    }

    @Override
    public @NotNull List<String> tabComplete(@NotNull CommandSender sender, @NotNull String alias, @NotNull String[] args) throws IllegalArgumentException {
        if (args.length == 1) {
            List<String> recipients = new ArrayList<>();
            if (EVERYONE_SELECTOR.toLowerCase().contains(args[0].toLowerCase())) {
                recipients.add(EVERYONE_SELECTOR);
            }
            for (Player player : Bukkit.getOnlinePlayers()) {
                if (player.getName().toLowerCase().contains(args[0].toLowerCase())) {
                    recipients.add(player.getName());
                }
            }
            return recipients;
        }
        return Collections.emptyList();
    }
}