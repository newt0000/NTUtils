package com.newttech.ntutils.manager;

import org.bukkit.configuration.file.FileConfiguration;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

public class ConvictionManager {

    private final Map<UUID, PlayerRecord> records = new ConcurrentHashMap<>();
    private final Set<UUID> innocents = new HashSet<>();

    private final FileConfiguration config;

    public ConvictionManager(FileConfiguration config) {
        this.config = config;
        loadInnocents();
    }

    /* -------------------------
       Innocent system (config)
       ------------------------- */

    private void loadInnocents() {
        innocents.clear();

        if (config == null || !config.isConfigurationSection("innocents")) return;

        List<String> list = config.getStringList("innocents.list");
        for (String raw : list) {
            try {
                innocents.add(UUID.fromString(raw));
            } catch (IllegalArgumentException ignored) {}
        }
    }

    public boolean isInnocent(UUID uuid) {
        return innocents.contains(uuid);
    }

    public void addInnocent(UUID uuid) {
        innocents.add(uuid);
        saveInnocents();
    }

    public void removeInnocent(UUID uuid) {
        innocents.remove(uuid);
        saveInnocents();
    }

    private void saveInnocents() {
        config.set("innocents.list", innocents.stream().map(UUID::toString).toList());
    }

    /* -------------------------
       Conviction system
       ------------------------- */

    public void convict(UUID uuid, String reason, long durationMillis) {
        PlayerRecord record = records.getOrDefault(uuid, new PlayerRecord());
        record.jailed = true;
        record.reason = reason;
        record.jailUntil = System.currentTimeMillis() + durationMillis;
        record.totalConvictions++;

        records.put(uuid, record);
    }

    public void release(UUID uuid) {
        PlayerRecord record = records.get(uuid);
        if (record != null) {
            record.jailed = false;
            record.jailUntil = 0;
        }
    }

    public boolean isJailed(UUID uuid) {
        PlayerRecord record = records.get(uuid);
        if (record == null) return false;

        if (record.jailed && System.currentTimeMillis() > record.jailUntil) {
            record.jailed = false;
        }

        return record.jailed;
    }

    public long getRemainingJailTime(UUID uuid) {
        PlayerRecord record = records.get(uuid);
        if (record == null || !record.jailed) return 0;

        return Math.max(0, record.jailUntil - System.currentTimeMillis());
    }

    public String getReason(UUID uuid) {
        PlayerRecord record = records.get(uuid);
        return record != null ? record.reason : null;
    }

    public int getConvictionCount(UUID uuid) {
        PlayerRecord record = records.get(uuid);
        return record != null ? record.totalConvictions : 0;
    }

    /* -------------------------
       Internal record
       ------------------------- */

    private static class PlayerRecord {
        boolean jailed = false;
        String reason = "";
        long jailUntil = 0;
        int totalConvictions = 0;
    }
}