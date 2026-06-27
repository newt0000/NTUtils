package com.newttech.ntutils.manager;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitTask;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

public class JailManager {

    private final JavaPlugin plugin;
    private final FileConfiguration config;
    private final ConvictionManager convictionManager;

    private final Map<UUID, JailData> jailedPlayers = new ConcurrentHashMap<>();
    private BukkitTask jailTask;

    public JailManager(JavaPlugin plugin,
                       FileConfiguration config,
                       ConvictionManager convictionManager) {
        this.plugin = plugin;
        this.config = config;
        this.convictionManager = convictionManager;

        startJailTask();
    }

    /* -------------------------
       Jail control
       ------------------------- */

    public void jail(Player player, String reason, long durationMillis) {
        UUID uuid = player.getUniqueId();

        convictionManager.convict(uuid, reason, durationMillis);

        JailData data = new JailData();
        data.reason = reason;
        data.jailUntil = System.currentTimeMillis() + durationMillis;
        data.lastLocation = player.getLocation();

        jailedPlayers.put(uuid, data);

        teleportToJail(player);
    }

    public void release(Player player) {
        UUID uuid = player.getUniqueId();

        jailedPlayers.remove(uuid);
        convictionManager.release(uuid);

        if (player.isOnline()) {
            teleportToRelease(player);
        }
    }

    public boolean isJailed(Player player) {
        return jailedPlayers.containsKey(player.getUniqueId());
    }

    public long getRemainingTime(Player player) {
        JailData data = jailedPlayers.get(player.getUniqueId());
        if (data == null) return 0;

        return Math.max(0, data.jailUntil - System.currentTimeMillis());
    }

    public String getReason(Player player) {
        JailData data = jailedPlayers.get(player.getUniqueId());
        return data != null ? data.reason : null;
    }

    /* -------------------------
       Teleport logic
       ------------------------- */

    private void teleportToJail(Player player) {
        Location jailLoc = getJailLocation();
        if (jailLoc != null) {
            player.teleport(jailLoc);
        }
    }

    private void teleportToRelease(Player player) {
        Location release = getReleaseLocation();
        if (release != null) {
            player.teleport(release);
        }
    }

    private Location getJailLocation() {
        if (!config.contains("jail.location")) return null;

        World world = Bukkit.getWorld(config.getString("jail.location.world"));
        if (world == null) return null;

        double x = config.getDouble("jail.location.x");
        double y = config.getDouble("jail.location.y");
        double z = config.getDouble("jail.location.z");
        float yaw = (float) config.getDouble("jail.location.yaw");
        float pitch = (float) config.getDouble("jail.location.pitch");

        return new Location(world, x, y, z, yaw, pitch);
    }

    private Location getReleaseLocation() {
        if (!config.contains("jail.release")) return Bukkit.getWorlds().get(0).getSpawnLocation();

        World world = Bukkit.getWorld(config.getString("jail.release.world"));
        if (world == null) return Bukkit.getWorlds().get(0).getSpawnLocation();

        double x = config.getDouble("jail.release.x");
        double y = config.getDouble("jail.release.y");
        double z = config.getDouble("jail.release.z");

        return new Location(world, x, y, z);
    }

    /* -------------------------
       Tick task (enforcement)
       ------------------------- */

    private void startJailTask() {
        jailTask = Bukkit.getScheduler().runTaskTimer(plugin, () -> {

            Iterator<Map.Entry<UUID, JailData>> iterator = jailedPlayers.entrySet().iterator();

            while (iterator.hasNext()) {
                Map.Entry<UUID, JailData> entry = iterator.next();
                UUID uuid = entry.getKey();
                JailData data = entry.getValue();

                Player player = Bukkit.getPlayer(uuid);

                // expired
                if (System.currentTimeMillis() > data.jailUntil) {
                    iterator.remove();
                    convictionManager.release(uuid);

                    if (player != null && player.isOnline()) {
                        teleportToRelease(player);
                    }
                    continue;
                }

                // enforce still jailed
                if (player != null && player.isOnline()) {
                    Location jailLoc = getJailLocation();
                    if (jailLoc != null && player.getLocation().distanceSquared(jailLoc) > 25) {
                        player.teleport(jailLoc);
                    }
                }
            }

        }, 20L, 20L);
    }

    public void shutdown() {
        if (jailTask != null) jailTask.cancel();
    }

    /* -------------------------
       Internal data
       ------------------------- */

    private static class JailData {
        String reason;
        long jailUntil;
        Location lastLocation;
    }
}