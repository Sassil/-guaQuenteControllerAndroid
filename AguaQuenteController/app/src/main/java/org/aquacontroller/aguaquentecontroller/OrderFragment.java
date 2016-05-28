package org.aquacontroller.aguaquentecontroller;

import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import org.aquacontroller.aguaquentecontroller.data.TeaPotState;

public class OrderFragment extends Fragment implements TeaPotState.TeaPotListener {

    private Handler handler;
    private TextView cupNumber;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
	final View view = inflater.inflate(R.layout.order_fragment, container, false);
	handler = new Handler(Looper.getMainLooper());
	cupNumber = (TextView) view.findViewById(R.id.cup_number);
	return view;
    }

    @Override
    public void onPause() {
	super.onPause();
	TeaPotState.registerForUpdates(null);
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
		    final int numberOfCups;
		    if (teaPotState == null) {
			numberOfCups = 0;
		    } else {
			numberOfCups = teaPotState.numberOfCups;
		    }
		    cupNumber.setText(String.valueOf(numberOfCups));
		} catch (Exception e) {
		    e.printStackTrace();
		}
	    }
	});
    }

}
