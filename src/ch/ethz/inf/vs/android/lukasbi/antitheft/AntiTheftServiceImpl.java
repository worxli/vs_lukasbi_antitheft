package ch.ethz.inf.vs.android.lukasbi.antitheft;

import java.util.regex.Pattern;

import android.accounts.Account;
import android.accounts.AccountManager;
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
import android.media.MediaPlayer;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.os.IBinder;
import android.os.Looper;
import android.os.SystemClock;
import android.support.v4.app.NotificationCompat;
import android.telephony.SmsManager;
import android.util.Log;
import android.util.Patterns;

public class AntiTheftServiceImpl extends Service implements AntiTheftService, LocationListener {
	
	class IssueMessager extends AsyncTask<Void, Void, Void> {

		@Override
		protected Void doInBackground(Void... arg0) {
			Log.d("A1", "SCHEISS DISTRIBUTED");
			// ----------------------------------------
			// Here comes the ENHANCEMENT section!
			// ----------------------------------------
			// vibrate and issue a sms/mail with the gps coordinates every 10 seconds
			
			// Get the location manager
		    locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
		    // Define the criteria how to select the locatioin provider -> use
		    // default
		    Criteria criteria = new Criteria();
		    provider = locationManager.getBestProvider(criteria, false);
		    Location location = locationManager.getLastKnownLocation(provider);   
		    
		    // getting GPS status
            isGPSEnabled = locationManager
                    .isProviderEnabled(LocationManager.GPS_PROVIDER);
 
            // getting network status
            isNetworkEnabled = locationManager
                    .isProviderEnabled(LocationManager.NETWORK_PROVIDER);
            
            if (!isGPSEnabled && !isNetworkEnabled) {
                // no network provider is enabled
            	Log.d("providers", "no network provider is enabled");
            } else {
			    try {
			    	if (isNetworkEnabled) {
			    		locationManager.requestSingleUpdate(
                                LocationManager.GPS_PROVIDER,
                                cont,
                                Looper.getMainLooper());
	                    Log.d("Network", "Network");
	                    if (locationManager != null) {
	                        location = locationManager.getLastKnownLocation(LocationManager.NETWORK_PROVIDER);
	                        if (location != null) {
	                            lat = location.getLatitude();
	                            lng = location.getLongitude();
	                        }
	                    }
			    	} 
			    	
			    	if (isGPSEnabled) {
			    		if (location == null) {
	                        locationManager.requestSingleUpdate(
	                                LocationManager.GPS_PROVIDER,
	                                cont,
	                                Looper.getMainLooper());
	                        Log.d("GPS Enabled", "GPS Enabled");
	                        if (locationManager != null) {
	                            location = locationManager
	                                    .getLastKnownLocation(LocationManager.GPS_PROVIDER);
	                            if (location != null) {
	                            	lat = location.getLatitude();
	    		                    lng = location.getLongitude();
	                            }
	                        }
	                    }
			    	}
		                    
		            Log.d("Location", lat + " , " + lng);
		            String msg = "Your phone alarm has gone of at: "+ lat + ", " + lng ;
		                    
		                    
                    if(number!=null){
                    	SmsManager sm = SmsManager.getDefault(); 
                    	sm.sendTextMessage(number, null, msg , null, null); 
                    } else {
                    	Log.d("number error", "no phone number specified");
                    	
                    	// get users account email and send to that
                    	Pattern emailPattern = Patterns.EMAIL_ADDRESS;
                    	Account[] accounts = AccountManager.get(getApplicationContext()).getAccounts();
                    	for (Account account : accounts) {
                    	    if (emailPattern.matcher(account.name).matches()) {
                    	        String email = account.name;
                    	        
                    	        Log.d("email", email);
                    	        
                    	        // send the gps coordinates in a mail ....
                    	        Intent emailIntent = new Intent(android.content.Intent.ACTION_SEND);
                    	        String[] recipients = new String[]{email};
                    	        emailIntent.putExtra(android.content.Intent.EXTRA_EMAIL, recipients);
                    	        emailIntent.putExtra(android.content.Intent.EXTRA_SUBJECT, "Your device is being stolen");
                    	        emailIntent.putExtra(android.content.Intent.EXTRA_TEXT, msg);
                    	        emailIntent.setType("text/plain");
                    	        startActivity(Intent.createChooser(emailIntent, "Send mail..."));
                    	    }
                    	}
                    }
		            
		        } catch (Exception e) {
		            e.printStackTrace();
		        }
            }
			return null;
		}
		
	}
	
	// flag for GPS status
    boolean isGPSEnabled = false;
 
    // flag for network status
    boolean isNetworkEnabled = false;
	
	//notification id
	private int notificationId = 001;
	
	// notificatoin manager
	private NotificationManager notificatoinMgr;
	
	// movement detector
	private MovementDetector movementDtr;
	
	// sensor manager to retrieve the accelerometer and the sensor
	private SensorManager sensorManager;
	private Sensor accelerometer;
	
	//location 
	private LocationManager locationManager;
	private String provider;
	
	private double lat;
	private double lng;
	
	//number to send gps data to
	private String number;
	
	// the interval in seconds to issue
	private int contInterval = 10;
	
	//timeout after which alarm cannot be disabled anymore
	int timeout;
	
	// context
	private AntiTheftServiceImpl cont = this;
	
	// used for delayed calls
	private Handler mPeriodicEventHandler;
	
	// mediaplayer
	MediaPlayer mp;
	
	@Override
	public void onCreate() {
		super.onCreate();
		
		// movement detector
		sensorManager = (SensorManager) getSystemService(Context.SENSOR_SERVICE);
		accelerometer = sensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER);
		
		// register accelerometer listener
		movementDtr = new MovementDetector(this, this);
		sensorManager.registerListener(movementDtr, accelerometer, SensorManager.SENSOR_DELAY_NORMAL);
		
		// periodic data handler
		mPeriodicEventHandler = new Handler();
	}
	
	@Override
	public int onStartCommand(Intent intent, int flags, int startId) {
		Bundle extras = intent.getExtras();
		boolean sensitivity = extras.getBoolean("sensitivity");
		this.timeout = Integer.parseInt(extras.getString("timeout"));
		this.number = extras.getString("number");
		
		movementDtr.setThreshold(sensitivity == false ? 1.2f : 2.4f );
		
		return super.onStartCommand(intent, flags, startId);
	}

	@Override
	public void onDestroy() {
		// clear the ongoing notificatoin
		if (this.notificatoinMgr != null) {
			this.notificatoinMgr.cancel(this.notificationId);
		}
		
		// unregister from the accelerometer and others
		sensorManager.unregisterListener(movementDtr);
		mPeriodicEventHandler.removeCallbacks(invokeMessages);    
		
		// disable alarm
		mp.stop();
		
		super.onDestroy();
	}

	@Override
	public void startAlarm() {
		
		/**
		 * create intent for notification. this will not create a new intent.
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
		
		//wait timeout for alarm, this wont freeze the UI because its a background service
		SystemClock.sleep(this.timeout * 1000);
		
		// call once directly and the each interval seconds
		mPeriodicEventHandler.postDelayed(invokeMessages, this.contInterval * 1000);
		
		// start playing the alarm file
		mp = MediaPlayer.create(cont, R.raw.alarm);
		mp.setLooping(true);
		mp.setVolume(1.0f, 1.0f);
		mp.start();
	}
	
	private final Runnable invokeMessages = new Runnable() {
		public void run () {
			IssueMessager im = new IssueMessager();
			im.execute();
		}
	};
	
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
