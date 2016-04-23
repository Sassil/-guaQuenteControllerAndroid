package org.aquacontroller.aguaquentecontroller;

import android.os.Environment;
import android.os.Handler;
import android.os.Looper;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import com.fasterxml.jackson.databind.ObjectMapper;

import org.aquacontroller.aguaquentecontroller.data.TeaPotState;
import org.aquacontroller.aguaquentecontroller.task.RequestStateTask;

import java.io.File;
import java.io.IOException;

import static org.aquacontroller.aguaquentecontroller.data.TeaPotState.*;

public class MainActivity extends AppCompatActivity implements TeaPotListener {

    private Handler handler;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
	super.onCreate(savedInstanceState);
	setContentView(R.layout.activity_main);
	handler = new Handler(Looper.getMainLooper());
	final View btRequest = findViewById(R.id.button_request_state);
	btRequest.setOnClickListener(new View.OnClickListener() {
	    @Override
	    public void onClick(View v) {
		new RequestStateTask().execute();
	    }
	});
    }

    @Override
    protected void onPause() {
	super.onPause();
	TeaPotState.registerForUpdates(null);
    }

    @Override
    protected void onResume() {
	super.onResume();
	TeaPotState.registerForUpdates(this);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
	// Inflate the menu; this adds items to the action bar if it is present.
	getMenuInflater().inflate(R.menu.menu_main, menu);
	return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
	// Handle action bar item clicks here. The action bar will
	// automatically handle clicks on the Home/Up button, so long
	// as you specify a parent activity in AndroidManifest.xml.
	int id = item.getItemId();

	//noinspection SimplifiableIfStatement
	if (id == R.id.action_settings) {
	    return true;
	}

	return super.onOptionsItemSelected(item);
    }

    @Override
    public void onTeaPotUpdate(final TeaPotState state) {
	handler.post(new Runnable() {
	    @Override
	    public void run() {
		if (state == null)
		    Toast.makeText(MainActivity.this, "No data mi friendo", Toast.LENGTH_LONG).show();
		else
		    Toast.makeText(MainActivity.this, state.temperature + " " + state.volume, Toast.LENGTH_SHORT).show();
	    }
	});
    }
}
