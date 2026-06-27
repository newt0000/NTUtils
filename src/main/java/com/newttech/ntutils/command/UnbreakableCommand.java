package com.newttech.ntutils.command;

import com.newttech.ntutils.manager.ItemModifierManager;
import org.bukkit.command.*;
import org.bukkit.entity.Player;

public class UnbreakableCommand implements CommandExecutor {

    private final ItemModifierManager manager;

    public UnbreakableCommand(ItemModifierManager manager) {
        this.manager = manager;
    }

    @Override
    public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {

        if (!(sender instanceof Player player)) return true;

        boolean value = args.length > 0 && Boolean.parseBoolean(args[0]);

        manager.setUnbreakable(player, value);

        player.sendMessage("Unbreakable set to " + value);
        return true;
    }
}