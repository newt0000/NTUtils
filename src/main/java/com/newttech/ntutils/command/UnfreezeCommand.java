package com.newttech.ntutils.command;

import com.newttech.ntutils.manager.FreezeManager;
import org.bukkit.Bukkit;
import org.bukkit.command.*;
import org.bukkit.entity.Player;

public class UnfreezeCommand implements CommandExecutor {

    private final FreezeManager freezeManager;

    public UnfreezeCommand(FreezeManager freezeManager) {
        this.freezeManager = freezeManager;
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {

        if (args.length < 1) {
            sender.sendMessage("Usage: /unfreeze <player>");
            return true;
        }

        Player target = Bukkit.getPlayer(args[0]);

        freezeManager.unfreeze(target);

        sender.sendMessage("Unfrozen " + target.getName());
        return true;
    }
}