package com.ezabenco.mcwebtool.commands;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import com.ezabenco.mcwebtool.listeners.PlayerEventListener;

public class UpdateCooldownCommand implements CommandExecutor {
    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (args.length == 1) {
            try {
                long newCooldown = Long.parseLong(args[0]);
                PlayerEventListener.setUpdateCooldownMs(newCooldown);
                sender.sendMessage("Update cooldown set to " + newCooldown + " ms");
                return true;
            } catch (NumberFormatException e) {
                sender.sendMessage("Please provide a valid number (in ms).");
            }
        } else {
            sender.sendMessage("Usage: /updatecooldown <milliseconds>");
        }
        return false;
    }
}
