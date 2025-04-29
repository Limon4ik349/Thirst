package org.limon4ik.thirst;

import org.bukkit.Bukkit;
import org.bukkit.GameMode;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;

public class MainTask extends BukkitRunnable {
    private final PlayerManager playerManager;

    public MainTask(PlayerManager playerManager) {
        this.playerManager = playerManager;
    }

    @Override
    public void run() {
        for (Player player : Bukkit.getOnlinePlayers()) {
            if (player != null) {
                Float thirst = playerManager.GetThirst(player);
                if (thirst <= 0) {
                    if (player.getHealth() > 1.0) {
                        player.damage(1.0);
                    }
                }
            }
        }
    }
}
