package org.aquacontroller.aguaquentecontroller.task;

import android.os.AsyncTask;
import android.widget.Toast;

import org.aquacontroller.aguaquentecontroller.R;
import org.aquacontroller.aguaquentecontroller.application.Application;

import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;

import static org.aquacontroller.aguaquentecontroller.data.TeaPotState.SERVER_URL;
import static org.aquacontroller.aguaquentecontroller.data.TeaPotState.TIMEOUT_MILLIS;

public class CancelTask extends AsyncTask<Void, Void, Boolean> {

    @Override
    protected Boolean doInBackground(Void... params) {
        final String id = URLEncoder.encode(Application.getInstance().getDeviceToken());
        final String serviceURL = String.format("/api/cancel/%s/", id);
        final String url = SERVER_URL + serviceURL;
        HttpURLConnection urlConnection = null;
        try {
            urlConnection = (HttpURLConnection) new URL(url).openConnection();
            urlConnection.setRequestMethod("GET");
            urlConnection.setRequestProperty("Accept", "application/json");
            urlConnection.setConnectTimeout(TIMEOUT_MILLIS);
            urlConnection.connect();
            return urlConnection.getResponseCode() == 200;
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (urlConnection != null)
                urlConnection.disconnect();
        }
        return false;
    }

    @Override
    protected void onPreExecute() {
	super.onPreExecute();
    }

    @Override
    protected void onPostExecute(Boolean result) {
	super.onPostExecute(result);
        try {
            if (result) {
                new RequestStateTask().execute();
            } else {
                Toast.makeText(Application.getInstance(), R.string.error_cancelling, Toast.LENGTH_SHORT).show();
            }
        } catch (Throwable e) {
            e.printStackTrace();
        }
    }
}
