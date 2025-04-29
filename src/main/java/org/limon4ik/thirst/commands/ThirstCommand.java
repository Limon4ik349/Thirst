package org.limon4ik.thirst.commands;

import dev.lone.itemsadder.api.FontImages.FontImageWrapper;
import me.clip.placeholderapi.PlaceholderAPI;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;
import net.kyori.adventure.text.minimessage.MiniMessage;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Color;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.limon4ik.thirst.HudDrawer;
import org.limon4ik.thirst.Main;
import org.limon4ik.thirst.PlayerManager;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class ThirstCommand extends AbstractCommand {

    public ThirstCommand() {
        super("thirst");
    }

    @Override
    public void execute(CommandSender sender, String label, String[] args) {

        if (args.length == 1 && args[0].equalsIgnoreCase("help")) {
            sender.sendMessage(Component.text("Thirst", NamedTextColor.AQUA));
            return;
        }

        if (args.length == 2 && args[0].equalsIgnoreCase("get")) {
            if (!(sender.hasPermission("thirst.admin"))) {
                sender.sendMessage(ChatColor.RED + "You do not have permission to use this command.");
                return;
            }
            Player target = Bukkit.getPlayer(args[1]);
            if (target == null) {
                sender.sendMessage(ChatColor.RED + "Player not found.");
                return;
            }

            Float thirst = Main.playerManager.GetThirst(target);
            int fixed = Math.round(thirst) / 2;
            int half = Math.round(thirst) % 2;
            int negative = 10 - fixed - half;

            StringBuilder msg = new StringBuilder("<gradient:#00e4ff:#0099cc>[ %img_offset_-2%%img_thirst_positive% ]</gradient> " +
                    "<gray><italic>" + target.getName() + "</italic></gray>'s thirst: " +
                    "<bold><aqua>" + Math.round(thirst * 100.0) / 100.0 + "</aqua></bold> (%img_offset_-1%");
            msg.append("%img_thirst_negative%%img_offset_1%".repeat(Math.max(0, negative)));
            msg.append("%img_thirst_half%%img_offset_1%".repeat(Math.max(0, half)));
            msg.append("%img_thirst_positive%%img_offset_1%".repeat(Math.max(0, fixed)));
            msg.append(")");

            Component component = MiniMessage.miniMessage().deserialize(PlaceholderAPI.setPlaceholders(target, msg.toString()));

            sender.sendMessage(component);
            return;
        }

        if (args.length == 3) {
            if (!(sender.hasPermission("thirst.admin"))) {
                sender.sendMessage(ChatColor.RED + "You do not have permission to use this command.");
                return;
            }

            if (args[0].equalsIgnoreCase("set")) {
                Player target = Bukkit.getPlayer(args[1]);
                if (target == null) {
                    sender.sendMessage(ChatColor.RED + "Player not found.");
                    return;
                }
                Main.playerManager.SetThirst(target, Float.parseFloat(args[2]));
                Main.playerManager.drawers.get(target.getUniqueId()).Draw(Math.round(Main.playerManager.GetThirst(target)));
                return;
            }

            sender.sendMessage(ChatColor.RED + "Usage: /thirst [set|get] <player> <amount>");
            return;
        }

        sender.sendMessage(ChatColor.RED + "Usage: /thirst help");
    }

    @Override
    public List<String> tabComplete(CommandSender sender, String alias, String[] args)
    {
        if (args.length == 1) {
            if (sender.hasPermission("thirst.admin"))
                return Arrays.asList("help", "set", "get");
            return List.of("help");
        }

        if (args.length == 2) {
            if (args[0].equalsIgnoreCase("set") || args[0].equalsIgnoreCase("get"))
                return Bukkit.getOnlinePlayers().stream().map(Player::getName).toList();
        }

        if (args.length == 3) {
            if (args[0].equalsIgnoreCase("set"))
                return Arrays.asList("20", "10", "0");
        }

        return List.of();
    }
}
