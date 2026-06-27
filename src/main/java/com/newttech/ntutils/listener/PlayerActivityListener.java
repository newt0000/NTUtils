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

        if (event.getTo() == null) return;

        afkManager.handleMove(
                event.getPlayer(),
                event.getFrom(),
                event.getTo()
        );
    }

    @EventHandler
    public void onChat(AsyncPlayerChatEvent event) {
        afkManager.handleChat(event.getPlayer());
    }

    @EventHandler
    public void onInteract(PlayerInteractEvent event) {
        afkManager.handleChat(event.getPlayer());
    }
}