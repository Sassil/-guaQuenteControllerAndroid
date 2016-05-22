package org.aquacontroller.aguaquentecontroller.task;

import android.app.IntentService;
import android.content.Intent;
import android.util.Log;

import org.aquacontroller.aguaquentecontroller.data.TeaPotState;

public class SyncService extends IntentService {
    public SyncService() {
	super("AguaQuente Sync Service");
    }

    @Override
    protected void onHandleIntent(Intent intent) {
	TeaPotState.readFromServer();
	Log.i("SYNC", "Auto synching with server");
    }
}
