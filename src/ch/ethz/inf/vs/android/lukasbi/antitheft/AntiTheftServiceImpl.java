package ch.ethz.inf.vs.android.lukasbi.antitheft;

import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.hardware.Sensor;
import android.hardware.SensorManager;
import android.os.Bundle;
import android.os.IBinder;
import android.os.Vibrator;
import android.support.v4.app.NotificationCompat;
import android.util.Log;

public class AntiTheftServiceImpl extends Service implements AntiTheftService {
	
	// vibrator for the alarm
	private Vibrator vib = null;
		
	//notification id
	private int notificationId = 001;
	
	// notificatoin manager
	private NotificationManager notificatoinMgr;
	
	// movement detector
	private MovementDetector movementDtr;
	
	// sensor manager to retrieve the accelerometer and the sensor
	private SensorManager sensorManager;
	private Sensor accelerometer;
	
	@Override
	public void onCreate() {
		super.onCreate();
		
		// init vibrator
		vib = (Vibrator) getSystemService(Context.VIBRATOR_SERVICE);
		
		/**
		 * create intent for notification. this will not create a new indent.
		 * instead it will retrieve the intent that was created before (like singleton)
		 */
		Intent notificationIntent = new Intent(this, MainActivity.class);
		notificationIntent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_SINGLE_TOP);
		PendingIntent intent = PendingIntent.getActivity(this, 0, notificationIntent, 0);

		// notification builder
		NotificationCompat.Builder mBuilder = new NotificationCompat.Builder(this)
			.setSmallIcon(R.drawable.ic_launcher)
			.setContentTitle(this.getString(R.string.app_name))
			.setContentIntent(intent)
			.setContentText(this.getString(R.string.notification_running))
			.setOngoing(true); // ongoing and no clear notification
		
		// gets an instance of the NotificationManager service
		this.notificatoinMgr = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
		
		// builds the notification and issues it
		notificatoinMgr.notify(this.notificationId, mBuilder.build());
		
		// movement detector
		sensorManager = (SensorManager) getSystemService(Context.SENSOR_SERVICE);
		accelerometer = sensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER);
		
		// register accelerometer listener
		movementDtr = new MovementDetector(this, this);
		sensorManager.registerListener(movementDtr, accelerometer, SensorManager.SENSOR_DELAY_NORMAL);
	}
	
	@Override
	public int onStartCommand(Intent intent, int flags, int startId) {
		Bundle extras = intent.getExtras();
		int sensivity = extras.getInt("sensivity");
		int timeout = extras.getInt("timeout");
		
		// set default to value 2
		sensivity = (sensivity == 0 ? 2 : sensivity);
		movementDtr.setThreshold(0.1f/(sensivity/1000.0f));
		movementDtr.setTimeout(timeout);
		
		float i = 0.1f/(sensivity/1000.0f);
		Log.d("#A1", Float.toString(i) + ", " + timeout);
		
		return super.onStartCommand(intent, flags, startId);
	}

	@Override
	public void onDestroy() {
		// clear the ongoing notificatoin
		this.notificatoinMgr.cancel(this.notificationId);
		
		// unregister from the accelerometer
		sensorManager.unregisterListener(movementDtr);
		super.onDestroy();
	}

	@Override
	public void startAlarm() {
		long[] pattern = {0, 50};
		vib.vibrate(pattern, -1);
	}

	@Override
	public IBinder onBind(Intent arg0) {
		// TODO Auto-generated method stub
		return null;
	}

}
