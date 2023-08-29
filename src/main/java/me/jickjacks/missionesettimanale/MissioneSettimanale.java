package me.jickjacks.missionesettimanale;

import org.bukkit.plugin.java.JavaPlugin;

public class MissioneSettimanale extends JavaPlugin {

    @Override
    public void onEnable() {

        ComandoSettimanale comandoSettimanale = new ComandoSettimanale(this);
        getCommand("settimanale").setExecutor(comandoSettimanale);
        getCommand("settimanale_cambia").setExecutor(comandoSettimanale);
    }

    @Override
    public void onDisable() {

    }
}
