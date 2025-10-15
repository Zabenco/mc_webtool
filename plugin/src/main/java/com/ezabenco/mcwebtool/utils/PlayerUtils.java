package com.ezabenco.mcwebtool.utils;

import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class PlayerUtils {

    public static List<String> getInventoryItemStacks(Player player) {
        List<String> itemStacks = new ArrayList<>();
        for (ItemStack item : player.getInventory().getContents()) {
            if (item != null) {
                String itemName = item.getType().name();
                int amount = item.getAmount();
                itemStacks.add(amount + "x " + itemName);
            }
        }
        return itemStacks;
    }

    public static List<String> getActiveEffectKeys(Player player) {
        return player.getActivePotionEffects().stream()
                .map(effect -> effect.getType().getKey().getKey())
                .collect(Collectors.toList());
    }

    public static String getStatsSummary(Player player) {
        double maxHealth = player.getMaxHealth();
        StringBuilder sb = new StringBuilder();
        sb.append("Health: ").append(player.getHealth()).append("/");
        sb.append(maxHealth).append(", ");
        sb.append("Food Level: ").append(player.getFoodLevel()).append(", ");
        sb.append("Level: ").append(player.getLevel());
        return sb.toString();
    }
}
