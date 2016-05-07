package org.aquacontroller.aguaquentecontroller.data;

import android.content.Context;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.File;
import java.io.IOException;
import java.util.Date;

public class State {
    final static ObjectMapper objectMapper = new ObjectMapper();

    public static final String STATE_DATA_FILE = "state_data.txt";

    @JsonProperty
    public Date lastRun;

    public void writeToFile(Context context) {
	final File dir = context.getExternalFilesDir(null);
	try {
	    lastRun = new Date();
	    objectMapper.writeValue(new File(dir, STATE_DATA_FILE), this);
	} catch (IOException e) {
	    e.printStackTrace();
	}
    }

    public static State readFromFile(Context context) {
	final File dir = context.getExternalFilesDir(null);
	try {
	    return objectMapper.readValue(new File(dir, STATE_DATA_FILE), new TypeReference<State>() {});
	} catch (IOException e) {
	    e.printStackTrace();
	    return null;
	}
    }
}
