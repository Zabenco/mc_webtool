package com.ezabenco.mcwebtool.listeners;

import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityPotionEffectEvent;
import org.bukkit.entity.Player;

public class BuffListener implements Listener {

    @EventHandler
    public void onPotionEffectChange(EntityPotionEffectEvent event) {
        if (!(event.getEntity() instanceof Player player)) return;

        String effectName = event.getModifiedType().getKey().getKey();
        player.sendMessage("Potion effect changed: " + effectName);
    }
}
