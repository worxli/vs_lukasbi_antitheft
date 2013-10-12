package ch.ethz.inf.vs.android.lukasbi.antitheft;

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
import android.widget.ToggleButton;

public class MainActivity extends Activity {
	
	//anti theft intent call
	private Intent antitheft;
	
	// togglebutton active
	boolean on = false;
	
	private static final int RESULT_SETTINGS = 1;
	
	@Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        
        // define antitheft service
        antitheft = new Intent(this, AntiTheftServiceImpl.class);
        
        // draw the accelerometer data as a "walk-path"
        VisualisationView graphicsView = new VisualisationView(this);
        ViewGroup v = (ViewGroup) findViewById(R.id.drawing_view);
        v.addView(graphicsView);
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
			
			// delete the recorded data
			AccelDataSet ads = AccelDataSet.getInstance();
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
