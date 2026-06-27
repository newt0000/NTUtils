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
            sender.sendMessage("Only players can use this.");
            return true;
        }

        afkManager.markActive(player);

        player.sendMessage("AFK status refreshed.");
        return true;
    }
}