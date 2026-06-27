package com.newttech.ntutils.command;

import com.newttech.ntutils.manager.AFKManager;
import org.bukkit.command.*;
import org.bukkit.entity.Player;

public class AFKCommand implements CommandExecutor {

    private final AFKManager afkManager;

    public AFKCommand(AFKManager afkManager) {
        this.afkManager = afkManager;
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {

        if (!(sender instanceof Player player)) {
            sender.sendMessage("Players only.");
            return true;
        }

        if (afkManager.isAFK(player)) {
            afkManager.disable(player);
            player.sendMessage("AFK disabled.");
        } else {
            afkManager.enable(player);

            player.teleport(afkManager.getAFKLocation());

            player.sendMessage("AFK enabled.");
        }

        return true;
    }
}