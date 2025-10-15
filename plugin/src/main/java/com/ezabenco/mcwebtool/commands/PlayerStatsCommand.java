package com.ezabenco.mcwebtool.commands;

import com.ezabenco.mcwebtool.utils.PlayerUtils;
import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class PlayerStatsCommand implements CommandExecutor {

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (args.length != 1) {
            sender.sendMessage("Usage: /playerstats <player>");
            return false;
        }

        Player target = Bukkit.getPlayerExact(args[0]);
        if (target == null) {
            sender.sendMessage("Player not found.");
            return false;
        }

        String stats = PlayerUtils.getStatsSummary(target);
        sender.sendMessage("Stats for " + target.getName() + ": " + stats);

        sender.sendMessage("Inventory items: " + String.join(", ", PlayerUtils.getInventoryItemStacks(target)));
        sender.sendMessage("Active effects: " + String.join(", ", PlayerUtils.getActiveEffectKeys(target)));
        return true;
    }
}
