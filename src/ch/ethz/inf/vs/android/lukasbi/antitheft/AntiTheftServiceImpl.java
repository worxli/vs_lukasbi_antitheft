package ch.ethz.inf.vs.android.lukasbi.antitheft;

import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.hardware.Sensor;
import android.hardware.SensorManager;
import android.location.Criteria;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.os.IBinder;
import android.os.Vibrator;
import android.support.v4.app.NotificationCompat;
import android.util.Log;

public class AntiTheftServiceImpl extends Service implements AntiTheftService, LocationListener {
	
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
	
	private LocationManager locationManager;
	private String provider;
	
	private double lat;
	private double lng;
	
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
		boolean sensitivity = extras.getBoolean("sensitivity");
		int timeout = Integer.parseInt(extras.getString("timeout"));
		
		movementDtr.setThreshold(sensitivity == false ? 1.2f : 2.4f );
		movementDtr.setTimeout(timeout);
		
		Log.d("Threshold", sensitivity == false ? "0.4f" : "0.8f");
		Log.d("TTimeout", ""+timeout);
		
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
		
		// Get the location manager
	    locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
	    // Define the criteria how to select the locatioin provider -> use
	    // default
	    Criteria criteria = new Criteria();
	    provider = locationManager.getBestProvider(criteria, false);
	    Location location = locationManager.getLastKnownLocation(provider);
	    onLocationChanged(location);
	    try {
            locationManager.requestLocationUpdates(
                    LocationManager.NETWORK_PROVIDER, 0, 0, this);
            if (locationManager != null) {
                location = locationManager
                        .getLastKnownLocation(LocationManager.NETWORK_PROVIDER);
                if (location != null) {
                    lat = location.getLatitude();
                    lng = location.getLongitude();
                    
                    Log.d("Location", lat + " , " + lng);
                    //TODO send to sms/email
                } else {
                	Log.d("error", "no location");
                }
            } else {
            	Log.d("error", "no locationmanager");
            }
            
        } catch (Exception e) {
            e.printStackTrace();
        }
	}

	@Override
	public IBinder onBind(Intent arg0) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void onLocationChanged(Location location) {
	    
	  }

	@Override
	public void onProviderDisabled(String arg0) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void onProviderEnabled(String arg0) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void onStatusChanged(String arg0, int arg1, Bundle arg2) {
		// TODO Auto-generated method stub
		
	}

}
