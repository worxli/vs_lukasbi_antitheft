package ch.ethz.inf.vs.android.lukasbi.antitheft.eventlisteners;

import android.widget.SeekBar;
import android.widget.SeekBar.OnSeekBarChangeListener;

public class TimeoutEventListener implements OnSeekBarChangeListener {
	
	private int timeout;
	
	@Override
	public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
		this.timeout = progress;
	}

	@Override
	public void onStartTrackingTouch(SeekBar seekBar) {

	}

	@Override
	public void onStopTrackingTouch(SeekBar seekBar) {

	}

	public int getTimeout() {
		return timeout;
	}

}
