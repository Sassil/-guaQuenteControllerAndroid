package org.aquacontroller.aguaquentecontroller.application;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.net.wifi.WifiInfo;
import android.net.wifi.WifiManager;
import android.provider.Settings;

import org.aquacontroller.aguaquentecontroller.task.AlarmReceiver;

public class Application extends android.app.Application {
    private static Application instance;
    private AlarmManager alarmMgr;
    private PendingIntent alarmIntent;
    @Override
    public void onCreate() {
	super.onCreate();
	instance = this;
    }

    public static Application getInstance() {
	return instance;
    }

    public void schedule() {
	alarmMgr = (AlarmManager) getSystemService(Context.ALARM_SERVICE);
	final Intent intent = new Intent(this, AlarmReceiver.class);
	alarmIntent = PendingIntent.getBroadcast(this, 0, intent, 0);
	alarmMgr.setInexactRepeating(AlarmManager.ELAPSED_REALTIME, 0, 60 * 1000, alarmIntent);
    }

    public String getDeviceToken() {
	try {
	    final WifiManager manager = (WifiManager) getSystemService(Context.WIFI_SERVICE);
	    final WifiInfo info = manager.getConnectionInfo();
	    return info.getMacAddress();
	} catch (Exception e) {
	    e.printStackTrace();
	    return Settings.Secure.getString(getApplicationContext().getContentResolver(), Settings.Secure.ANDROID_ID);
	}
    }
}
