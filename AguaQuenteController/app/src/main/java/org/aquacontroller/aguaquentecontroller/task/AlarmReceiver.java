package org.aquacontroller.aguaquentecontroller.task;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

public class AlarmReceiver extends BroadcastReceiver {
    @Override
    public void onReceive(Context context, Intent intent) {
	final Intent serviceIntent = new Intent(context, SyncService.class);
	context.startService(serviceIntent);
    }
}
