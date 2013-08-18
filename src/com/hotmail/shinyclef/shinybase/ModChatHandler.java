package com.hotmail.shinyclef.shinybase;

import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.event.player.AsyncPlayerChatEvent;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.ArrayList;
import java.util.List;

/**
 * User: Shinyclef
 * Date: 19/08/13
 * Time: 4:19 AM
 */

public class ModChatHandler
{
    public static final String MOD_BYPASS_CHAR = "ʘ";

    private ShinyBase plugin;
    private List<String> modChatToggledOffList;

    public ModChatHandler(ShinyBase plugin)
    {
        //plugin
        this.plugin = plugin;

        //modChatToggleOffList
        modChatToggledOffList = plugin.getConfig().getStringList("ModChatOff");
        if (modChatToggledOffList == null)
        {
            modChatToggledOffList = new ArrayList<String>();
        }
    }

    public void modStandardChat(CommandSender sender, String senderName, String sentence)
    {
        senderName = senderName.toLowerCase();

        if (sentence.equals(""))
        {
            return; //do nothing!
        }

        if (sender instanceof Player)
        {
            if (!modChatToggledOffList.contains(senderName))
            {
                sentence = MOD_BYPASS_CHAR + sentence;
            }

            ((Player) sender).chat(sentence);
        }
    }

    public boolean changeModChatToggle(CommandSender sender, boolean defaultIsMB)
    {
        if (!sender.hasPermission("rolyd.mod"))
        {
            sender.sendMessage(ChatColor.RED + "You do not have permission to do that.");
            return true;
        }

        if (defaultIsMB)
        {
            modChatToggledOffList.remove(sender.getName().toLowerCase());
        }
        else
        {
            modChatToggledOffList.add(sender.getName().toLowerCase());
        }

        //feedback
        sender.sendMessage(ChatColor.DARK_GREEN + "Default chat set to " + ChatColor.BLUE +
                (defaultIsMB ? "MB" : "standard") + ChatColor.DARK_GREEN + ".");

        //save config
        plugin.getConfig().set("ModChatOff", modChatToggledOffList);
        plugin.saveConfig();

        return true;
    }

    public boolean defaultIsModChat(CommandSender sender)
    {
        return sender.hasPermission("rolyd.mod") &&
                !modChatToggledOffList.contains(sender.getName().toLowerCase());
    }

    public void newModChatEvent(AsyncPlayerChatEvent e)
    {
        new ModChatEvent(e.getPlayer(), e.getMessage()).runTask(plugin);
    }

    private class ModChatEvent extends BukkitRunnable
    {
        Player player;
        String message;

        private ModChatEvent(Player player, String message)
        {
            this.player = player;
            this.message = message;
        }

        @Override
        public void run()
        {
            player.performCommand("mb " + message);
        }
    }


    /* Getters */

    public List<String> getModChatToggledOffList()
    {
        return modChatToggledOffList;
    }
}
