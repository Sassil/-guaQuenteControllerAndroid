package org.aquacontroller.aguaquentecontroller.task;

import android.os.AsyncTask;

import org.aquacontroller.aguaquentecontroller.data.TeaPotState;

public class RequestStateTask extends AsyncTask<Void, Void, Void> {

    @Override
    protected Void doInBackground(Void... params) {
	TeaPotState.readFromServer();
	return null;
    }

    @Override
    protected void onPreExecute() {
	super.onPreExecute();
    }

    @Override
    protected void onPostExecute(Void aVoid) {
	super.onPostExecute(aVoid);
    }
}
