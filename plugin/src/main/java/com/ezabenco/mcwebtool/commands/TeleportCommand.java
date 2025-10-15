package com.ezabenco.mcwebtool.commands;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class TeleportCommand implements CommandExecutor {

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (args.length != 4) {
            sender.sendMessage("Usage: /teleport <player> <x> <y> <z>");
            return false;
        }

        Player target = Bukkit.getPlayerExact(args[0]);
        if (target == null) {
            sender.sendMessage("Player not found.");
            return false;
        }

        try {
            double x = Double.parseDouble(args[1]);
            double y = Double.parseDouble(args[2]);
            double z = Double.parseDouble(args[3]);
            Location loc = new Location(target.getWorld(), x, y, z);
            target.teleport(loc);
            sender.sendMessage("Teleported " + target.getName() + " to " + x + ", " + y + ", " + z);
        } catch (NumberFormatException e) {
            sender.sendMessage("Coordinates must be numbers.");
        }

        return true;
    }
}
