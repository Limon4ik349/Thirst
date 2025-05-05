package org.limon4ik.thirst;

import me.clip.placeholderapi.expansion.PlaceholderExpansion;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

public class ThirstPlaceholder extends PlaceholderExpansion {

    @Override
    public @NotNull String getIdentifier() {
        return "thirst";
    }

    @Override
    public @NotNull String getAuthor() {
        return "Limon4ik349";
    }

    @Override
    public @NotNull String getVersion() {
        return "1.1";
    }

    @Override
    public String onPlaceholderRequest(Player player, @NotNull String params) {
        if (player == null) {
            return "";
        }

        if (params.equalsIgnoreCase("")) {
            float thirst = Main.playerManager.GetThirst(player);
            return String.valueOf(Math.round(thirst));
        }

        return null;
    }
}
