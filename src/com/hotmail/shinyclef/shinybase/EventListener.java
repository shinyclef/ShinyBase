package com.hotmail.shinyclef.shinybase;

import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;

/**
 * Author: ShinyClef
 * Date: 21/06/12
 * Time: 1:56 AM
 */

public class EventListener implements Listener
{
    private ShinyBase plugin;

    public EventListener(ShinyBase plugin)
    {
        plugin.getServer().getPluginManager().registerEvents(this, plugin);
        this.plugin = plugin;
    }

    @EventHandler
    public void eventPlayerLogin(PlayerJoinEvent ev)
    {
        plugin.getPlayerlistManager().ListCheckAdd(ev.getPlayer(), ev.getPlayer().getName());
    }
}
