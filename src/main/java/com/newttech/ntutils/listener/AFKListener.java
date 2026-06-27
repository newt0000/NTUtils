package com.newttech.ntutils.listener;

import com.newttech.ntutils.manager.AFKManager;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.AsyncPlayerChatEvent;
import org.bukkit.event.player.PlayerMoveEvent;

public class AFKListener implements Listener {

    private final AFKManager afkManager;

    public AFKListener(AFKManager afkManager) {
        this.afkManager = afkManager;
    }

    @EventHandler
    public void onMove(PlayerMoveEvent event) {
        if (event.getTo() == null) return;

        afkManager.handleMove(event.getPlayer(), event.getFrom(), event.getTo());
    }

    @EventHandler
    public void onChat(AsyncPlayerChatEvent event) {
        afkManager.handleChat(event.getPlayer());
    }
}