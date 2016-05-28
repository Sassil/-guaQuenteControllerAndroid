package org.aquacontroller.aguaquentecontroller;

import android.app.Activity;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
import android.widget.Button;
import android.widget.ListView;

import org.aquacontroller.aguaquentecontroller.data.DataIndicator;
import org.aquacontroller.aguaquentecontroller.data.TeaPotState;
import org.aquacontroller.aguaquentecontroller.task.RequestStateTask;

import java.util.Collections;
import java.util.List;

public class IndicatorsFragment extends Fragment implements TeaPotState.TeaPotListener, ViewTreeObserver.OnGlobalLayoutListener {

    private Handler handler;
    private ListView indicatorList;
    private boolean init;
    public static int LIST_WIDTH;
    private IndicatorAdapter indicatorAdapter;
    private Button onOffButton;
    private boolean onState;
    private View syncButton;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
	final View view = inflater.inflate(R.layout.indicator_fragment, container, false);
	handler = new Handler(Looper.getMainLooper());
	indicatorList = (ListView) view.findViewById(R.id.indicators_list);
	syncButton = view.findViewById(R.id.button_request_state);
	syncButton.setOnClickListener(new View.OnClickListener() {
	    @Override
	    public void onClick(View v) {
		new RequestStateTask().execute();
	    }
	});
	onOffButton = (Button) view.findViewById(R.id.button_on_off);
	onOffButton.setOnClickListener(new View.OnClickListener() {
	    @Override
	    public void onClick(View v) {
		//TODO Send request
		updateOnOfButton(!onState);
	    }
	});
	final Activity context = getActivity();
	init = false;
	indicatorList.getViewTreeObserver().addOnGlobalLayoutListener(this);
	indicatorAdapter = new IndicatorAdapter(context);
	indicatorList.setAdapter(indicatorAdapter);
	return view;
    }

    @Override
    public void onPause() {
	super.onPause();
	TeaPotState.unregisterForUpdates(this);
    }

    @Override
    public void onResume() {
	super.onResume();
	TeaPotState.registerForUpdates(this);
    }

    @Override
    public void onTeaPotUpdate(final TeaPotState teaPotState) {
	handler.post(new Runnable() {
	    @Override
	    public void run() {
		try {
		    final List<DataIndicator> indicators;
		    final boolean isOn;
		    if (teaPotState == null) {
			indicators = Collections.EMPTY_LIST;
			isOn = false;
		    } else {
			indicators = teaPotState.toIndicators();
			isOn = teaPotState.isOn;
		    }
		    indicatorAdapter.setIndicators(indicators);
		    updateOnOfButton(isOn);
		} catch (Exception e) {
		    e.printStackTrace();
		}
	    }
	});
    }

    private void updateOnOfButton(boolean isOn) {
	onState = isOn;
	onOffButton.setText(getString(onState ? R.string.on : R.string.off));
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

