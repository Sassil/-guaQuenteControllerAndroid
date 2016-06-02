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
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.net.URLEncoder;

public class TeaPotState {
    final static ObjectMapper objectMapper = new ObjectMapper();
    public static final int TIMEOUT_MILLIS = 4000;
    private static final double TEMPERATURE_MIN = 0;
    private static final double TEMPERATURE_MAX = 100;
    private static final double VOLUME_MIN = 0;
    private static final double VOLUME_MAX = 1000;
    public static final String SERVER_URL = "http://192.168.10.89:5080";

    public interface TeaPotListener {
	void onTeaPotUpdate(TeaPotState state);
    }

    private static Set<TeaPotListener> listeners = new HashSet<>();
    public static final String TEAPOT_DATA_FILE = "teapot_data.txt";

    @JsonProperty
    public double temperature;
    @JsonProperty
    public double volume;
    @JsonProperty
    public boolean isOn;
    @JsonProperty
    public int numberOfCups;

    public void writeToFile(Context context) {
	final File dir = context.getExternalFilesDir(null);
	try {
	    dir.mkdirs();
	    objectMapper.writeValue(new File(dir, TEAPOT_DATA_FILE), this);
	    update(this);
	} catch (IOException e) {
	    e.printStackTrace();
	}
    }

    private static TeaPotState readFromFile(Context context) {
	final File dir = context.getExternalFilesDir(null);
	try {
	    return objectMapper.readValue(new File(dir, TEAPOT_DATA_FILE), new TypeReference<TeaPotState>() {
	    });
	} catch (IOException e) {
	    e.printStackTrace();
	    return new TeaPotState();
	}
    }

    public synchronized static TeaPotState readFromServer() {
	String id=URLEncoder.encode(Application.getInstance().getDeviceToken());
	final String url = SERVER_URL + "/api/update/id/"+id+"/";
	HttpURLConnection urlConnection = null;
	try {
	    urlConnection = (HttpURLConnection) new URL(url).openConnection();
	    urlConnection.setRequestMethod("GET");
	    urlConnection.setRequestProperty("Accept", "application/json");
	    urlConnection.setConnectTimeout(TIMEOUT_MILLIS);
	    final TeaPotState state;
	    state = objectMapper.readValue(urlConnection.getInputStream(), new TypeReference<TeaPotState>() {
	    });
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
	if (listener == null)
	    throw new IllegalArgumentException();
	TeaPotState.listeners.add(listener);
	update(readFromFile(Application.getInstance()));
    }

    public static void requestSingleUpdate(TeaPotListener listener) {
	singleUpdate(readFromFile(Application.getInstance()), listener);
    }

    public static void unregisterForUpdates(TeaPotListener listener) {
	if (listener == null)
	    throw new IllegalArgumentException();
	TeaPotState.listeners.remove(listener);
    }

    public static void update(TeaPotState state) {
	if (listeners == null)
	    return;
	for (TeaPotListener listener : listeners) {
	    singleUpdate(state, listener);
	}
    }

    private static void singleUpdate(TeaPotState state, TeaPotListener listener) {
	if (listener != null)
	    listener.onTeaPotUpdate(state);
    }

    public List<DataIndicator> toIndicators() {
	final List<DataIndicator> indicators = new ArrayList<>();
	DataIndicator indicator;
	DataIndicator indicator2;
	// Temperature
	indicator = new DataIndicator();
	indicator.titleId = R.string.temperature;
	indicator.min = TEMPERATURE_MIN;
	indicator.max = TEMPERATURE_MAX;
	indicator.value = temperature;
	indicator.showBounds = true;
	indicator.isInteger=true;
	indicators.add(indicator);
	// Volume
	indicator = new DataIndicator();
	indicator.titleId = R.string.volume;
	indicator.min = VOLUME_MIN;
	indicator.max = VOLUME_MAX;
	indicator.value = volume;
	indicator.showBounds = true;
	indicator.isInteger=true;
	indicators.add(indicator);
	// Cups
	indicator = new DataIndicator();
	indicator.titleId = R.string.cups;
	indicator.value = numberOfCups;

	indicator.isInteger = true;
	indicators.add(indicator);
	//on_off
	indicator = new DataIndicator();
	indicator.titleId = R.string.status_on_off;
	indicator.isInteger = false;
	indicator.text_value=this.isOn;

	indicators.add(indicator);



	return indicators;
    }

}
