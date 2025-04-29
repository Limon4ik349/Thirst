package org.limon4ik.thirst;

import dev.lone.itemsadder.api.ItemsAdder;
import org.bukkit.Bukkit;
import org.bukkit.Color;
import org.bukkit.entity.Player;
import org.bukkit.event.Listener;
import org.bukkit.plugin.java.JavaPlugin;
import org.limon4ik.thirst.commands.ThirstCommand;

public final class Main extends JavaPlugin implements Listener
{
    public static Main instance;
    public static PlayerManager playerManager;

    @Override
    public synchronized void onEnable() {
        instance = this;

        if (ItemsAdder.areItemsLoaded())
        {
            getLogger().info("Thirst Plugin enabled!");
            playerManager = new PlayerManager(this);

            getServer().getPluginManager().registerEvents(new PlayerListener(playerManager), this);
            getServer().getPluginManager().registerEvents(new ActionListener(playerManager), this);

            new MainTask(playerManager).runTaskTimer(this, 0L, 60L);

            new ThirstCommand();
        }
        else
            Bukkit.getPluginManager().registerEvents(this, this);
    }

    @Override
    public void onDisable() {
        // Plugin shutdown logic
    }

    public static Main getInstance() {
        return instance;
    }
}
