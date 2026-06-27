package com.newttech.ntutils.command;

import com.newttech.ntutils.manager.ItemModifierManager;
import org.bukkit.command.*;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Player;

public class EnchantCommand implements CommandExecutor {

    private final ItemModifierManager manager;

    public EnchantCommand(ItemModifierManager manager) {
        this.manager = manager;
    }

    @Override
    public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {

        if (!(sender instanceof Player player)) return true;

        if (args.length < 2) {
            player.sendMessage("Usage: /ench <enchant> <level>");
            return true;
        }

        Enchantment ench = Enchantment.getByName(args[0].toUpperCase());
        if (ench == null) {
            player.sendMessage("Invalid enchant.");
            return true;
        }

        int level = Integer.parseInt(args[1]);

        manager.addEnchant(player, ench, level);

        player.sendMessage("Enchanted item.");
        return true;
    }
}