package org.aquacontroller.aguaquentecontroller.data;

import android.content.Context;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;

import org.aquacontroller.aguaquentecontroller.application.Application;

import java.io.File;
import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLConnection;

public class TeaPotState {
    final static ObjectMapper objectMapper = new ObjectMapper();
    public static final int TIMEOUT_MILLIS = 60000;

    public interface TeaPotListener {
	void onTeaPotUpdate(TeaPotState state);
    }

    private static TeaPotListener listener;
    public static final String TEAPOT_DATA_FILE = "teapot_data.txt";

    @JsonProperty
    public double temperature;
    @JsonProperty
    public double volume;
    @JsonProperty
    public boolean isOn;

    private void writeToFile(Context context) {
	final File dir = context.getExternalFilesDir(null);
	try {
	    objectMapper.writeValue(new File(dir, TEAPOT_DATA_FILE), this);
	    update(this);
	} catch (IOException e) {
	    e.printStackTrace();
	}
    }

    private static TeaPotState readFromFile(Context context) {
	final File dir = context.getExternalFilesDir(null);
	try {
	    return objectMapper.readValue(new File(dir, TEAPOT_DATA_FILE), new TypeReference<TeaPotState>() {});
	} catch (IOException e) {
	    e.printStackTrace();
	    return null;
	}
    }

    public static TeaPotState readFromServer() {
	final String url = "http://localhost:8090";
	HttpURLConnection urlConnection = null;
	try {
	    urlConnection = (HttpURLConnection) new URL(url).openConnection();
	    urlConnection.setRequestMethod("GET");
	    urlConnection.setRequestProperty("Accept", "application/json");
	    urlConnection.setConnectTimeout(TIMEOUT_MILLIS);
	    final TeaPotState state;
	    state = objectMapper.readValue(urlConnection.getInputStream(), new TypeReference<TeaPotState>() {});
	    state.writeToFile(Application.getInstance());
	    return state;
	} catch (Exception e) {
	    e.printStackTrace();
	} finally {
	    if (urlConnection != null)
		urlConnection.disconnect();
	}
	return null;
    }

    public static void registerForUpdates(TeaPotListener listener) {
	TeaPotState.listener = listener;
	update(readFromFile(Application.getInstance()));
    }

    public static void update(TeaPotState state) {
	if (listener == null)
	    return;
	listener.onTeaPotUpdate(state);
    }
}
