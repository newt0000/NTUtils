package com.newttech.ntutils.listener;

import com.newttech.ntutils.manager.AFKManager;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.*;

public class PlayerActivityListener implements Listener {

    private final AFKManager afkManager;

    public PlayerActivityListener(AFKManager afkManager) {
        this.afkManager = afkManager;
    }

    @EventHandler
    public void onMove(PlayerMoveEvent event) {
        if (event.getFrom().distanceSquared(event.getTo()) > 0) {
            afkManager.markActive(event.getPlayer());
        }
    }

    @EventHandler
    public void onInteract(PlayerInteractEvent event) {
        afkManager.markActive(event.getPlayer());
    }

    @EventHandler
    public void onChat(AsyncPlayerChatEvent event) {
        afkManager.markActive(event.getPlayer());
    }

    @EventHandler
    public void onJoin(PlayerJoinEvent event) {
        afkManager.markJoin(event.getPlayer());
    }

    @EventHandler
    public void onQuit(PlayerQuitEvent event) {
        afkManager.markQuit(event.getPlayer());
    }
}