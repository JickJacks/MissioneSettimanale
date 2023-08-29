package me.jickjacks.missionesettimanale;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.plugin.Plugin;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class FileMissioni {

    private static final String FILE_NAME = "CoordinateMissioni.yml";
    private final File file;
    private final FileConfiguration config;

    public FileMissioni(MissioneSettimanale plugin) {
        this.file = new File(plugin.getDataFolder(), FILE_NAME);
        this.config = YamlConfiguration.loadConfiguration(file);
    }

    public void saveCoordinates(String playerName, Location location) {
        String path = "coordinates." + playerName;
        ConfigurationSection section = config.getConfigurationSection(path);
        if (section == null) {
            section = config.createSection(path);
        }

        section.set("world", location.getWorld().getName());
        section.set("x", location.getX());
        section.set("y", location.getY());
        section.set("z", location.getZ());

        saveConfig();
    }

    public Location getCoordinates(String playerName) {
        String path = "coordinates." + playerName;
        if (config.contains(path)) {
            ConfigurationSection section = config.getConfigurationSection(path);
            String worldName = section.getString("world");
            double x = section.getDouble("x");
            double y = section.getDouble("y");
            double z = section.getDouble("z");
            return new Location(Bukkit.getWorld(worldName), x, y, z);
        }
        return null;
    }

    public List<String> getNicknames() {
        ConfigurationSection coordinatesSection = config.getConfigurationSection("coordinates");
        if (coordinatesSection != null) {
            return new ArrayList<>(coordinatesSection.getKeys(false));
        }
        return new ArrayList<>();
    }

    public void saveConfig() {
        try {
            config.save(file);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
