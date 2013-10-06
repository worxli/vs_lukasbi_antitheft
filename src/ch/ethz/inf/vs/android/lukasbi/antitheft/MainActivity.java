package ch.ethz.inf.vs.android.lukasbi.antitheft;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.View;
import android.widget.SeekBar;
import android.widget.ToggleButton;
import ch.ethz.inf.vs.android.lukasbi.antitheft.eventlisteners.SensivityEventListener;
import ch.ethz.inf.vs.android.lukasbi.antitheft.eventlisteners.TimeoutEventListener;

public class MainActivity extends Activity {
	
	/**
	 * anti theft intent call
	 */
	private Intent antitheft;
	
	@Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        
        // address objects
        SeekBar sensivitySB = (SeekBar) findViewById(R.id.seekbar_sensivity);
        SeekBar timeoutSB = (SeekBar) findViewById(R.id.seekbar_timeout);
        
        // define eventlisteners
        SensivityEventListener sensivityEL = new SensivityEventListener();
        TimeoutEventListener timeoutEL = new TimeoutEventListener();
        
        sensivitySB.setOnSeekBarChangeListener(sensivityEL);
        timeoutSB.setOnSeekBarChangeListener(timeoutEL);
        
        // define antitheft service
        antitheft = new Intent(this, AntiTheftServiceImpl.class);
      //antitheft.putExtra("KEY1", "Value to be used by the service");
    }

	/**
	 * Eventhandler for the togglebutton
	 */
	public void onRunClicked (View v) {
		// Is the toggle on?
	    boolean on = ((ToggleButton) v).isChecked();
	    
	    if (on) {
	    	// start anti theft service
	    	this.startService(antitheft);
	    } else {
			// stop anti theft service
			this.stopService(antitheft);
	    }
	}

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }
    
}
