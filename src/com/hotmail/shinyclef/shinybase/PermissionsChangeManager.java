package com.hotmail.shinyclef.shinybase;

import java.util.HashSet;
import java.util.Set;

/**
 * User: Shinyclef
 * Date: 6/10/13
 * Time: 6:20 PM
 */

public class PermissionsChangeManager
{
    private static Set<PermissionListener> registeredListeners = new HashSet<>();

    public static void registerListener(PermissionListener theListener)
    {
        registeredListeners.add(theListener);
    }

    public static void unregisterListener(PermissionListener theListener)
    {
        registeredListeners.remove(theListener);
    }

    public static void firePermissionsChangeEvent()
    {
        for (PermissionListener listener : registeredListeners)
        {
            listener.permissionChangeEvent();
        }
    }
}
