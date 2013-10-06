package ch.ethz.inf.vs.android.lukasbi.antitheft.eventlisteners;

import android.widget.SeekBar;
import android.widget.SeekBar.OnSeekBarChangeListener;

public class SensivityEventListener implements OnSeekBarChangeListener {
	
	private int sensivity = 0;

	@Override
	public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
		this.sensivity = progress;
	}

	@Override
	public void onStartTrackingTouch(SeekBar seekBar) {
		
	}

	@Override
	public void onStopTrackingTouch(SeekBar seekBar) {
		
	}

	public int getSensivity () {
		return sensivity;
	}
	
}
