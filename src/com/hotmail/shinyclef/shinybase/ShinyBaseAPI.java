package com.hotmail.shinyclef.shinybase;

import com.hotmail.shinyclef.shinybridge.ShinyBridgeAPI;
import org.bukkit.ChatColor;
import org.bukkit.Server;
import org.bukkit.command.CommandExecutor;
import org.bukkit.plugin.Plugin;
import org.bukkit.scheduler.BukkitRunnable;

/**
 * Author: ShinyClef
 * Date: 21/06/12
 * Time: 3:20 AM
 */

public class ShinyBaseAPI
{
    private ShinyBase p;

    public ShinyBaseAPI(ShinyBase plugin)
    {
        this.p = plugin;
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

    public boolean isExistingPlayer(String playerName)
    {
        return p.getPlayerListManager().getPlayers().contains(playerName);
    }

    public void takeOverBukkitCommand(Plugin plugin, String cmdName, CommandExecutor executor)
    {
        //make sure this happens after server is loaded
        plugin.getServer().getScheduler().runTaskLater(plugin, new CommandModification(plugin, cmdName, executor), 0);
    }

    public String makeSentence(String[] args, int startingArg)
    {
        int i = startingArg;
        String sentence = "";

        //put all the args starting from the 1st into string 'sentence'
        do
        {
            sentence = sentence + args[i] + " ";
            i++;
        }
        while (i < args.length);

        return sentence;
    }
}
