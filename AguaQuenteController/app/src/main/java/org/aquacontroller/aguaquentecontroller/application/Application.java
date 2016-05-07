package org.aquacontroller.aguaquentecontroller.application;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;

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
}
