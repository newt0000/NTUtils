package com.newttech.ntutils.manager;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.scheduler.BukkitTask;

import java.util.Map;
import java.util.Set;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;

public class FreezeManager {

    private final JavaPlugin plugin;

    private final Map<UUID, FreezeData> frozenPlayers = new ConcurrentHashMap<>();
    private BukkitTask freezeTask;

    public FreezeManager(JavaPlugin plugin) {
        this.plugin = plugin;
        startFreezeTask();
    }

    /* -------------------------
       Freeze control
       ------------------------- */

    public void freeze(Player player, String reason, long durationMillis) {
        UUID uuid = player.getUniqueId();

        FreezeData data = new FreezeData();
        data.reason = reason;
        data.freezeUntil = System.currentTimeMillis() + durationMillis;
        data.lastLocation = player.getLocation();

        frozenPlayers.put(uuid, data);
    }

    public void unfreeze(Player player) {
        frozenPlayers.remove(player.getUniqueId());
    }

    public boolean isFrozen(Player player) {
        return frozenPlayers.containsKey(player.getUniqueId());
    }

    public long getRemainingTime(Player player) {
        FreezeData data = frozenPlayers.get(player.getUniqueId());
        if (data == null) return 0;

        return Math.max(0, data.freezeUntil - System.currentTimeMillis());
    }

    public String getReason(Player player) {
        FreezeData data = frozenPlayers.get(player.getUniqueId());
        return data != null ? data.reason : null;
    }

    /* -------------------------
       Enforcement task
       ------------------------- */

    private void startFreezeTask() {
        freezeTask = Bukkit.getScheduler().runTaskTimer(plugin, () -> {

            for (Map.Entry<UUID, FreezeData> entry : frozenPlayers.entrySet()) {

                UUID uuid = entry.getKey();
                FreezeData data = entry.getValue();

                Player player = Bukkit.getPlayer(uuid);

                // expire freeze
                if (System.currentTimeMillis() > data.freezeUntil) {
                    frozenPlayers.remove(uuid);
                    continue;
                }

                if (player == null || !player.isOnline()) continue;

                // keep player locked in place
                Location loc = data.lastLocation;

                if (loc != null) {
                    player.teleport(loc);
                }

                // constantly refresh last position so they don’t drift
                data.lastLocation = player.getLocation();
            }

        }, 1L, 1L);
    }

    public void shutdown() {
        if (freezeTask != null) freezeTask.cancel();
        frozenPlayers.clear();
    }

    /* -------------------------
       Internal data
       ------------------------- */

    private static class FreezeData {
        String reason;
        long freezeUntil;
        Location lastLocation;
    }
}