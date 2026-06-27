package com.newttech.ntutils.command;

import com.newttech.ntutils.manager.SeenManager;
import org.bukkit.Bukkit;
import org.bukkit.command.*;
import org.bukkit.entity.Player;

import java.util.UUID;

public class SeenCommand implements CommandExecutor {

    private final SeenManager seenManager;

    public SeenCommand(SeenManager seenManager) {
        this.seenManager = seenManager;
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {

        if (args.length < 1) {
            sender.sendMessage("Usage: /seen <player>");
            return true;
        }

        Player target = Bukkit.getPlayer(args[0]);

        if (target != null) {
            sender.sendMessage(target.getName() + " is currently online.");
            return true;
        }

        sender.sendMessage("Player not online.");
        return true;
    }
}