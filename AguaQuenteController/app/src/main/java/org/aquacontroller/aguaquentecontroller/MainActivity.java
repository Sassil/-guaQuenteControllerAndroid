package org.aquacontroller.aguaquentecontroller;

import android.os.Bundle;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;

import org.aquacontroller.aguaquentecontroller.application.Application;
import org.aquacontroller.aguaquentecontroller.data.State;
import org.aquacontroller.aguaquentecontroller.data.TeaPotState;

public class MainActivity extends AppCompatActivity implements MainPageAdapter.MainPageNavigator {

    private MainPageAdapter pageAdapter;
    private ViewPager pager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
	super.onCreate(savedInstanceState);
	setContentView(R.layout.activity_main);
	pager = (ViewPager) findViewById(R.id.pager);
	pageAdapter = new MainPageAdapter(getSupportFragmentManager());
	pager.setAdapter(pageAdapter);
	State state = State.readFromFile(this);
	if (state == null) {
	    Application.getInstance().schedule();
	    state = new State();
	}
	state.writeToFile(this);

	Toast.makeText(this, Application.getInstance().getDeviceToken() + "", Toast.LENGTH_LONG).show();
    }

    @Override
    protected void onRestoreInstanceState(Bundle savedInstanceState) {
	super.onRestoreInstanceState(savedInstanceState);
	if (savedInstanceState != null)
	    pager.setCurrentItem(0, false);
    }

    @Override
    protected void onPause() {
	super.onPause();
    }

    @Override
    protected void onResume() {
	super.onResume();
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
    public void goToPage(int page) {
	try {
	    if (pager != null)
		pager.setCurrentItem(page, true);
	} catch (Exception e) {
	    e.printStackTrace();
	}
    }
}
