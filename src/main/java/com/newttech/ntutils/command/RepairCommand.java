package com.newttech.ntutils.command;

import com.newttech.ntutils.manager.ItemModifierManager;
import org.bukkit.command.*;
import org.bukkit.entity.Player;

public class RepairCommand implements CommandExecutor {

    private final ItemModifierManager manager;

    public RepairCommand(ItemModifierManager manager) {
        this.manager = manager;
    }

    @Override
    public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {

        if (!(sender instanceof Player player)) return true;

        manager.repair(player);

        player.sendMessage("Item repaired.");
        return true;
    }
}