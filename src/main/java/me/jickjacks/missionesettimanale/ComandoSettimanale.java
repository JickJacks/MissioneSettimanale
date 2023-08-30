package me.jickjacks.missionesettimanale;

import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.Location;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;

import java.io.File;
import java.io.IOException;

public class ComandoSettimanale implements CommandExecutor {

    private final MissioneSettimanale plugin;
    private FileConfiguration config;

    public ComandoSettimanale(MissioneSettimanale plugin) {
        this.plugin = plugin;
        this.config = plugin.getConfig();
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (command.getName().equalsIgnoreCase("settimanale")) {
            if (sender instanceof Player) {
                Player player = (Player) sender;
                String playerName = player.getName();

                if (hasCoordinates(playerName)) {
                    // Chiedi la conferma per la sostituzione delle coordinate
                    player.sendMessage(ChatColor.AQUA + "" + ChatColor.BOLD + "[Missione Settimanale] " + ChatColor.RED + "Hai già settato le coordinate, vuoi sostituirle? Digita " + ChatColor.GREEN + ChatColor.ITALIC + "/settimanale_cambia" + ChatColor.RESET + ChatColor.RED + " per confermare.");
                    setWaitingForConfirmation(playerName, true);
                } else {
                    // Coordinate non presenti, procedi con il salvataggio
                    Location playerLocation = player.getLocation();
                    double x = playerLocation.getX();
                    double y = playerLocation.getY();
                    double z = playerLocation.getZ();
                    String worldName = playerLocation.getWorld().getName();

                    saveCoordinates(playerName, x, y, z, worldName);

                    player.sendMessage(ChatColor.AQUA + "" + ChatColor.BOLD + "[Missione Settimanale] " + ChatColor.GREEN + "Coordinate Missione Settimanale Salvate!");
                }

                return true;
            }
        } else if (command.getName().equalsIgnoreCase("settimanale_cambia")) {
            if (sender instanceof Player) {
                Player player = (Player) sender;
                String playerName = player.getName();

                if (isWaitingForConfirmation(playerName)) {
                    // Cambia le coordinate nel file e reimposta lo stato
                    Location playerLocation = player.getLocation();
                    double x = playerLocation.getX();
                    double y = playerLocation.getY();
                    double z = playerLocation.getZ();
                    String worldName = playerLocation.getWorld().getName();

                    saveCoordinates(playerName, x, y, z, worldName);
                    setWaitingForConfirmation(playerName, false);

                    player.sendMessage(ChatColor.AQUA + "" + ChatColor.BOLD + "[Missione Settimanale] " + ChatColor.GREEN + "Coordinate cambiate!");
                } else {
                    player.sendMessage(ChatColor.AQUA + "" + ChatColor.BOLD + "[Missione Settimanale] " + ChatColor.RED + "Non hai una conferma in sospeso per cambiare le coordinate.");
                }

                return true;
            }
        }

        return false;
    }

    private boolean hasCoordinates(String playerName) {
        return config.contains(playerName + ".Coordinate.x");
    }

    private void setWaitingForConfirmation(String playerName, boolean value) {
        config.set(playerName + ".WaitingForConfirmation", value);
        plugin.saveConfig();
    }

    private boolean isWaitingForConfirmation(String playerName) {
        return config.getBoolean(playerName + ".WaitingForConfirmation", false);
    }

    private void saveCoordinates(String playerName, double x, double y, double z, String worldName) {
        config.set(playerName + ".Coordinate.x", x);
        config.set(playerName + ".Coordinate.y", y);
        config.set(playerName + ".Coordinate.z", z);
        config.set(playerName + ".Coordinate.world", worldName);
        plugin.saveConfig();
    }
}
