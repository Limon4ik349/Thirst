package org.limon4ik.thirst;

import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.*;
import org.bukkit.event.player.PlayerItemConsumeEvent;
import org.bukkit.event.player.PlayerMoveEvent;
import org.bukkit.event.player.PlayerToggleSprintEvent;
import org.bukkit.inventory.ItemStack;

import static org.bukkit.Bukkit.getLogger;
import static org.limon4ik.thirst.Main.config;

public class ActionListener implements Listener {
    private final PlayerManager players;

    public ActionListener(PlayerManager players) {
        this.players = players;
    }

    @EventHandler
    public void onSprint(PlayerToggleSprintEvent event) {
        Player player = event.getPlayer();
        if (event.isSprinting()) {
            if (players.GetThirst(player) <= 4.5F) {
                Bukkit.getScheduler().runTask(Main.getInstance(), () -> {
                    player.setSprinting(false);
                });
                return;
            }
            players.ChangeThirst(player, (float) -config.getDouble("thirst-loss.sprinting", 0.0025));
            players.drawers.get(player.getUniqueId()).Draw(Math.round(players.GetThirst(player)));
        }
    }



    @EventHandler
    public void onAttack(EntityDamageByEntityEvent event) {
        if (event.getDamager() instanceof Player player) {
            players.ChangeThirst(player, (float) -config.getDouble("thirst-loss.attacking", 0.005));
            players.drawers.get(player.getUniqueId()).Draw(Math.round(players.GetThirst(player)));
        }
    }

    @EventHandler
    public void onMove(PlayerMoveEvent event) {
        Player player = event.getPlayer();
        players.ChangeThirst(player, (float) -config.getDouble("thirst-loss.moving", 0.00005));
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
            players.ChangeThirst(player, (float) -config.getDouble("thirst-loss.regeneration", 0.15));
            players.drawers.get(player.getUniqueId()).Draw(Math.round(players.GetThirst(player)));
        }
    }

    @EventHandler
    public void onHungerChange(FoodLevelChangeEvent event) {
        if (event.getEntity() instanceof Player player) {
            if (event.getFoodLevel() > player.getFoodLevel()) {
                players.ChangeThirst(player, (float) -config.getDouble("thirst-loss.eating", 0.1));
                players.drawers.get(player.getUniqueId()).Draw(Math.round(players.GetThirst(player)));
            }
        }
    }

    @EventHandler
    public void onPotionDrink(PlayerItemConsumeEvent event) {
        Player player = event.getPlayer();
        ItemStack item = event.getItem();

        switch (item.getType()) {
            case Material.BEETROOT_SOUP, Material.MUSHROOM_STEW -> players.ChangeThirst(player, (float) config.getDouble("thirst-gain.soup", 3));
            case Material.POTION -> players.ChangeThirst(player, (float) config.getDouble("thirst-gain.potion", 5));
            case Material.MILK_BUCKET -> players.ChangeThirst(player, (float) config.getDouble("thirst-gain.milk", 10));
            case Material.HONEY_BOTTLE -> players.ChangeThirst(player, (float) -config.getDouble("thirst-loss.honey", 4));
        }
    }

    @EventHandler
    public void onDeath(PlayerDeathEvent event) {
        Player player = event.getEntity();
        players.SetThirst(player, (float) config.getDouble("thirst-default", 20));
        players.drawers.get(player.getUniqueId()).Draw(Math.round(players.GetThirst(player)));
    }
}
