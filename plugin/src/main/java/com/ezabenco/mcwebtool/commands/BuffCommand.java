package com.ezabenco.mcwebtool.commands;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

import java.util.Map;

public class BuffCommand implements CommandExecutor {

    private static final Map<String, PotionEffectType> EFFECTS = Map.ofEntries(
            Map.entry("speed", PotionEffectType.SPEED),
            Map.entry("slowness", PotionEffectType.SLOWNESS),
            Map.entry("haste", PotionEffectType.HASTE),
            Map.entry("strength", PotionEffectType.STRENGTH),
            Map.entry("jump_boost", PotionEffectType.JUMP_BOOST),
            Map.entry("regeneration", PotionEffectType.REGENERATION),
            Map.entry("resistance", PotionEffectType.RESISTANCE),
            Map.entry("fire_resistance", PotionEffectType.FIRE_RESISTANCE),
            Map.entry("water_breathing", PotionEffectType.WATER_BREATHING),
            Map.entry("invisibility", PotionEffectType.INVISIBILITY),
            Map.entry("blindness", PotionEffectType.BLINDNESS),
            Map.entry("night_vision", PotionEffectType.NIGHT_VISION),
            Map.entry("poison", PotionEffectType.POISON),
            Map.entry("wither", PotionEffectType.WITHER)
    );

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (args.length < 2) {
            sender.sendMessage("Usage: /buff <player> <effect> [duration seconds]");
            return false;
        }

        Player target = sender.getServer().getPlayerExact(args[0]);
        if (target == null) {
            sender.sendMessage("Player not found.");
            return false;
        }

        String effectName = args[1].toLowerCase();
        PotionEffectType effectType = EFFECTS.get(effectName);

        if (effectType == null) {
            sender.sendMessage("Unknown effect type: " + args[1]);
            return false;
        }

        int durationSeconds = 30;
        if (args.length >= 3) {
            try {
                durationSeconds = Integer.parseInt(args[2]);
            } catch (NumberFormatException e) {
                sender.sendMessage("Invalid duration, using default 30 seconds.");
            }
        }

        PotionEffect effect = new PotionEffect(effectType, durationSeconds * 20, 0);
        target.addPotionEffect(effect);

        sender.sendMessage("Applied effect " + effectName + " to " + target.getName() + " for " + durationSeconds + " seconds.");
        return true;
    }
}
