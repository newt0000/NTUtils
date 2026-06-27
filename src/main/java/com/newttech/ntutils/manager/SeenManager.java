package com.newttech.ntutils.manager;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.scheduler.BukkitTask;
import org.bukkit.scoreboard.*;

import java.util.Map;
import java.util.Set;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;

public class SeenManager {

    private final JavaPlugin plugin;

    // players who are "seen" (glowing enabled)
    private final Set<UUID> seenEnabled = ConcurrentHashMap.newKeySet();

    private final ScoreboardManager scoreboardManager;
    private final Scoreboard board;

    private BukkitTask task;

    public SeenManager(JavaPlugin plugin) {
        this.plugin = plugin;

        this.scoreboardManager = Bukkit.getScoreboardManager();
        this.board = scoreboardManager.getMainScoreboard();

        startTask();
    }

    /* -------------------------
       Toggle system
       ------------------------- */

    public void enableSeen(Player player) {
        seenEnabled.add(player.getUniqueId());
        applyGlow(player);
    }

    public void disableSeen(Player player) {
        seenEnabled.remove(player.getUniqueId());
        removeGlow(player);
    }

    public boolean isSeenEnabled(Player player) {
        return seenEnabled.contains(player.getUniqueId());
    }

    public void toggleSeen(Player player) {
        if (isSeenEnabled(player)) {
            disableSeen(player);
        } else {
            enableSeen(player);
        }
    }

    /* -------------------------
       Glow handling
       ------------------------- */

    private void applyGlow(Player player) {
        player.setGlowing(true);

        Team team = getOrCreateTeam(player.getUniqueId().toString());
        team.addEntry(player.getName());
    }

    private void removeGlow(Player player) {
        player.setGlowing(false);

        Team team = board.getTeam(player.getUniqueId().toString());
        if (team != null) {
            team.removeEntry(player.getName());
        }
    }

    private Team getOrCreateTeam(String name) {
        Team team = board.getTeam(name);
        if (team == null) {
            team = board.registerNewTeam(name);

            team.setColor(ChatColor.AQUA);
            team.setCanSeeFriendlyInvisibles(true);
            team.setOption(Team.Option.NAME_TAG_VISIBILITY, Team.OptionStatus.ALWAYS);
        }
        return team;
    }

    /* -------------------------
       Enforcement loop
       ------------------------- */

    private void startTask() {
        task = Bukkit.getScheduler().runTaskTimer(plugin, () -> {

            for (UUID uuid : seenEnabled) {
                Player player = Bukkit.getPlayer(uuid);

                if (player == null || !player.isOnline()) continue;

                // enforce glow permanently
                if (!player.isGlowing()) {
                    player.setGlowing(true);
                }
            }

        }, 20L, 20L);
    }

    public void shutdown() {
        if (task != null) task.cancel();

        for (UUID uuid : seenEnabled) {
            Player player = Bukkit.getPlayer(uuid);
            if (player != null) {
                player.setGlowing(false);
            }
        }

        seenEnabled.clear();
    }
}