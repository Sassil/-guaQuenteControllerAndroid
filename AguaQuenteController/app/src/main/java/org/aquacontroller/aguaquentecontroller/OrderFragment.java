package org.aquacontroller.aguaquentecontroller;

import android.content.Context;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import org.aquacontroller.aguaquentecontroller.MainPageAdapter.MainPageNavigator;
import org.aquacontroller.aguaquentecontroller.data.TeaPotState;
import org.aquacontroller.aguaquentecontroller.task.CancelTask;
import org.aquacontroller.aguaquentecontroller.task.ReserveTask;

public class OrderFragment extends Fragment implements TeaPotState.TeaPotListener {

    private Handler handler;
    private TextView cupNumber;
    private MainPageNavigator pageNavigator;
    private View orderButton;
    private int numberOfCups;
    private View orderMinus;
    private View orderPlus;
    private View cancelButton;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
	final View view = inflater.inflate(R.layout.order_fragment, container, false);
	cupNumber = (TextView) view.findViewById(R.id.cup_number);

	orderButton = view.findViewById(R.id.button_order);
	cancelButton = view.findViewById(R.id.button_cancel);
	orderPlus = view.findViewById(R.id.button_order_plus);
	orderMinus = view.findViewById(R.id.button_order_minus);
	final FragmentActivity activity = getActivity();
	if (activity instanceof MainPageNavigator)
	    pageNavigator = (MainPageNavigator) activity;
	initOrderButtons();
	return view;
    }

    private Handler getHandler() {
	if (handler == null)
	    handler = new Handler(Looper.getMainLooper());
	return handler;
    }

    public void setUserVisibleHint(boolean isVisibleToUser) {
	super.setUserVisibleHint(isVisibleToUser);
	TeaPotState.requestSingleUpdate(this);
    }

    private void initOrderButtons() {
	cancelButton.setOnClickListener(new View.OnClickListener() {
	    @Override
	    public void onClick(View v) {
		if (pageNavigator != null)
		    pageNavigator.goToPage(MainPageAdapter.PAGE_INDICATOR);
		final Context context = getActivity();
		Toast.makeText(context, context.getString(R.string.cancel_requested, numberOfCups), Toast.LENGTH_LONG).show();
		new CancelTask().execute();
	    }
	});
	orderButton.setOnClickListener(new View.OnClickListener() {
	    @Override
	    public void onClick(View v) {
		if (pageNavigator != null)
		    pageNavigator.goToPage(MainPageAdapter.PAGE_INDICATOR);
		final Context context = getActivity();
		Toast.makeText(context, context.getString(R.string.ordered_cups, numberOfCups), Toast.LENGTH_LONG).show();
		new ReserveTask().execute(numberOfCups);
	    }
	});
	orderPlus.setOnClickListener(new View.OnClickListener() {
	    @Override
	    public void onClick(View v) {
		numberOfCups++;
		updateCupNumberView();
	    }
	});
	orderMinus.setOnClickListener(new View.OnClickListener() {
	    @Override
	    public void onClick(View v) {
		numberOfCups--;
		updateCupNumberView();
	    }
	});
	  }

	  @Override
    public void onPause() {
	super.onPause();
    }

	  @Override
    public void onResume() {
	super.onResume();
    }

    @Override
    public void onTeaPotUpdate(final TeaPotState teaPotState) {
	getHandler();
	handler.post(new Runnable() {
	    @Override
	    public void run() {
		try {
		    if (teaPotState == null) {
			numberOfCups = 0;
		    } else {
			numberOfCups = teaPotState.numberOfCups;
		    }
		    updateCupNumberView();
		} catch (Exception e) {
		    e.printStackTrace();
		}
	    }
	});
    }

    private void updateCupNumberView() {
	cupNumber.setText(String.valueOf(numberOfCups));
    }

}
