package me.jickjacks.missionesettimanale;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.ArrayList;
import java.util.List;

public class GUIMissioni implements CommandExecutor, Listener {

    private final JavaPlugin plugin;

    public GUIMissioni(JavaPlugin plugin) {
        this.plugin = plugin;
        plugin.getCommand("missioni_gui").setExecutor(this);
        Bukkit.getPluginManager().registerEvents(this, plugin);
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (command.getName().equalsIgnoreCase("missioni_gui") && sender instanceof Player) {
            Player player = (Player) sender;
            openMissioniGUI(player);
            return true;
        }
        return false;
    }

    private void openMissioniGUI(Player player) {
        Inventory gui = Bukkit.createInventory(null, 54, "Missioni Settimanali");

        ConfigurationSection playersSection = plugin.getConfig().getConfigurationSection("");
        if (playersSection != null) {
            for (String playerName : playersSection.getKeys(false)) {
                ConfigurationSection coordinatesSection = playersSection.getConfigurationSection(playerName + ".Coordinate");
                if (coordinatesSection != null) {
                    double x = coordinatesSection.getDouble("x");
                    double y = coordinatesSection.getDouble("y");
                    double z = coordinatesSection.getDouble("z");

                    ItemStack playerItem = new ItemStack(Material.PLAYER_HEAD);
                    ItemMeta meta = playerItem.getItemMeta();
                    meta.setDisplayName(playerName);
                    List<String> lore = new ArrayList<>();
                    lore.add("X: " + x);
                    lore.add("Y: " + y);
                    lore.add("Z: " + z);
                    playerItem.setItemMeta(meta);

                    gui.addItem(playerItem);
                }
            }
        }

        player.openInventory(gui);
    }

    @EventHandler
    public void onInventoryClick(InventoryClickEvent event) {
        if (!event.getView().getTitle().equals("Missioni Settimanali")) {
            return;
        }

        event.setCancelled(true);

        ItemStack clickedItem = event.getCurrentItem();
        if (clickedItem == null || clickedItem.getType() == Material.AIR || !(event.getWhoClicked() instanceof Player)) {
            return;
        }

        Player player = (Player) event.getWhoClicked();
        Location location = getLocationFromConfig(clickedItem.getItemMeta().getDisplayName());

        if (location != null) {
            player.teleport(location);
            player.closeInventory();
        }
    }

    private Location getLocationFromConfig(String playerName) {
        ConfigurationSection coordinatesSection = plugin.getConfig().getConfigurationSection(playerName + ".Coordinate");
        if (coordinatesSection != null) {
            double x = coordinatesSection.getDouble("x");
            double y = coordinatesSection.getDouble("y");
            double z = coordinatesSection.getDouble("z");

            String worldName = coordinatesSection.getString("world");

            org.bukkit.World world = Bukkit.getWorld(worldName);
            if (world != null) {
                return new Location(world, x, y, z);
            }
        }
        return null;
    }
}
