package ch.ethz.inf.vs.android.lukasbi.antitheft;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;
import android.util.Log;

public class AntiTheftServiceImpl extends Service implements AntiTheftService {

	//private Vibrator vib = null;

	@Override
	public void onCreate() {
		// TODO Auto-generated method stub
		super.onCreate();
		Log.d("#A1", "antitheft service created");
	}
	
	@Override
	public void onDestroy() {
		// TODO Auto-generated method stub
		super.onDestroy();
		Log.d("#A1", "antitheft service destroyed");
	}

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
