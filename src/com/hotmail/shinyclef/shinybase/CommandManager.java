package com.hotmail.shinyclef.shinybase;

import org.bukkit.Bukkit;
import org.bukkit.command.*;
import org.bukkit.plugin.Plugin;
import org.bukkit.plugin.PluginManager;

import java.lang.reflect.Field;
import java.util.HashMap;

/**
 * Author: Shinyclef
 * Date: 15/08/13
 * Time: 6:31 PM
 */

public class CommandManager
{
    private static ShinyBase plugin;

    public static void initialize(ShinyBase thePlugin)
    {
        plugin = thePlugin;
    }

    /* Register/unregister a command from bukkit. */
    public static void modifyBukkitCommands(Plugin plugin, String cmdName, boolean putting, CommandExecutor executor)
    {
        try
        {
            PluginManager pluginManager = plugin.getServer().getPluginManager();
            Object result = getPrivateField(pluginManager, "commandMap");
            SimpleCommandMap commandMap = (SimpleCommandMap) result;
            Object map = getPrivateField(commandMap, "knownCommands");
            @SuppressWarnings("unchecked")
            HashMap<String, Command> knownCommands = (HashMap<String, Command>) map;

            if (putting)
            {
                CustomCommand theCommand = new CustomCommand(cmdName);
                knownCommands.put(cmdName, theCommand);
                theCommand.setExecutor(executor);
            }
            else //removing
            {
                PluginCommand cmd = Bukkit.getPluginCommand(cmdName);
                knownCommands.remove(cmd.getName());
                for (String alias : cmd.getAliases())
                {
                    knownCommands.remove(alias);
                }
            }

        }
        catch (SecurityException e)
        {
            e.printStackTrace();
        }
        catch (IllegalArgumentException e)
        {
            e.printStackTrace();
        }
        catch (NoSuchFieldException e)
        {
            e.printStackTrace();
        }
        catch (IllegalAccessException e)
        {
            e.printStackTrace();
        }
    }

    /* Getting the private field of another class */
    private static Object getPrivateField(Object object, String field) throws SecurityException,
            NoSuchFieldException, IllegalArgumentException, IllegalAccessException
    {
        Class<?> clazz = object.getClass();
        Field objectField = clazz.getDeclaredField(field);
        objectField.setAccessible(true);
        Object result = objectField.get(object);
        objectField.setAccessible(false);
        return result;
    }

    private static class CustomCommand extends Command
    {

        private CommandExecutor exe = null;

        protected CustomCommand(String name)
        {
            super(name);
        }

        public boolean execute(CommandSender sender, String commandLabel,String[] args)
        {
            if(exe != null)
            {
                exe.onCommand(sender, this, commandLabel,args);
            }
            return false;
        }

        public void setExecutor(CommandExecutor exe)
        {
            this.exe = exe;
        }
    }
}
