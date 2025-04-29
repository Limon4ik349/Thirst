package org.limon4ik.thirst;

import org.bukkit.NamespacedKey;
import org.bukkit.entity.Player;
import org.bukkit.persistence.PersistentDataType;
import org.bukkit.plugin.Plugin;

import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;

public class PlayerManager {
    private final Plugin plugin;
    private final NamespacedKey thirstKey;
    public final ConcurrentHashMap<UUID, HudDrawer> drawers = new ConcurrentHashMap<>();

    PlayerManager(Plugin plugin) {
        this.plugin = plugin;
        this.thirstKey = new NamespacedKey(plugin, "thirst");
    }

    public Float GetThirst(Player player)
    {
        return player.getPersistentDataContainer().getOrDefault(thirstKey, PersistentDataType.FLOAT, 20F);
    }

    public void SetThirst(Player player, Float thirst) {
        if (thirst > 20F)
            thirst = 20F;
        if (thirst < 0F)
            thirst = 0F;
        player.getPersistentDataContainer().set(thirstKey, PersistentDataType.FLOAT, thirst);
    }

    public void ChangeThirst(Player player, Float thirst) {
        float newThirst = GetThirst(player) + thirst;
        if (newThirst > 20F)
            newThirst = 20F;
        if (newThirst < 0F)
            newThirst = 0F;
        player.getPersistentDataContainer().set(thirstKey, PersistentDataType.FLOAT, newThirst);
    }
}
