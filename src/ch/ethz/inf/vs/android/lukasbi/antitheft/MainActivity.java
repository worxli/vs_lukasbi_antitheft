package ch.ethz.inf.vs.android.lukasbi.antitheft;

import android.app.Activity;
import android.content.Intent;
import android.content.res.Configuration;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.View;
import android.widget.SeekBar;
import android.widget.ToggleButton;
import ch.ethz.inf.vs.android.lukasbi.antitheft.eventlisteners.SensivityEventListener;
import ch.ethz.inf.vs.android.lukasbi.antitheft.eventlisteners.TimeoutEventListener;

public class MainActivity extends Activity {
	
	//anti theft intent call
	private Intent antitheft;
	
	// seekbars
	SeekBar sensivitySB, timeoutSB;
	
	// seekbars eventlisteners
	SensivityEventListener sensivityEL;
	TimeoutEventListener timeoutEL;
	
	// togglebutton active
	boolean on = false;
	
	@Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        
        // address objects
        sensivitySB = (SeekBar) findViewById(R.id.seekbar_sensivity);
        timeoutSB = (SeekBar) findViewById(R.id.seekbar_timeout);
        
        // define eventlisteners
        sensivityEL = new SensivityEventListener();
        timeoutEL = new TimeoutEventListener();
        
        sensivitySB.setOnSeekBarChangeListener(sensivityEL);
        timeoutSB.setOnSeekBarChangeListener(timeoutEL);
        
        // define antitheft service
        antitheft = new Intent(this, AntiTheftServiceImpl.class);
    }

	// orientation changed?
	@Override
	public void onConfigurationChanged(Configuration newConfig) {
		super.onConfigurationChanged(newConfig);
		this.setSeekBarsVisibility();
		Log.d("#A1", "fabian");
	}

	/**
	 * Eventhandler for the togglebutton
	 */
	public void onRunClicked (View v) {
		// Is the toggle on?
	    on = ((ToggleButton) v).isChecked();
	    
	    if (on) {
	    	// start anti theft service
	    	antitheft.putExtra("sensivity", sensivityEL.getSensivity());
	    	antitheft.putExtra("timeout", timeoutEL.getTimeout());
	    	this.startService(antitheft);
	    } else {
			// stop anti theft service
			this.stopService(antitheft);
	    }
	    
	    this.setSeekBarsVisibility();
	}
	

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }
    
    private void setSeekBarsVisibility () {
    	if (on) {
    		// disable seekbars
    		sensivitySB.setEnabled(false);
    		timeoutSB.setEnabled(false);
    	} else {
    		// enable seekbars
    		sensivitySB.setEnabled(true);
    		timeoutSB.setEnabled(true);
    	}
    }
}
