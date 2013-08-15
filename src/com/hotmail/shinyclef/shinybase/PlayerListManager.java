package com.hotmail.shinyclef.shinybase;

import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Author: ShinyClef
 * Date: 21/06/12
 * Time: 2:01 AM
 */

class PlayerListManager
{
    private ShinyBase plugin;
    private List<String> playerlist = null;
    private FileConfiguration ymlplayerlist = null;
    private File fileplayerlist = null;

    public PlayerListManager(ShinyBase plugin) //constructor
    {
        this.plugin = plugin;
        playerlist = getConfigPlayerlist().getStringList("players");
    }


    public void reloadConfigPlayerlist()
    {
        //if file var is null (like at startup), ...
        if (fileplayerlist == null)
        {
            //make a new file object and direct it to location in data folder.
            fileplayerlist = new File(plugin.getDataFolder(), "playerlist.yml");
        }
        //file defs exists now. set yml var to file var.
        ymlplayerlist = YamlConfiguration.loadConfiguration(fileplayerlist);

        //look for defaults in the jar
        InputStream defConfigStream = plugin.getResource("playerlist.yml");

        //if there is a default playerlist.yml (which there is)
        if (defConfigStream != null)
        {
            YamlConfiguration defConfig = YamlConfiguration.loadConfiguration(defConfigStream);
            ymlplayerlist.setDefaults(defConfig);
        }
    }

    public FileConfiguration getConfigPlayerlist()
    {
        if (ymlplayerlist == null)
        {
            this.reloadConfigPlayerlist();
        }
        return ymlplayerlist;
    }

    public void saveConfigPlayerlist() {
        if (ymlplayerlist == null || fileplayerlist == null)
        {
            return;
        }

        try
        {
            ymlplayerlist.save(fileplayerlist);
        }
        catch (IOException ex)
        {
            plugin.getLogger().log(Level.SEVERE, "Could not save config to " + fileplayerlist, ex);
        }
    }

    public void ListCheckAdd (Player player, String playername)
    {
        //if the name is not on the list
        if (!playerlist.contains(playername.toLowerCase()))
        {
            //add them to the list
            playerlist.add(playername.toLowerCase());
            getConfigPlayerlist().set("players", playerlist);
            saveConfigPlayerlist();

            //initiate first time interaction
            new NewPlayerInteraction(player);

        }
    }

    public List<String> getPlayers()
    {
        return playerlist;
    }
}