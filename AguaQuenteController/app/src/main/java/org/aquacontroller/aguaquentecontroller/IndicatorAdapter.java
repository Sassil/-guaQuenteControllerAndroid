package org.aquacontroller.aguaquentecontroller;

import android.content.Context;
import android.content.res.Resources;
import android.support.annotation.NonNull;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import org.aquacontroller.aguaquentecontroller.data.DataIndicator;
import org.aquacontroller.aguaquentecontroller.data.TeaPotState;

import java.util.ArrayList;
import java.util.List;

public class IndicatorAdapter extends BaseAdapter {
    public class ViewHolder {
	public TextView title;
	public TextView value;
	public TextView min;
	public TextView max;
	public View bar;
    }

    private List<DataIndicator> indicatorList = new ArrayList<>();
    private LayoutInflater inflater;
    private Context context;

    public IndicatorAdapter(Context context) {
	this.context = context;
	this.inflater = LayoutInflater.from(context);
    }

    public void setIndicators(List<DataIndicator> indicators) {
	indicatorList.clear();
	indicatorList.addAll(indicators);
	notifyDataSetChanged();
    }

    @Override
    public int getCount() {
	return indicatorList.size();
    }

    @Override
    public Object getItem(int position) {
	return indicatorList.get(position);
    }

    @Override
    public long getItemId(int position) {
	return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
	final ViewHolder holder;
	if (convertView == null) {
	    convertView = inflater.inflate(R.layout.indicator, parent, false);
	    holder = new ViewHolder();
	    holder.title = (TextView) convertView.findViewById(R.id.indicator_title);
	    holder.value = (TextView) convertView.findViewById(R.id.indicator_value);
	    holder.min = (TextView) convertView.findViewById(R.id.indicator_min);
	    holder.max = (TextView) convertView.findViewById(R.id.indicator_max);
	    holder.bar = convertView.findViewById(R.id.indicator_bar);
	    convertView.setTag(holder);
	} else {
	    holder = (ViewHolder) convertView.getTag();
	}
	final DataIndicator indicator = indicatorList.get(position);
	holder.title.setText(context.getString(indicator.titleId));
	holder.value.setText(getValue(indicator.value, indicator));
	holder.min.setText(getValue(indicator.min, indicator));
	holder.min.setVisibility(indicator.showBounds ? View.VISIBLE : View.INVISIBLE);
	holder.max.setText(getValue(indicator.max, indicator));
	holder.max.setVisibility(indicator.showBounds ? View.VISIBLE : View.INVISIBLE);
	setIndicatorBar(indicator, holder);
	return convertView;
    }

    @NonNull

    private String getValue(double value, DataIndicator indicator) {
	if (indicator.isInteger)
	    return String.valueOf((int) value);
	String onOff;
	if(indicator.text_value==true){
	    onOff="on";

	}else{
	    onOff="off";
	}
	return String.valueOf(onOff);
    }

    private void setIndicatorBar(DataIndicator indicator, ViewHolder holder) {
	final double percentage;
	holder.bar.setVisibility(indicator.showBounds ? View.VISIBLE : View.INVISIBLE);
	if (!indicator.showBounds)
	    return;
	if (indicator.max == 0)
	    percentage = 0;
	else if (indicator.max < indicator.value - 1E-6)
	    percentage = 1;
	else
	    percentage = indicator.value / indicator.max;
	final ViewGroup.LayoutParams layoutParams = holder.bar.getLayoutParams();
	layoutParams.width = (int) (IndicatorsFragment.LIST_WIDTH * percentage);
	holder.bar.setLayoutParams(layoutParams);
	holder.bar.setBackgroundColor(getBarColor(percentage));
    }

    private int getBarColor(double percentage) {
	final Resources resources = context.getResources();
	if (percentage <= .25)
	    return resources.getColor(R.color.red_bad);
	else if (percentage <= .50)
	    return resources.getColor(R.color.orange_decent);
	else if (percentage <= .75)
	    return resources.getColor(R.color.yellow_medium);
	return resources.getColor(R.color.green_ok);
    }
}
