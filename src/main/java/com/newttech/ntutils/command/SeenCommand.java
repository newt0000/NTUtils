package com.newttech.ntutils.command;

import com.newttech.ntutils.manager.SeenManager;
import org.bukkit.Bukkit;
import org.bukkit.command.*;
import org.bukkit.entity.Player;

public class SeenCommand implements CommandExecutor {

    private final SeenManager seenManager;

    public SeenCommand(SeenManager seenManager) {
        this.seenManager = seenManager;
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {

        if (args.length < 2) {
            sender.sendMessage("Usage: /seen <player> <true|false>");
            return true;
        }

        Player target = Bukkit.getPlayer(args[0]);

        if (target == null) {
            sender.sendMessage("Player not found or not online.");
            return true;
        }

        boolean enable;

        if (args[1].equalsIgnoreCase("true")) {
            enable = true;
        } else if (args[1].equalsIgnoreCase("false")) {
            enable = false;
        } else {
            sender.sendMessage("Use true or false.");
            return true;
        }

        if (enable) {
            seenManager.enableSeen(target);
            sender.sendMessage(target.getName() + " is now visible (glow enabled).");
        } else {
            seenManager.disableSeen(target);
            sender.sendMessage(target.getName() + " is now hidden (glow disabled).");
        }

        return true;
    }
}