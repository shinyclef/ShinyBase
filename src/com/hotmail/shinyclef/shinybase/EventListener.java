package com.hotmail.shinyclef.shinybase;

import com.hotmail.shinyclef.shinybridge.MCServer;
import com.hotmail.shinyclef.shinybridge.cmdadaptations.PreProcessParser;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerCommandPreprocessEvent;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.server.ServerCommandEvent;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.HashSet;
import java.util.Set;

/**
 * Author: Shinyclef
 * Date: 21/06/12
 * Time: 1:56 AM
 */

public class EventListener implements Listener
{
    private ShinyBase plugin;
    private Set<String> commandList;

    public EventListener(ShinyBase plugin)
    {
        plugin.getServer().getPluginManager().registerEvents(this, plugin);
        this.plugin = plugin;
        populateCommandList();
    }

    /* The list of commands the commandPreProcess listener should not ignore. */
    private void populateCommandList()
    {
        commandList = new HashSet<String>();
        commandList.add("/pex");
    }

    @EventHandler
    public void eventPlayerLogin(PlayerJoinEvent ev)
    {
        plugin.getPlayerListManager().ListCheckAdd(ev.getPlayer(), ev.getPlayer().getName());
    }

    @EventHandler
    public void eventServerCommand(ServerCommandEvent e)
    {
        if (e.getCommand().toLowerCase().startsWith("pex "))
        {
            Bukkit.getScheduler().runTaskLater(plugin, new BukkitRunnable()
            {
                @Override
                public void run()
                {
                    PermissionsChangeManager.firePermissionsChangeEvent();
                }
            }, 0);
        }
    }

    @EventHandler
    public void eventPlayerCommandPreprocess(PlayerCommandPreprocessEvent e)
    {
        final String message = e.getMessage().trim();
        String lcMessage = message.toLowerCase();

        //setup args string and command
        String command;
        String argsString;
        if (message.contains(" "))
        {
            command = message.substring(0, message.indexOf(" "));
            argsString = message.substring(message.indexOf(" ") + 1);
        }
        else
        {
            command = message;
            argsString = "";
        }

        //filter command and aliases
        boolean relevantCommandFound = false;
        for (String com : commandList)
        {
            if (command.equals(com))
            {
                relevantCommandFound = true;
            }
        }

        if (!relevantCommandFound)
        {
            return;
        }

        //setup sender command and sender
        CommandSender sender = e.getPlayer();

        //convert the args string to args array
        String [] args;
        if (!argsString.equals(""))
        {
            args = argsString.split(" ");
        }
        else
        {
            args = new String[0];
        }

        //send all our data to be parsed
        preProcessParser(e, sender, command, args);
    }

    private void preProcessParser(PlayerCommandPreprocessEvent e, CommandSender sender,
                                         String command, String[] args)
    {
        switch (command.substring(1).toLowerCase())
        {
            case "pex":
                if (args.length == 0)
                {
                    return;
                }

                if (!sender.hasPermission("rolyd.mod"))
                {
                    return;
                }

                Bukkit.getScheduler().runTaskLater(plugin, new BukkitRunnable()
                {
                    @Override
                    public void run()
                    {
                        PermissionsChangeManager.firePermissionsChangeEvent();
                    }
                }, 0);
                break;
        }
    }
}
