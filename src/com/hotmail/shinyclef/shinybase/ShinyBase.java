package com.hotmail.shinyclef.shinybase;

import net.milkbowl.vault.economy.Economy;
import org.bukkit.plugin.RegisteredServiceProvider;
import org.bukkit.plugin.java.JavaPlugin;


/**
 * Author: ShinyClef
 * Date: 21/06/12
 * Time: 1:28 AM
 */

public class ShinyBase extends JavaPlugin
{
    private PlayerListManager playerListManager;
    private ShinyBaseAPI shinyBaseAPI;
    private Economy economy = null;

    private CmdExecutor commandExecutor;

    @Override
    public void onEnable()
    {
        //if no economy is found, disable this plugin with a message
        if (!setupEconomy())
        {
            this.getLogger().info("SEVERE!!! DISABLING PLUGIN DUE TO NO VAULT ECONOMY FOUND!");
            getServer().getPluginManager().disablePlugin(this);
            return;
        }

        playerListManager = new PlayerListManager(this);
        shinyBaseAPI = new ShinyBaseAPI(this);
        NewPlayerInteraction.setPlugin(this);
        NewPlayerInteraction.messageSetup();
        NewPlayerInteraction.setupResources();
        new EventListener(this);

        commandExecutor = new CmdExecutor(this, playerListManager, economy);
        getCommand("shinybase").setExecutor(commandExecutor);
        getCommand("rolyd").setExecutor(commandExecutor);

        CommandManager.initialize(this);
    }

    @Override
    public void onDisable()
    {

    }

    public PlayerListManager getPlayerlistManager()
    {
        return playerListManager;
    }

    public ShinyBaseAPI getShinyBaseAPI()
    {
        return shinyBaseAPI;
    }

    private boolean setupEconomy()
    {
        RegisteredServiceProvider<Economy> economyProvider = getServer().getServicesManager().getRegistration(net.milkbowl.vault.economy.Economy.class);
        if (economyProvider != null)
        {
            economy = economyProvider.getProvider();
        }

        return (economy != null);
    }

}
