package com.newttech.ntutils;

import com.newttech.ntutils.manager.*;
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

        configManager = new ConfigManager(this);
        innocentManager = new InnocentManager(this);
        convictionManager = new ConvictionManager(this);
        jailManager = new JailManager(this);
        freezeManager = new FreezeManager();
        seenManager = new SeenManager(this);
        afkManager = new AFKManager(this);

        registerManagers();

        registerCommands();

        registerListeners();

        getLogger().info("NTUtils enabled.");
    }

    @Override
    public void onDisable() {

        convictionManager.save();

        jailManager.save();

        seenManager.save();

        afkManager.save();

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

        // Future hook if needed.

    }

    private void registerCommands() {

        // Filled in once commands are created.

    }

    private void registerListeners() {

        // Filled in after listeners exist.

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