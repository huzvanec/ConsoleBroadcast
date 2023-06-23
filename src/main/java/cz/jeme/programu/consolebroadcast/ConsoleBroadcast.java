package cz.jeme.programu.consolebroadcast;

import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.plugin.java.JavaPlugin;

public class ConsoleBroadcast extends JavaPlugin {

    public static FileConfiguration config = null;


    @Override
    public void onEnable() {
        saveDefaultConfig();
        new BroadcastCommand(); // register the /broadcast command
        new ConsoleBroadcastCommand(); // register the /consolebroadcast command
        reload();
    }

    public static void reload() {
        ConsoleBroadcast plugin = ConsoleBroadcast.getPlugin(ConsoleBroadcast.class);
        plugin.reloadConfig();
        config = plugin.getConfig();
    }
}