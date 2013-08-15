package com.hotmail.shinyclef.shinybase;

import net.milkbowl.vault.economy.Economy;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;

/**
 * Author: ShinyClef
 * Date: 22/06/12
 * Time: 3:48 AM
 */

public class CmdExecutor implements CommandExecutor
{
    private ShinyBase plugin;
    private PlayerListManager playerListManager;
    Economy economy = null;

    public CmdExecutor (ShinyBase plugin, PlayerListManager playerListManager, Economy economy) //constructor
    {
        this.plugin = plugin;
        this.playerListManager = playerListManager;
        this.economy = economy;
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args)
    {
        if (command.getName().equalsIgnoreCase("shinybase"))
        {
            //make sure there's at least one arg
            if (args.length < 1)
            {
                return false;
            }

            if (args[0].equalsIgnoreCase("help"))
            {
                //send help details to player
                sender.sendMessage(ChatColor.RED + "/shinybase reloadplayerlist" + ChatColor.WHITE + " - Reloads playerlist.yml. Useful if oyu want to make manual changes to playerlist.yml without needing to restart server for those changes to take effect.");
                return true;
            }

            if (args[0].equalsIgnoreCase("reloadplayerlist"))
            {
                playerListManager.reloadConfigPlayerlist();
                sender.sendMessage(ChatColor.RED + "playerlist.yml has been reloaded.");

                return true;
            }

        }

        if (command.getName().equalsIgnoreCase("rolyd"))
        {
            //check perms
            if (!sender.hasPermission("ShinyBase.shinybase"))
            {
                sender.sendMessage(ChatColor.RED + "You do not have permission to do that.");
                return true;
            }

            if (args[0].equalsIgnoreCase("newaccount"))
            {
                //args length
                if (args.length != 2)
                    return false;

                //create account and notify
                economy.createPlayerAccount(args[1]);
                sender.sendMessage(ChatColor.GREEN + "New account created: " + ChatColor.GOLD + args[1]);
                return true;
            }

            if (args[0].equalsIgnoreCase("help"))
            {
                //args length
                if (args.length != 1)
                    return false;

                //help info
                sender.sendMessage(ChatColor.RED + "/rolyd newaccount [accountname]" + ChatColor.WHITE + " - Create a new economy account.");

                return true;
            }


        }

        return false;
    }
}
