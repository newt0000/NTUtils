package com.newttech.ntutils.manager;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class AFKManager {

    private final JavaPlugin plugin;

    private final Map<UUID, Location> returnLocations = new HashMap<>();
    private final Map<UUID, Location> afkLocations = new HashMap<>();
    private final Map<UUID, Boolean> afkState = new HashMap<>();

    public AFKManager(JavaPlugin plugin) {
        this.plugin = plugin;
    }

    /* -------------------------
       Toggle AFK
       ------------------------- */

    public void toggle(Player player) {
        UUID uuid = player.getUniqueId();

        if (isAFK(player)) {
            disable(player);
        } else {
            enable(player);
        }
    }

    public boolean isAFK(Player player) {
        return afkState.getOrDefault(player.getUniqueId(), false);
    }

    /* -------------------------
       Enable AFK
       ------------------------- */

    public void enable(Player player) {

        UUID uuid = player.getUniqueId();

        // save return location
        returnLocations.put(uuid, player.getLocation().clone());

        // save AFK spot (config teleport handled in command or here)
        afkState.put(uuid, true);
    }

    /* -------------------------
       Disable AFK
       ------------------------- */

    public void disable(Player player) {

        UUID uuid = player.getUniqueId();

        Location returnLoc = returnLocations.remove(uuid);

        afkState.remove(uuid);

        if (returnLoc != null) {
            player.teleport(returnLoc);
        }
    }

    /* -------------------------
       Movement check
       ------------------------- */

    public void handleMove(Player player, Location from, Location to) {

        UUID uuid = player.getUniqueId();

        if (!isAFK(player)) return;

        if (from.distanceSquared(to) > 4) { // >2 blocks
            disable(player);
        }
    }

    /* -------------------------
       Chat break AFK
       ------------------------- */

    public void handleChat(Player player) {
        if (isAFK(player)) {
            disable(player);
        }
    }

    /* -------------------------
       AFK teleport target
       ------------------------- */

    public Location getAFKLocation() {
        return plugin.getConfig().getLocation("afk.location");
    }
    public void shutdown() {
        returnLocations.clear();
        afkLocations.clear();
        afkState.clear();
    }
}