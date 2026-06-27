package com.newttech.ntutils.manager;

import com.newttech.ntutils.NTUtils;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;

import java.io.File;
import java.io.IOException;

public class ConfigManager {

    private final NTUtils plugin;

    private File innocentFile;
    private File convictionFile;
    private File jailFile;
    private File seenFile;

    private FileConfiguration innocents;
    private FileConfiguration convictions;
    private FileConfiguration jailed;
    private FileConfiguration seen;

    public ConfigManager(NTUtils plugin) {
        this.plugin = plugin;

        createFiles();
        loadFiles();
        saveDefaults();
    }

    private void createFiles() {

        if (!plugin.getDataFolder().exists())
            plugin.getDataFolder().mkdirs();

        innocentFile = new File(plugin.getDataFolder(), "innocents.yml");
        convictionFile = new File(plugin.getDataFolder(), "convictions.yml");
        jailFile = new File(plugin.getDataFolder(), "jailed.yml");
        seenFile = new File(plugin.getDataFolder(), "seen.yml");

        try {

            if (!innocentFile.exists())
                innocentFile.createNewFile();

            if (!convictionFile.exists())
                convictionFile.createNewFile();

            if (!jailFile.exists())
                jailFile.createNewFile();

            if (!seenFile.exists())
                seenFile.createNewFile();

        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    private void loadFiles() {

        innocents = YamlConfiguration.loadConfiguration(innocentFile);
        convictions = YamlConfiguration.loadConfiguration(convictionFile);
        jailed = YamlConfiguration.loadConfiguration(jailFile);
        seen = YamlConfiguration.loadConfiguration(seenFile);

    }

    private void saveDefaults() {

        if (!innocents.contains("innocents")) {

            innocents.set("innocents.COW.cost", 5.0);
            innocents.set("innocents.SHEEP.cost", 5.0);
            innocents.set("innocents.PIG.cost", 5.0);
            innocents.set("innocents.CHICKEN.cost", 3.0);
            innocents.set("innocents.VILLAGER.cost", 500.0);

            saveInnocents();

        }

        FileConfiguration config = plugin.getConfig();

        if (!config.contains("jail.world")) {

            config.set("jail.world", "world");
            config.set("jail.x", 0.5);
            config.set("jail.y", 80.0);
            config.set("jail.z", 0.5);
            config.set("jail.yaw", 0F);
            config.set("jail.pitch", 0F);
            config.set("jail.radius", 3);

            config.set("afk.world", "world");
            config.set("afk.x", 0.5);
            config.set("afk.y", 80.0);
            config.set("afk.z", 0.5);
            config.set("afk.yaw", 0F);
            config.set("afk.pitch", 0F);

            config.set("broadcast-command.jailed",
                    "/dbcast primary %player% was jailed for %crime% and fined %fine%");

            config.set("broadcast-command.released",
                    "/dbcast primary %player% was released from jail");

            plugin.saveConfig();

        }

    }

    public void reload() {

        plugin.reloadConfig();

        loadFiles();

    }

    public void saveAll() {

        saveInnocents();
        saveConvictions();
        saveJailed();
        saveSeen();

        plugin.saveConfig();

    }

    public void saveInnocents() {

        try {
            innocents.save(innocentFile);
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    public void saveConvictions() {

        try {
            convictions.save(convictionFile);
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    public void saveJailed() {

        try {
            jailed.save(jailFile);
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    public void saveSeen() {

        try {
            seen.save(seenFile);
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    public FileConfiguration getInnocents() {
        return innocents;
    }

    public FileConfiguration getConvictions() {
        return convictions;
    }

    public FileConfiguration getJailed() {
        return jailed;
    }

    public FileConfiguration getSeen() {
        return seen;
    }

    public Location getJailLocation() {

        FileConfiguration c = plugin.getConfig();

        World world = plugin.getServer().getWorld(c.getString("jail.world"));

        if (world == null)
            return null;

        return new Location(
                world,
                c.getDouble("jail.x"),
                c.getDouble("jail.y"),
                c.getDouble("jail.z"),
                (float) c.getDouble("jail.yaw"),
                (float) c.getDouble("jail.pitch")
        );

    }

    public Location getAFKLocation() {

        FileConfiguration c = plugin.getConfig();

        World world = plugin.getServer().getWorld(c.getString("afk.world"));

        if (world == null)
            return null;

        return new Location(
                world,
                c.getDouble("afk.x"),
                c.getDouble("afk.y"),
                c.getDouble("afk.z"),
                (float) c.getDouble("afk.yaw"),
                (float) c.getDouble("afk.pitch")
        );

    }

    public int getJailRadius() {
        return plugin.getConfig().getInt("jail.radius", 3);
    }

    public String getJailedBroadcast() {
        return plugin.getConfig().getString("broadcast-command.jailed");
    }

    public String getReleasedBroadcast() {
        return plugin.getConfig().getString("broadcast-command.released");
    }

}