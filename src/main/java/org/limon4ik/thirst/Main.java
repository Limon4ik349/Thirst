package org.limon4ik.thirst;

import dev.lone.itemsadder.api.ItemsAdder;
import org.bukkit.Bukkit;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.event.Listener;
import org.bukkit.plugin.java.JavaPlugin;
import org.limon4ik.thirst.commands.ThirstCommand;

public final class Main extends JavaPlugin implements Listener
{
    public static Main instance;
    public static PlayerManager playerManager;
    public static FileConfiguration config;

    @Override
    public synchronized void onEnable() {
        instance = this;

        if (ItemsAdder.areItemsLoaded())
        {
            Console.log("Loading config...");
            saveDefaultConfig();
            reloadConfig();
            config = getConfig();

            Console.log("Loading players...");
            playerManager = new PlayerManager(this);
            for (Player player : Bukkit.getOnlinePlayers()) {
                playerManager.SetThirst(player, playerManager.GetThirst(player));
                HudDrawer drawer = new HudDrawer(player);
                drawer.Draw(Math.round(playerManager.GetThirst(player)));
                playerManager.drawers.put(player.getUniqueId(), drawer);
            }

            Console.log("Registering events...");
            getServer().getPluginManager().registerEvents(new PlayerListener(playerManager), this);
            getServer().getPluginManager().registerEvents(new ActionListener(playerManager), this);

            Console.log("Launching parallel processes...");
            new MainTask(playerManager).runTaskTimer(this, 60L, 60L);

            Console.log("Loading commands and placeholders...");
            new ThirstCommand();
            new ThirstPlaceholder().register();

            Console.log("Thirst successfully enabled!");
        }
        else
            Bukkit.getPluginManager().registerEvents(this, this);
    }

    @Override
    public void onDisable() {
        saveConfig();
    }

    public void reloadPlugin() {
        saveDefaultConfig();
        reloadConfig();
        config = getConfig();
    }

    public static Main getInstance() {
        return instance;
    }
}
