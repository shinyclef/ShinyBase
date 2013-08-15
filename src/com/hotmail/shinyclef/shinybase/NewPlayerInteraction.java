package com.hotmail.shinyclef.shinybase;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.block.Chest;
import org.bukkit.configuration.Configuration;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;

import java.util.LinkedList;
import java.util.List;

/**
 * Author: ShinyClef
 * Date: 30/10/12
 * Time: 5:52 PM
 */

public class NewPlayerInteraction
{
    private static ShinyBase plugin;
    private static ItemStack[] items;

    private static List<MessageEvent> messages = new LinkedList<MessageEvent>();
    private Runnable runnable;
    private int index;
    private Player player;

    public NewPlayerInteraction(Player passedPlayer)
    {
        player = passedPlayer;
        runnableSetup();
        giveItems(player);
        readInfo();
    }

    public static void setupResources()
    {
        double x, y, z;
        Configuration config = plugin.getConfig();
        x = config.getDouble("ResourceChest.x");
        y = config.getDouble("ResourceChest.y");
        z = config.getDouble("ResourceChest.z");
        Location loc = new Location(plugin.getServer().getWorld("world"), x, y, z);



        //reference the new chest and its inventory
        Chest resourceChest;
        try
        {
            resourceChest = (Chest) plugin.getServer().getWorld("world").getBlockAt(loc).getState();
        }
        catch (ClassCastException e)
        {
            //we're finishing early if resource chest is not found
            plugin.getLogger().info("WARNING: THE RESOURCE CHEST COULD NOT BE FOUND. " +
                    "New players will not receive any items. Check config.yml for chest location.");
            return;
        }
        Inventory resourceChestInv = resourceChest.getBlockInventory();
        items = resourceChestInv.getContents();
    }

    public void runnableSetup()
    {
        runnable = new Runnable()
        {
            @Override
            public void run()
            {
                Player thisPlayer = player;

                ChatColor colour = ChatColor.YELLOW;
                if (index % 2 == 0)
                {
                    colour = ChatColor.GOLD;
                }

                thisPlayer.sendMessage(colour + messages.get(index).message);
                index++;
                if (index <= messages.size() - 1)
                {
                    Bukkit.getScheduler().scheduleSyncDelayedTask(plugin, runnable, messages.get(index).delay);
                }
            }
        };
    }

    public static void messageSetup()
    {
        messages.add(new MessageEvent("Welcome to RolyD.com. " +
                "Please take 20 seconds to read the following information.", 80L));
        messages.add(new MessageEvent("\"Where can I build?\"  You can build 500 blocks from spawn or any city. " +
                "You'll have to run from spawn or a warp point.", 160L));
        messages.add(new MessageEvent("Commands to get you started include /spawn, /sethome, /home & /warp list. " +
                "For a full list, visit: rolyd.com/commandlist", 160L));
        messages.add(new MessageEvent("Finally, please visit the information kiosk to read all the rules and info! " +
                "/warp kiosk. Please don't ask questions answered at Kiosk! /warp kiosk!!! Have fun!", 160L));
    }

    public static void giveItems(Player player)
    {
        Inventory inventory = player.getInventory();
        inventory.setContents(items);
    }

    public void readInfo()
    {
        index = 0;
        Bukkit.getScheduler().scheduleSyncDelayedTask(plugin, runnable, messages.get(0).delay);
    }

    private static class MessageEvent
    {
        String message;
        Long delay;

        private MessageEvent(String message, Long delay)
        {
            this.message = message;
            this.delay = delay;
        }
    }

    public static void setPlugin(ShinyBase plugin)
    {
        NewPlayerInteraction.plugin = plugin;
    }
}
