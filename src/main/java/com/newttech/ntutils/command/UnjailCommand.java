package com.newttech.ntutils.command;

import com.newttech.ntutils.manager.JailManager;
import org.bukkit.Bukkit;
import org.bukkit.command.*;
import org.bukkit.entity.Player;

public class UnjailCommand implements CommandExecutor {

    private final JailManager jailManager;

    public UnjailCommand(JailManager jailManager) {
        this.jailManager = jailManager;
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {

        if (args.length < 1) {
            sender.sendMessage("Usage: /unjail <player>");
            return true;
        }

        Player target = Bukkit.getPlayer(args[0]);
        if (target == null) {
            sender.sendMessage("Player not found.");
            return true;
        }

        jailManager.release(target);

        sender.sendMessage("Unjailed " + target.getName());
        return true;
    }
}