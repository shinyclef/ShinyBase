package com.hotmail.shinyclef.shinybase;

import com.hotmail.shinyclef.shinybridge.ShinyBridgeAPI;
import org.bukkit.Server;
import org.bukkit.command.CommandExecutor;
import org.bukkit.entity.Player;
import org.bukkit.event.player.AsyncPlayerChatEvent;
import org.bukkit.plugin.Plugin;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.List;

/**
 * Author: ShinyClef
 * Date: 21/06/12
 * Time: 3:20 AM
 */

public class ShinyBaseAPI
{
    private ShinyBase p;
    private Server s;
    private ShinyBridgeAPI shinyBridgeAPI;
    private boolean haveBridge = false;
    private ModChatHandler modChatHandler;

    public ShinyBaseAPI(ShinyBase plugin, ShinyBridgeAPI shinyBridgeAPI, ModChatHandler modChatHandler)
    {
        this.p = plugin;
        this.shinyBridgeAPI = shinyBridgeAPI;
        if (shinyBridgeAPI != null)
        {
            haveBridge = true;
        }
        this.modChatHandler = modChatHandler;
    }

    /* Schedules the removal of a command and it's re-addition via a particular plugin's PluginManager.
    * The scheduling is important because scheduled tasks occur after the server is loaded, when
    * all plugins have finished registering their commands. */
    private class CommandModification extends BukkitRunnable
    {
        Plugin plugin;
        String cmdName;
        CommandExecutor executor;

        private CommandModification(Plugin plugin, String cmdName, CommandExecutor executor)
        {
            this.plugin = plugin;
            s = plugin.getServer();
            this.cmdName = cmdName;
            this.executor = executor;
        }

        @Override
        public void run()
        {
            CommandManager.modifyBukkitCommands(plugin, cmdName, false, null);
            CommandManager.modifyBukkitCommands(plugin, cmdName, true, executor);
        }
    }

    // -------------- Public Methods -------------- //

    public boolean isExistingPlayer(String playername)
    {
        return p.getPlayerlistManager().getPlayers().contains(playername);
    }

    public void takeOverBukkitCommand(Plugin plugin, String cmdName, CommandExecutor executor)
    {
        //make sure this happens after server is loaded
        plugin.getServer().getScheduler().runTaskLater(plugin, new CommandModification(plugin, cmdName, executor), 0);
    }

    public void broadcastPermissionMessage(String message, String permission)
    {
        //send to bridge clients if we have bridge
        if (haveBridge)
        {
            shinyBridgeAPI.broadcastMessage(message, permission, true);
        }
        else
        {
            //standard broadcast
            s.broadcast(message, permission);
        }
    }

    public void sendMessage(String playerName, String message)
    {
        //standard send
        if (s.getOfflinePlayer(playerName).isOnline())
        {
            s.getPlayer(playerName).sendMessage(message);
        }

        //additionally if they're a bridge client
        if (haveBridge)
        {
            shinyBridgeAPI.sendToClient(playerName, message);
        }
    }

    public boolean isOnlineAnywhere(String playerName)
    {
        if (haveBridge)
        {
            return shinyBridgeAPI.isOnlineServerPlusClients(playerName);
        }
        else
        {
            return s.getOfflinePlayer(playerName).isOnline();
        }
    }



    /* MB Toggle Related */

    public ModChatHandler getModChatHandler()
    {
        return modChatHandler;
    }

    /* Returns true if the event should be cancelled. But do not use e.setCancelled(handleChatEvent) as we don't
    * necessarily want to set it to false (other plugins might have set it to true previously). */
    public void handleChatEvent(AsyncPlayerChatEvent e)
    {
        if (e.isCancelled())
        {
            return;
        }

        Player player = e.getPlayer();

        //if player's a mod and they do not have modChat toggled off
        if (modChatHandler.defaultIsModChat(e.getPlayer()))
        {
            if (e.getMessage().startsWith(ModChatHandler.MOD_BYPASS_CHAR))
            {
                e.setMessage(e.getMessage().substring(1));
            }
            else
            {
                modChatHandler.newModChatEvent(e);
                e.setCancelled(true);
            }
        }
    }
}
