package com.newttech.ntutils.command;

import org.bukkit.ChatColor;
import org.bukkit.command.*;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.Arrays;

public class ModifyCommand implements CommandExecutor, TabCompleter {

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {

        if (!(sender instanceof Player player)) {
            sender.sendMessage("Players only.");
            return true;
        }

        if (args.length < 2) {
            player.sendMessage("Usage: /modify <name|lore|flag> <value>");
            return true;
        }

        ItemStack item = player.getInventory().getItemInMainHand();

        if (item == null || item.getType().isAir()) {
            player.sendMessage("You must be holding an item.");
            return true;
        }

        ItemMeta meta = item.getItemMeta();
        if (meta == null) {
            player.sendMessage("This item cannot be modified.");
            return true;
        }

        String sub = args[0].toLowerCase();

        switch (sub) {

            case "name" -> {
                String name = ChatColor.translateAlternateColorCodes('&',
                        String.join(" ", Arrays.copyOfRange(args, 1, args.length)));

                meta.setDisplayName(name);
                item.setItemMeta(meta);

                player.sendMessage("Item name updated.");
            }

            case "lore" -> {
                String loreText = ChatColor.translateAlternateColorCodes('&',
                        String.join(" ", Arrays.copyOfRange(args, 1, args.length)));

                meta.setLore(Arrays.asList(loreText.split("\\\\n")));
                item.setItemMeta(meta);

                player.sendMessage("Item lore updated.");
            }

            case "flag" -> {
                try {
                    ItemFlag flag = ItemFlag.valueOf(args[1].toUpperCase());
                    meta.addItemFlags(flag);
                    item.setItemMeta(meta);

                    player.sendMessage("Flag added: " + flag.name());
                } catch (IllegalArgumentException e) {
                    player.sendMessage("Invalid flag.");
                }
            }

            default -> player.sendMessage("Usage: /modify <name|lore|flag> <value>");
        }

        return true;
    }

    /* -------------------------
       TAB COMPLETER
       ------------------------- */

    @Override
    public java.util.List<String> onTabComplete(CommandSender sender, Command command, String alias, String[] args) {

        if (args.length == 1) {
            return Arrays.asList("name", "lore", "flag");
        }

        if (args.length == 2 && args[0].equalsIgnoreCase("flag")) {
            return Arrays.stream(ItemFlag.values())
                    .map(Enum::name)
                    .toList();
        }

        return java.util.Collections.emptyList();
    }
}