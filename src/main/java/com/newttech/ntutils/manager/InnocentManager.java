package com.newttech.ntutils.manager;

import com.newttech.ntutils.NTUtils;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.EntityType;

import java.util.Collections;
import java.util.EnumMap;
import java.util.Map;
import java.util.Set;

public class InnocentManager {

    private final NTUtils plugin;
    private final EnumMap<EntityType, Double> innocentMap = new EnumMap<>(EntityType.class);

    public InnocentManager(NTUtils plugin) {
        this.plugin = plugin;
        reload();
    }

    public void reload() {
        innocentMap.clear();
        FileConfiguration config = plugin.getConfigManager().getInnocents();
        ConfigurationSection section = config.getConfigurationSection("innocents");
        if (section == null) return;

        for (String key : section.getKeys(false)) {
            try {
                EntityType type = EntityType.valueOf(key.toUpperCase());
                innocentMap.put(type, section.getDouble(key + ".cost", 0D));
            } catch (IllegalArgumentException ex) {
                plugin.getLogger().warning("Unknown entity type in innocents.yml: " + key);
            }
        }
    }

    public void save() {
        FileConfiguration config = plugin.getConfigManager().getInnocents();
        config.set("innocents", null);
        for (Map.Entry<EntityType, Double> e : innocentMap.entrySet()) {
            config.set("innocents." + e.getKey().name() + ".cost", e.getValue());
        }
        plugin.getConfigManager().saveInnocents();
    }

    public boolean isInnocent(EntityType type) {
        return innocentMap.containsKey(type);
    }

    public double getFine(EntityType type) {
        return innocentMap.getOrDefault(type, 0D);
    }

    public void setFine(EntityType type, double fine) {
        innocentMap.put(type, fine);
    }

    public void remove(EntityType type) {
        innocentMap.remove(type);
    }

    public Set<EntityType> getInnocents() {
        return Collections.unmodifiableSet(innocentMap.keySet());
    }

    public Map<EntityType, Double> getFineMap() {
        return Collections.unmodifiableMap(innocentMap);
    }
}
