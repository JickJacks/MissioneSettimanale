package me.jickjacks.missionesettimanale;

import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;

public class ComandoReset implements CommandExecutor {

    private final MissioneSettimanale plugin;
    private FileConfiguration config;

    public ComandoReset(MissioneSettimanale plugin) {
        this.plugin = plugin;
        this.config = plugin.getConfig();
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (command.getName().equalsIgnoreCase("missionesettimanale_reset")) {
            if (sender instanceof Player) {
                Player player = (Player) sender;
                if (!player.hasPermission("missionesettimanale.reset")) {
                    player.sendMessage(ChatColor.RED + "Non hai il permesso per utilizzare questo comando.");
                    return true;
                }
            }

            resetConfig();
            sender.sendMessage(ChatColor.AQUA + "[Missione Settimanale] " + ChatColor.GREEN + "Coordinate Missioni Settimanali Resettate.");
            return true;
        }
        return false;
    }

    public void resetConfig() {
        // Rimuove tutte le chiavi dalla configurazione
        for (String key : config.getKeys(false)) {
            config.set(key, null);
        }
        plugin.saveConfig();
    }
}
