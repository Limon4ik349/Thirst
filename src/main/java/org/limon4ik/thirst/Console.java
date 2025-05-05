package org.limon4ik.thirst;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;

import static org.bukkit.Bukkit.getLogger;

public class Console {
    static String prefix = ChatColor.AQUA + "[Thirst] " + ChatColor.RESET;

    static void log(String message) {
        Bukkit.getConsoleSender().sendMessage(prefix + message);
    }

    static void error(String message) {
        getLogger().severe(message);
    }
}
