package com.newttech.ntutils;

import com.newttech.ntutils.command.*;
import com.newttech.ntutils.listener.FreezeListener;
import com.newttech.ntutils.listener.JailListener;
import com.newttech.ntutils.listener.PlayerActivityListener;
import com.newttech.ntutils.manager.*;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.plugin.RegisteredServiceProvider;
import org.bukkit.plugin.java.JavaPlugin;

import net.milkbowl.vault.economy.Economy;

public class NTUtils extends JavaPlugin {

    private static NTUtils instance;

    private static Economy economy;

    private ConfigManager configManager;
    private InnocentManager innocentManager;
    private ConvictionManager convictionManager;
    private JailManager jailManager;
    private FreezeManager freezeManager;
    private SeenManager seenManager;
    private AFKManager afkManager;

    public static NTUtils getInstance() {
        return instance;
    }

    public static Economy getEconomy() {
        return economy;
    }

    @Override
    public void onEnable() {

        instance = this;

        if (!setupEconomy()) {
            getLogger().severe("Vault was not found! Disabling.");
            getServer().getPluginManager().disablePlugin(this);
            return;
        }

        saveDefaultConfig();
        FileConfiguration config = getConfig();
        configManager = new ConfigManager(this);
        innocentManager = new InnocentManager(this);
        convictionManager = new ConvictionManager(config);
        jailManager = new JailManager(
                this,
                config,
                convictionManager
        );
        freezeManager = new FreezeManager(this);
        seenManager = new SeenManager(this);
        long afkThresholdMillis = config.getLong("afk.threshold-seconds", 300) * 1000L;
        afkManager = new AFKManager(this, afkThresholdMillis);

        registerManagers();
        registerCommands();
        registerListeners();

        getLogger().info("NTUtils enabled.");
    }

    @Override
    public void onDisable() {

        jailManager.shutdown();
        freezeManager.shutdown();
        seenManager.shutdown();
        afkManager.shutdown();

    }

    private boolean setupEconomy() {

        if (getServer().getPluginManager().getPlugin("Vault") == null)
            return false;

        RegisteredServiceProvider<Economy> rsp =
                getServer().getServicesManager().getRegistration(Economy.class);

        if (rsp == null)
            return false;

        economy = rsp.getProvider();

        return economy != null;

    }

    private void registerManagers() {

        FileConfiguration config = getConfig();

        convictionManager = new ConvictionManager(config);

        jailManager = new JailManager(
                this,
                config,
                convictionManager
        );

        freezeManager = new FreezeManager(this);

        seenManager = new SeenManager(this);

        long afkThresholdMillis =
                config.getLong("afk.threshold-seconds", 300) * 1000L;

        afkManager = new AFKManager(this, afkThresholdMillis);
    }

    private void registerCommands() {

        getCommand("jail").setExecutor(new JailCommand(jailManager, convictionManager));
        getCommand("unjail").setExecutor(new UnjailCommand(jailManager));

        getCommand("freeze").setExecutor(new FreezeCommand(freezeManager));
        getCommand("unfreeze").setExecutor(new UnfreezeCommand(freezeManager));

        getCommand("seen").setExecutor(new SeenCommand(seenManager));
        getCommand("afk").setExecutor(new AFKCommand(afkManager));
    }

    private void registerListeners() {

        getServer().getPluginManager().registerEvents(
                new PlayerActivityListener(afkManager),
                this
        );

        getServer().getPluginManager().registerEvents(
                new JailListener(jailManager),
                this
        );

        getServer().getPluginManager().registerEvents(
                new FreezeListener(freezeManager),
                this
        );
    }

    public ConfigManager getConfigManager() {
        return configManager;
    }

    public InnocentManager getInnocentManager() {
        return innocentManager;
    }

    public ConvictionManager getConvictionManager() {
        return convictionManager;
    }

    public JailManager getJailManager() {
        return jailManager;
    }

    public FreezeManager getFreezeManager() {
        return freezeManager;
    }

    public SeenManager getSeenManager() {
        return seenManager;
    }

    public AFKManager getAfkManager() {
        return afkManager;
    }

}