package org.aquacontroller.aguaquentecontroller;

import android.os.Environment;
import android.os.Handler;
import android.os.Looper;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewTreeObserver;
import android.widget.ListView;
import android.widget.Toast;

import com.fasterxml.jackson.databind.ObjectMapper;

import org.aquacontroller.aguaquentecontroller.application.Application;
import org.aquacontroller.aguaquentecontroller.data.DataIndicator;
import org.aquacontroller.aguaquentecontroller.data.State;
import org.aquacontroller.aguaquentecontroller.data.TeaPotState;
import org.aquacontroller.aguaquentecontroller.task.RequestStateTask;

import java.io.File;
import java.io.IOException;
import java.util.Collections;
import java.util.List;

import static org.aquacontroller.aguaquentecontroller.data.TeaPotState.*;

public class MainActivity extends AppCompatActivity implements TeaPotListener, ViewTreeObserver.OnGlobalLayoutListener {

    private Handler handler;
    private ListView indicatorList;
    private boolean init;
    public static int LIST_WIDTH;
    private IndicatorAdapter indicatorAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
	super.onCreate(savedInstanceState);
	setContentView(R.layout.activity_main);
	handler = new Handler(Looper.getMainLooper());
	final View btRequest = findViewById(R.id.button_request_state);
	indicatorList = (ListView) findViewById(R.id.indicators_list);
	btRequest.setOnClickListener(new View.OnClickListener() {
	    @Override
	    public void onClick(View v) {
		new RequestStateTask().execute();
	    }
	});
	State state = State.readFromFile(this);
	if (state == null) {
	    Application.getInstance().schedule();
	    state = new State();
	}
	state.writeToFile(this);
	init = false;
	indicatorList.getViewTreeObserver().addOnGlobalLayoutListener(this);
	indicatorAdapter = new IndicatorAdapter(this);
	indicatorList.setAdapter(indicatorAdapter);
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
    public void onTeaPotUpdate(final TeaPotState teaPotState) {
	handler.post(new Runnable() {
	    @Override
	    public void run() {
		final List<DataIndicator> indicators;
		if (teaPotState == null)
		    indicators = Collections.EMPTY_LIST;
		else
		    indicators = teaPotState.toIndicators();
		indicatorAdapter.setIndicators(indicators);
	    }
	});
    }

    @Override
    public void onGlobalLayout() {
	if (init)
	    return;
	init = true;
	LIST_WIDTH = indicatorList.getWidth();
	indicatorAdapter.notifyDataSetChanged();
    }
}
