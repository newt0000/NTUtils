package com.newttech.ntutils.command;

import com.newttech.ntutils.manager.ItemModifierManager;
import org.bukkit.command.*;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Player;

public class DenchCommand implements CommandExecutor {

    private final ItemModifierManager manager;

    public DenchCommand(ItemModifierManager manager) {
        this.manager = manager;
    }

    @Override
    public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {

        if (!(sender instanceof Player player)) return true;

        if (args.length < 1) {
            manager.clearEnchants(player);
            player.sendMessage("All enchants removed.");
            return true;
        }

        Enchantment ench = Enchantment.getByName(args[0].toUpperCase());
        if (ench == null) {
            player.sendMessage("Invalid enchant.");
            return true;
        }

        manager.removeEnchant(player, ench);

        player.sendMessage("Enchant removed.");
        return true;
    }
}