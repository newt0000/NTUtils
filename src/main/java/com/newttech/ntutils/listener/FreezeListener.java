package com.newttech.ntutils.listener;

import com.newttech.ntutils.manager.FreezeManager;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerMoveEvent;

public class FreezeListener implements Listener {

    private final FreezeManager freezeManager;

    public FreezeListener(FreezeManager freezeManager) {
        this.freezeManager = freezeManager;
    }

    @EventHandler
    public void onMove(PlayerMoveEvent event) {

        if (!freezeManager.isFrozen(event.getPlayer())) return;

        event.setCancelled(true);
    }
}