package com.newttech.ntutils.manager;

import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

public class ItemModifierManager {

    public void repair(Player player) {
        ItemStack item = player.getInventory().getItemInMainHand();
        if (item == null) return;

        item.setDurability((short) 0);
    }

    public void setUnbreakable(Player player, boolean value) {
        ItemStack item = player.getInventory().getItemInMainHand();
        if (item == null) return;

        ItemMeta meta = item.getItemMeta();
        if (meta == null) return;

        meta.setUnbreakable(value);
        item.setItemMeta(meta);
    }

    public void addEnchant(Player player, Enchantment ench, int level) {
        ItemStack item = player.getInventory().getItemInMainHand();
        if (item == null) return;

        item.addUnsafeEnchantment(ench, level);
    }

    public void removeEnchant(Player player, Enchantment ench) {
        ItemStack item = player.getInventory().getItemInMainHand();
        if (item == null) return;

        item.removeEnchantment(ench);
    }

    public void clearEnchants(Player player) {
        ItemStack item = player.getInventory().getItemInMainHand();
        if (item == null) return;

        item.getEnchantments().keySet().forEach(item::removeEnchantment);
    }

    public void addFlag(Player player, ItemFlag flag) {
        ItemStack item = player.getInventory().getItemInMainHand();
        if (item == null) return;

        ItemMeta meta = item.getItemMeta();
        if (meta == null) return;

        meta.addItemFlags(flag);
        item.setItemMeta(meta);
    }
}