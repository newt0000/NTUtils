package com.newttech.ntutils.command;

import com.newttech.ntutils.manager.FreezeManager;
import org.bukkit.Bukkit;
import org.bukkit.command.*;
import org.bukkit.entity.Player;

public class FreezeCommand implements CommandExecutor {

    private final FreezeManager freezeManager;

    public FreezeCommand(FreezeManager freezeManager) {
        this.freezeManager = freezeManager;
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {

        if (args.length < 2) {
            sender.sendMessage("Usage: /freeze <player> <seconds>");
            return true;
        }

        Player target = Bukkit.getPlayer(args[0]);
        long time = Long.parseLong(args[1]) * 1000L;

        freezeManager.freeze(target, "Staff freeze", time);

        sender.sendMessage("Frozen " + target.getName());
        return true;
    }
}