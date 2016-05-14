package org.aquacontroller.aguaquentecontroller.data;

import android.content.Context;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;

import org.aquacontroller.aguaquentecontroller.R;
import org.aquacontroller.aguaquentecontroller.application.Application;

import java.io.File;
import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

public class TeaPotState {
    final static ObjectMapper objectMapper = new ObjectMapper();
    public static final int TIMEOUT_MILLIS = 60000;
    private static final double TEMPERATURE_MIN = 0;
    private static final double TEMPERATURE_MAX = 100;
    private static final double VOLUME_MIN = 0;
    private static final double VOLUME_MAX = 100;

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
    @JsonProperty
    public int numberOfCups;

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

    public synchronized static TeaPotState readFromServer() {
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

    public List<DataIndicator> toIndicators() {
	final List<DataIndicator> indicators = new ArrayList<>();
	DataIndicator indicator;
	// Temperature
	indicator = new DataIndicator();
	indicator.titleId = R.string.temperature;
	indicator.min = TEMPERATURE_MIN;
	indicator.max = TEMPERATURE_MAX;
	indicator.value = temperature;
	indicators.add(indicator);
	// Volume
	indicator = new DataIndicator();
	indicator.titleId = R.string.volume;
	indicator.min = VOLUME_MIN;
	indicator.max = VOLUME_MAX;
	indicator.value = volume;
	indicators.add(indicator);
	return indicators;
    }

}
