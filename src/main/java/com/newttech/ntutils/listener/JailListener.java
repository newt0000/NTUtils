package com.newttech.ntutils.listener;

import com.newttech.ntutils.manager.JailManager;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerMoveEvent;

public class JailListener implements Listener {

    private final JailManager jailManager;

    public JailListener(JailManager jailManager) {
        this.jailManager = jailManager;
    }

    @EventHandler
    public void onMove(PlayerMoveEvent event) {

        if (!jailManager.isJailed(event.getPlayer())) return;

        event.setCancelled(true);
    }
}