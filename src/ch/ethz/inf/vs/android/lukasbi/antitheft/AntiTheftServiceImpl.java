package ch.ethz.inf.vs.android.lukasbi.antitheft;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;
import android.os.Vibrator;

public class AntiTheftServiceImpl extends Service implements AntiTheftService {

	//private Vibrator vib = null;
	
	@Override
	public void startAlarm() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public IBinder onBind(Intent arg0) {
		// TODO Auto-generated method stub
		return null;
	}

}
