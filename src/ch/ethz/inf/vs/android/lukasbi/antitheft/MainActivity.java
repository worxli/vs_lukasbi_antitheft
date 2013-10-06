package ch.ethz.inf.vs.android.lukasbi.antitheft;

import ch.ethz.inf.vs.android.lukasbi.antitheft.eventlisteners.SensivityEventListener;
import ch.ethz.inf.vs.android.lukasbi.antitheft.eventlisteners.TimeoutEventListener;
import android.app.Activity;
import android.os.Bundle;
import android.preference.PreferenceActivity;
import android.util.Log;
import android.view.Menu;
import android.view.View;
import android.widget.SeekBar;
import android.widget.SeekBar.OnSeekBarChangeListener;

public class MainActivity extends Activity {
	
	private int sensivity, timeout = 0;
	
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
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }
    
}
