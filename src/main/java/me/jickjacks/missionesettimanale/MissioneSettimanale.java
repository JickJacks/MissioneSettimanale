package me.jickjacks.missionesettimanale;

import org.bukkit.plugin.java.JavaPlugin;

public class MissioneSettimanale extends JavaPlugin {

    @Override
    public void onEnable() {

        ComandoSettimanale comandoSettimanale = new ComandoSettimanale(this);
        getCommand("settimanale").setExecutor(comandoSettimanale);
        getCommand("settimanale_cambia").setExecutor(comandoSettimanale);

        GUIMissioni guiMissioni = new GUIMissioni(this);
        getCommand("missioni_gui").setExecutor(guiMissioni);
        getServer().getPluginManager().registerEvents(guiMissioni, this);
    }

    @Override
    public void onDisable() {

    }
}
