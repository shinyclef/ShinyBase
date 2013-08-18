package com.hotmail.shinyclef.shinybase;

import com.hotmail.shinyclef.shinybridge.ShinyBridge;
import com.hotmail.shinyclef.shinybridge.ShinyBridgeAPI;
import net.milkbowl.vault.economy.Economy;
import org.bukkit.Bukkit;
import org.bukkit.plugin.Plugin;
import org.bukkit.plugin.RegisteredServiceProvider;
import org.bukkit.plugin.java.JavaPlugin;


/**
 * Author: ShinyClef
 * Date: 21/06/12
 * Time: 1:28 AM
 */

public class ShinyBase extends JavaPlugin
{
    private static ShinyBase plugin;
    private PlayerListManager playerListManager;
    private ShinyBaseAPI shinyBaseAPI;
    private ShinyBridgeAPI shinyBridgeAPI = null;
    private Economy economy = null;

    private CmdExecutor commandExecutor;

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

        Plugin bridge = Bukkit.getPluginManager().getPlugin("ShinyBridge");
        if (bridge != null)
        {
            ShinyBridge shinyBridge = (ShinyBridge) bridge;
            shinyBridgeAPI = shinyBridge.getShinyBridgeAPI();
        }

        playerListManager = new PlayerListManager(this);
        ModChatHandler modChatHandler = new ModChatHandler(this);
        shinyBaseAPI = new ShinyBaseAPI(this, shinyBridgeAPI, modChatHandler);  //shinyBaseAPI constructs
        NewPlayerInteraction.setPlugin(this);
        NewPlayerInteraction.messageSetup();
        NewPlayerInteraction.setupResources();
        new EventListener(this);

        commandExecutor = new CmdExecutor(this, playerListManager, economy);
        getCommand("shinybase").setExecutor(commandExecutor);
        getCommand("rolyd").setExecutor(commandExecutor);

        CommandManager.initialize(this);

        plugin = this;
    }

    @Override
    public void onDisable()
    {

    }


    /* Getters */

    public static ShinyBase getPlugin()
    {
        return plugin;
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
