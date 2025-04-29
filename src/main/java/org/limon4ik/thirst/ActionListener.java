package org.limon4ik.thirst;

import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.*;
import org.bukkit.event.player.PlayerItemConsumeEvent;
import org.bukkit.event.player.PlayerMoveEvent;
import org.bukkit.event.player.PlayerToggleSprintEvent;
import org.bukkit.inventory.ItemStack;

import java.util.HashSet;
import java.util.Set;
import java.util.UUID;

public class ActionListener implements Listener {
    private final PlayerManager players;

    public ActionListener(PlayerManager players) {
        this.players = players;
    }

    @EventHandler
    public void onSprint(PlayerToggleSprintEvent event) {
        if (event.isSprinting()) {
            Player player = event.getPlayer();
            players.ChangeThirst(player, -0.0025F);
            players.drawers.get(player.getUniqueId()).Draw(Math.round(players.GetThirst(player)));
        }
    }

    @EventHandler
    public void onAttack(EntityDamageByEntityEvent event) {
        if (event.getDamager() instanceof Player player) {
            players.ChangeThirst(player, -0.005F);
            players.drawers.get(player.getUniqueId()).Draw(Math.round(players.GetThirst(player)));
        }
    }

    @EventHandler
    public void onMove(PlayerMoveEvent event) {
        Player player = event.getPlayer();
        players.ChangeThirst(player, -0.00005F);
        players.drawers.get(player.getUniqueId()).Draw(Math.round(players.GetThirst(player)));
    }

    @EventHandler
    public void onHealthRegen(EntityRegainHealthEvent event) {
        if (event.getEntity() instanceof Player player) {

            Float thirst = players.GetThirst(player);
            float multiplier = 1F;
            if (thirst < 5) {
                return;
            } else if (thirst < 10) {
                multiplier = 0.5F;
            } else if (thirst < 15) {
                multiplier = 0.8F;
            }

            event.setAmount(event.getAmount() * multiplier);
            players.ChangeThirst(player, -0.15F);
            players.drawers.get(player.getUniqueId()).Draw(Math.round(players.GetThirst(player)));
        }
    }

    @EventHandler
    public void onHungerChange(FoodLevelChangeEvent event) {
        if (event.getEntity() instanceof Player player) {
            if (event.getFoodLevel() > player.getFoodLevel()) {
                players.ChangeThirst(player, -0.1F);
                players.drawers.get(player.getUniqueId()).Draw(Math.round(players.GetThirst(player)));
            }
        }
    }

    @EventHandler
    public void onPotionDrink(PlayerItemConsumeEvent event) {
        Player player = event.getPlayer();
        ItemStack item = event.getItem();

        switch (item.getType()) {
            case Material.BEETROOT_SOUP, Material.MUSHROOM_STEW -> players.ChangeThirst(player, 3F);
            case Material.POTION -> players.ChangeThirst(player, 5F);
            case Material.MILK_BUCKET -> players.ChangeThirst(player, 10F);
            case Material.HONEY_BOTTLE -> players.ChangeThirst(player, -4F);
        }
    }

    @EventHandler
    public void onDeath(PlayerDeathEvent event) {
        Player player = event.getEntity();
        players.SetThirst(player, 20F);
        players.drawers.get(player.getUniqueId()).Draw(Math.round(players.GetThirst(player)));
    }
}
