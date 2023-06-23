package cz.jeme.programu.consolebroadcast;

import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.jetbrains.annotations.NotNull;

import java.util.Collections;
import java.util.List;

public class ConsoleBroadcastCommand extends Command {
    protected ConsoleBroadcastCommand() {
        super(
                "consolebroadcast",
                "Main ConsoleBroadcast command",
                "false",
                List.of("cb")
        );
        setPermission("consolebroadcast.consolebroadcast");
        register();
    }

    private void register() {
        Bukkit.getCommandMap().register("consolebroadcast", this);
    }

    @Override
    public boolean execute(@NotNull CommandSender sender, @NotNull String commandLabel, @NotNull String[] args) {
        if (args.length < 1) {
            sender.sendMessage(Messages.prefix("<red>Not enough arguments!</red>"));
            return true;
        }
        if (args[0].equals("reload")) {
            ConsoleBroadcast.reload();
            sender.sendMessage(Messages.prefix("<green>Config reloaded successfully!</green>"));
            return true;
        }
        sender.sendMessage(Messages.prefix("<red>Unknown command!</red>"));
        return true;
    }


    @Override
    public @NotNull List<String> tabComplete(@NotNull CommandSender sender, @NotNull String alias, @NotNull String[] args) throws IllegalArgumentException {
        if (args.length == 1) {
            return List.of("reload");
        }
        return Collections.emptyList();
    }
}
