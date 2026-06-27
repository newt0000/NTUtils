package com.newttech.ntutils.manager;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.scheduler.BukkitTask;

import java.util.Map;
import java.util.Set;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;

public class AFKManager {

    private final JavaPlugin plugin;

    private final Map<UUID, Long> lastActivity = new ConcurrentHashMap<>();
    private final Set<UUID> afkPlayers = ConcurrentHashMap.newKeySet();

    private final long afkThresholdMillis;
    private BukkitTask task;

    public AFKManager(JavaPlugin plugin, long afkThresholdMillis) {
        this.plugin = plugin;
        this.afkThresholdMillis = afkThresholdMillis;

        startTask();
    }

    /* -------------------------
       Activity tracking
       ------------------------- */

    public void markActive(Player player) {
        UUID uuid = player.getUniqueId();

        lastActivity.put(uuid, System.currentTimeMillis());

        if (afkPlayers.contains(uuid)) {
            setAFK(player, false);
        }
    }

    public void markJoin(Player player) {
        markActive(player);
    }

    public void markQuit(Player player) {
        UUID uuid = player.getUniqueId();
        lastActivity.remove(uuid);
        afkPlayers.remove(uuid);
    }

    /* -------------------------
       AFK state
       ------------------------- */

    public boolean isAFK(Player player) {
        return afkPlayers.contains(player.getUniqueId());
    }

    private void setAFK(Player player, boolean afk) {
        UUID uuid = player.getUniqueId();

        if (afk) {
            afkPlayers.add(uuid);
            player.setPlayerListName("[AFK] " + player.getName());
        } else {
            afkPlayers.remove(uuid);
            player.setPlayerListName(player.getName());
        }
    }

    /* -------------------------
       Loop enforcement
       ------------------------- */

    private void startTask() {
        task = Bukkit.getScheduler().runTaskTimer(plugin, () -> {

            long now = System.currentTimeMillis();

            for (Player player : Bukkit.getOnlinePlayers()) {
                UUID uuid = player.getUniqueId();

                long last = lastActivity.getOrDefault(uuid, now);
                long idle = now - last;

                if (idle >= afkThresholdMillis) {
                    if (!afkPlayers.contains(uuid)) {
                        setAFK(player, true);
                    }
                } else {
                    if (afkPlayers.contains(uuid)) {
                        setAFK(player, false);
                    }
                }
            }

        }, 20L, 20L);
    }

    public void shutdown() {
        if (task != null) task.cancel();

        for (UUID uuid : afkPlayers) {
            Player player = Bukkit.getPlayer(uuid);
            if (player != null) {
                player.setPlayerListName(player.getName());
            }
        }

        afkPlayers.clear();
        lastActivity.clear();
    }
}