package org.limon4ik.thirst.commands;

import me.clip.placeholderapi.PlaceholderAPI;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.minimessage.MiniMessage;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.limon4ik.thirst.Main;

import java.util.Arrays;
import java.util.List;

public class ThirstCommand extends AbstractCommand {

    public ThirstCommand() {
        super("thirst");
    }

    @Override
    public void execute(CommandSender sender, String label, String[] args) {

        if (args.length == 1) {
            if (args[0].equalsIgnoreCase("help")) {
                StringBuilder msg = new StringBuilder(
                        "<gradient:#00e4ff:#0099cc>[ %img_offset_-2%%img_thirst_positive% ]</gradient> <bold><color:#00e4ff>Жажда!</color></bold>\n" +
                                "\n" +
                                "<bold><color:#00cfff>Как восстановить жажду?</color></bold>\n" +
                                "  <white>Пейте <aqua>воду</aqua>, <green>зелья</green>, <yellow>молоко</yellow> или ешьте <gold>супы</gold>.</white>\n" +
                                "\n" +
                                "<bold><color:#00cfff>На что влияет жажда?</color></bold>\n" +
                                "  <white>Чем ниже уровень воды, тем <red>медленнее</red> вы восстанавливаете здоровье и передвигаетесь.</white>\n" +
                                "  <white>При 0% жажды вы <red>получаете урон</red>, но не можете <gray>умереть</gray> от этого.</white>\n" +
                                "\n" +
                                "<bold><color:#00cfff>Что вызывает потерю жажды?</color></bold>\n" +
                                "  <white>• <gray>Бег</gray>, <gray>движение</gray> и <gray>атаки</gray> тратят воду.</white>\n" +
                                "  <white>• <gray>Восстановление здоровья</gray> ускоряет обезвоживание.</white>\n" +
                                "  <white>• <gray>Приём пищи</gray> также немного снижает уровень воды.</white>\n" +
                                "\n" +
                                "<bold><color:#00cfff>Другие моменты:</color></bold>\n" +
                                "  <white>• После <gray>смерти</gray> уровень жажды сбрасывается.</white>\n" +
                                "  <white>• Некоторые напитки (например, мёд) <red>увеличивают жажду</red>, а не утоляют её.</white>\n"
                );


                Component component = MiniMessage.miniMessage().deserialize(PlaceholderAPI.setPlaceholders(null, msg.toString()));

                sender.sendMessage(component);
                return;
            }

            if (args[0].equalsIgnoreCase("reload")) {
                if (!sender.hasPermission("thirst.admin")) {
                    sender.sendMessage(ChatColor.RED + "У вас нет прав на выполенение этой команды.");
                    return;
                }
                Main.getInstance().reloadPlugin();
                StringBuilder msg = new StringBuilder("<gradient:#00e4ff:#0099cc>[ %img_offset_-2%%img_thirst_positive% ]</gradient> " +
                        "<italic>Файл конфигурации успешно перезагружен.</italic>");
                Component component = MiniMessage.miniMessage().deserialize(PlaceholderAPI.setPlaceholders(null, msg.toString()));

                sender.sendMessage(component);
                return;
            }
        }

        if (args.length == 2 && args[0].equalsIgnoreCase("get")) {
            if (!(sender.hasPermission("thirst.admin"))) {
                sender.sendMessage(ChatColor.RED + "У вас нет прав на выполенение этой команды.");
                return;
            }
            Player target = Bukkit.getPlayer(args[1]);
            if (target == null) {
                sender.sendMessage(ChatColor.RED + "Игрок с таким именем не найден.");
                return;
            }

            Float thirst = Main.playerManager.GetThirst(target);
            int fixed = Math.round(thirst) / 2;
            int half = Math.round(thirst) % 2;
            int negative = 10 - fixed - half;

            StringBuilder msg = new StringBuilder("<gradient:#00e4ff:#0099cc>[ %img_offset_-2%%img_thirst_positive% ]</gradient> " +
                    "Уровень жажды <gray><italic>" + target.getName() + "</italic></gray>: " +
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
                sender.sendMessage(ChatColor.RED + "У вас нет прав на выполенение этой команды.");
                return;
            }

            if (args[0].equalsIgnoreCase("set")) {
                Player target = Bukkit.getPlayer(args[1]);
                if (target == null) {
                    sender.sendMessage(ChatColor.RED + "Игрок с таким именем не найден.");
                    return;
                }
                Main.playerManager.SetThirst(target, Float.parseFloat(args[2]));
                Main.playerManager.drawers.get(target.getUniqueId()).Draw(Math.round(Main.playerManager.GetThirst(target)));

                StringBuilder msg = new StringBuilder("<gradient:#00e4ff:#0099cc>[ %img_offset_-2%%img_thirst_positive% ]</gradient> " +
                        "Значение жажды для <gray><italic>" + target.getName() + "</italic></gray> установлено.");
                Component component = MiniMessage.miniMessage().deserialize(PlaceholderAPI.setPlaceholders(null, msg.toString()));

                sender.sendMessage(component);

                return;
            }

            sender.sendMessage(ChatColor.RED + "Использование: /thirst [set|get] <player> <amount>");
            return;
        }

        sender.sendMessage(ChatColor.RED + "Использование: /thirst help");
    }

    @Override
    public List<String> tabComplete(CommandSender sender, String alias, String[] args)
    {
        if (args.length == 1) {
            if (sender.hasPermission("thirst.admin"))
                return Arrays.asList("help", "set", "get", "reload");
            return List.of("help");
        }

        if (args.length == 2 && sender.hasPermission("thirst.admin")) {
            if (args[0].equalsIgnoreCase("set") || args[0].equalsIgnoreCase("get"))
                return Bukkit.getOnlinePlayers().stream().map(Player::getName).toList();
        }

        if (args.length == 3 && sender.hasPermission("thirst.admin")) {
            if (args[0].equalsIgnoreCase("set"))
                return Arrays.asList("20", "10", "0");
        }

        return List.of();
    }
}
