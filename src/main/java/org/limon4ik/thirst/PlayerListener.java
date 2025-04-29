package org.limon4ik.thirst;

import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.jetbrains.annotations.NotNull;

public class PlayerListener implements Listener {
    private final PlayerManager players;

    public PlayerListener(PlayerManager playerManager) {
        this.players = playerManager;
    }

    @EventHandler
    public void onPlayerJoin(@NotNull PlayerJoinEvent event) {
        Player player = event.getPlayer();
        players.SetThirst(player, players.GetThirst(player));
        HudDrawer drawer = new HudDrawer(player);
        drawer.Draw(Math.round(players.GetThirst(player)));
        players.drawers.put(player.getUniqueId(), drawer);
    }

    @EventHandler
    public void onPlayerQuit(@NotNull PlayerQuitEvent event) {
        Player player = event.getPlayer();
        players.drawers.remove(player.getUniqueId());
        players.SetThirst(player, players.GetThirst(player));
    }
}
