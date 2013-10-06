package com.hotmail.shinyclef.shinybase;

import com.hotmail.shinyclef.shinybridge.ShinyBridge;
import com.hotmail.shinyclef.shinybridge.ShinyBridgeAPI;
import net.milkbowl.vault.economy.Economy;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.plugin.Plugin;
import org.bukkit.plugin.RegisteredServiceProvider;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.scheduler.BukkitRunnable;


/**
 * Author: ShinyClef
 * Date: 21/06/12
 * Time: 1:28 AM
 */

public class ShinyBase extends JavaPlugin
{
    private static ShinyBase plugin;
    private static ShinyBaseAPI shinyBaseAPI;
    private Economy economy = null;
    private PlayerListManager playerListManager;

    @Override
    public void onEnable()
    {
        //default config
        saveDefaultConfig();

        //if no economy is found, disable this plugin with a message
        if (!setupEconomy())
        {
            this.getLogger().info("SEVERE!!! DISABLING PLUGIN DUE TO NO VAULT ECONOMY FOUND!");
            getServer().getPluginManager().disablePlugin(this);
            return;
        }

        //prepare components
        playerListManager = new PlayerListManager(this);
        shinyBaseAPI = new ShinyBaseAPI(this);
        CommandManager.initialize(this);
        CmdExecutor commandExecutor = new CmdExecutor(this, playerListManager, economy);
        getCommand("shinybase").setExecutor(commandExecutor);
        getCommand("rolyd").setExecutor(commandExecutor);

        //setup player interaction
        NewPlayerInteraction.setPlugin(this);
        NewPlayerInteraction.messageSetup();
        NewPlayerInteraction.setupResources();
        new EventListener(this);

        plugin = this;
    }

    @Override
    public void onDisable()
    {

    }

    private boolean setupEconomy()
    {
        RegisteredServiceProvider<Economy> economyProvider =
                getServer().getServicesManager().getRegistration(net.milkbowl.vault.economy.Economy.class);
        if (economyProvider != null)
        {
            economy = economyProvider.getProvider();
        }

        return (economy != null);
    }


    /* Getters */

    public static ShinyBase getPlugin()
    {
        return plugin;
    }

    public PlayerListManager getPlayerListManager()
    {
        return playerListManager;
    }

    public ShinyBaseAPI getShinyBaseAPI()
    {
        return shinyBaseAPI;
    }
}
