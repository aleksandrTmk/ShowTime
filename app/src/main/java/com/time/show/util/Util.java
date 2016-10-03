package com.time.show.util;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.res.Resources;
import android.support.v4.content.LocalBroadcastManager;
import android.util.DisplayMetrics;


/**
 * Utility class with various helper methods
 *
 * @author aleksandrTmk
 */
public class Util {

    /**
     * Broadcasts a LocalBroadcast message with no parameters
     *
     * @param context App context
     * @param key     The message key to broadcast
     */
    public static void broadcastMessage(Context context, String key) {
        if (context == null){
            Blog.e(Util.class, "Failed to broadcast: " + key);
            return;
        }
        Blog.d(Util.class, "** Broadcast Event: " + key);
        Intent pttIntent = new Intent(key);
        LocalBroadcastManager.getInstance(context).sendBroadcast(pttIntent);
    }

    /**
     * Registers a broadcast receiver for the particular event
     *
     * @param context
     * @param broadcastReceiver
     * @param events String[] of events to register
     */
    public static void registerBroadcastReceiver(Context context, BroadcastReceiver broadcastReceiver, String[] events){
        if (context == null || broadcastReceiver == null || events == null || events.length == 0){
            return;
        }
        for (String event : events){
            LocalBroadcastManager.getInstance(context).registerReceiver(broadcastReceiver, new IntentFilter(event));
        }
    }

    /**
     * Unregisters broadcast receivers if they are non null
     *
     * @param context
     * @param broadcastReceiver
     */
    public static void unregisterBroadcastReceiver(Context context, BroadcastReceiver broadcastReceiver) {
        if (context == null || broadcastReceiver == null) {
            return;
        }
        LocalBroadcastManager.getInstance(context).unregisterReceiver(broadcastReceiver);
    }

    /**
     * Returns the screen density in DPI for this phone.
     *
     * i.e. 160, 240, etc
     *
     * @param context
     * @return
     */
    public static int getScreenDensity(Context context){
        int defaultDensity = Constants.SCREEN_DENSITY_DEFAULT;
        if (context == null){
            return defaultDensity;
        }
        Resources resources = context.getResources();
        if (resources == null){
            return defaultDensity;
        }
        DisplayMetrics metrics = resources.getDisplayMetrics();
        if (metrics == null){
            return defaultDensity;
        }
        return metrics.densityDpi;
    }
}
