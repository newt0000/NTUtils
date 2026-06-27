package com.newttech.ntutils.command;

import com.newttech.ntutils.manager.ConvictionManager;
import com.newttech.ntutils.manager.JailManager;
import org.bukkit.Bukkit;
import org.bukkit.command.*;
import org.bukkit.entity.Player;

public class JailCommand implements CommandExecutor {

    private final JailManager jailManager;
    private final ConvictionManager convictionManager;

    public JailCommand(JailManager jailManager, ConvictionManager convictionManager) {
        this.jailManager = jailManager;
        this.convictionManager = convictionManager;
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {

        if (args.length < 3) {
            sender.sendMessage("Usage: /jail <player> <timeSeconds> <reason>");
            return true;
        }

        Player target = Bukkit.getPlayer(args[0]);
        if (target == null) {
            sender.sendMessage("Player not found.");
            return true;
        }

        long time = Long.parseLong(args[1]) * 1000L;

        String reason = String.join(" ", java.util.Arrays.copyOfRange(args, 2, args.length));

        jailManager.jail(target, reason, time);

        sender.sendMessage("Jailed " + target.getName());
        return true;
    }
}