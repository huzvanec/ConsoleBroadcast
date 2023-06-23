package cz.jeme.programu.consolebroadcast;

import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.minimessage.MiniMessage;

public final class Messages {
    private Messages() {
    }

    public static final MiniMessage MESSAGE = MiniMessage.miniMessage();
    public static final String PREFIX = "<dark_gray>[<gold>ConsoleBroadcast</gold>]: </dark_gray>";

    public static Component from(String string) {
        return MESSAGE.deserialize(string);
    }

    public static Component prefix(String string) {
        return from(PREFIX + string);
    }

    public static String strip(String string) {
        return MESSAGE.stripTags(string);
    }
}
