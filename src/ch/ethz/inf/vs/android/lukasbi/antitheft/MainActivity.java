package ch.ethz.inf.vs.android.lukasbi.antitheft;

import java.util.ArrayList;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Configuration;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.ToggleButton;

import com.android.graphbutton.plot2d;

public class MainActivity extends Activity {
	
	//anti theft intent call
	private Intent antitheft;
	
	// togglebutton active
	boolean on = false;
	
	private static final int RESULT_SETTINGS = 1;
	
	public static boolean alarmStarted = false;
	
	@Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        
        // define antitheft service
        antitheft = new Intent(this, AntiTheftServiceImpl.class);
    }

	// orientation changed?
	@Override
	public void onConfigurationChanged(Configuration newConfig) {
		super.onConfigurationChanged(newConfig);
	}

	/**
	 * Eventhandler for the togglebutton
	 */
	public void onRunClicked (View v) {
		// Is the toggle on?
	    on = ((ToggleButton) v).isChecked();
	    
	    SharedPreferences sharedPrefs = PreferenceManager
                .getDefaultSharedPreferences(this);
	    
	    if (on) {
	    	// start anti theft service
	    	antitheft.putExtra("sensitivity", sharedPrefs.getBoolean("sensitivity", false));
	    	antitheft.putExtra("timeout", sharedPrefs.getString("timeout", "5"));
	    	antitheft.putExtra("number", sharedPrefs.getString("phone", null));
	    	this.startService(antitheft);
	    } else {
			// stop anti theft service
			this.stopService(antitheft);
			MainActivity.alarmStarted = false;
			
			//-------------------
			// Plotting the Data
			// important: The class which does the plotting is not coded by us!
			// it's an opensource class found in the internet
			//-------------------
			// get the view which to put in it
			ViewGroup v1 = (ViewGroup) findViewById(R.id.drawing_view);
			
			// this removes all child views to ensure no old data is displayed
			v1.removeAllViews();
			
			// the height of each dimension plot
			int margin = 10;
			int h = v1.getHeight();
			int height = (int) Math.floor(h / 3) - margin;
			AccelDataSet ads = AccelDataSet.getInstance();
			
			// The data
	        ArrayList<AccelData> data = ads.get();
	        
	        // some test data
	        /*
	        data.add(new AccelData(1, 1.920469, 0.61291564, 9.575105));
	        data.add(new AccelData(2, 2.920469, -1.61291564, 9.575105));
	        data.add(new AccelData(3, 3.920469, -2.61291564, 9.575105));
	        data.add(new AccelData(4, 4.920469, -3.61291564, 9.575105));
	        data.add(new AccelData(5, 5.920469, -4.61291564, 9.575105));
	        */
	        
	        // get the dimension data to plot
	        int size = data.size(); int i = 0;
	        float[] xValues = new float[size];
	        float[] yValues = new float[size];
	        float[] zValues = new float[size];
	        float[] time = new float[size];
	        long t = data.get(0).getTimestamp();
	        for (AccelData d : data) {
	        	xValues[i] = (float) d.getX();
	        	yValues[i] = (float) d.getY();
	        	zValues[i] = (float) d.getZ();
	        	time[i] = (float) (d.getTimestamp() - t);
	        	i++;
	        }
	        
	        // set the plots with appropriate heights and feed them with the data
	        LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(v1.getWidth(), height);
	        params.setMargins(margin, margin, margin, margin);
	        plot2d graphX = new plot2d(this, time, xValues, 1);
	        plot2d graphY = new plot2d(this, time, yValues, 1);
	        plot2d graphZ = new plot2d(this, time, zValues, 1);
	        v1.addView(graphX, params);
	        v1.addView(graphY, params);
	        v1.addView(graphZ, params);
	        
	        // clear the data
	        ads.clear();
	    }
	}
	

	@Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu this adds items to the action bar if it is present.
    	if(!on){
    		getMenuInflater().inflate(R.menu.main, menu);
    	}
    	
        return true;
    }
    
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
    	
	        switch (item.getItemId()) {
	 
		        case R.id.action_settings:
		            Intent i = new Intent(this, PrefActivity.class);
		            startActivityForResult(i, RESULT_SETTINGS);
		            break;
		    }
 
        return true;
    }
}
