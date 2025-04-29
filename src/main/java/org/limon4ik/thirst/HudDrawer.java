package org.limon4ik.thirst;

import dev.lone.itemsadder.api.FontImages.FontImageWrapper;
import dev.lone.itemsadder.api.FontImages.PlayerCustomHudWrapper;
import dev.lone.itemsadder.api.FontImages.PlayerHudsHolderWrapper;
import org.bukkit.GameMode;
import org.bukkit.entity.Player;

public class HudDrawer
{
    private final PlayerHudsHolderWrapper holder;
    private final PlayerCustomHudWrapper thirstHud;

    public HudDrawer(Player player) {
        holder = new PlayerHudsHolderWrapper(player);
        thirstHud = new PlayerCustomHudWrapper(holder, "thirst:thirst_bar");
    }

    public void Draw(Integer value)
    {
        int fixed = value / 2;
        int half = value % 2;
        int negative = 10 - fixed - half;
        thirstHud.clearFontImagesAndRefresh();
        thirstHud.setVisible(holder.getPlayer().getGameMode() != GameMode.CREATIVE);
        thirstHud.setOffsetX(4);
        for (int i = 0; i < negative; i++) {
            thirstHud.addFontImage(new FontImageWrapper("thirst:thirst_bar_negative"));
        }
        for (int i = 0; i < half; i++) {
            thirstHud.addFontImage(new FontImageWrapper("thirst:thirst_bar_half"));
        }
        for (int i = 0; i < fixed; i++) {
            thirstHud.addFontImage(new FontImageWrapper("thirst:thirst_bar_positive"));
        }
    }
}
