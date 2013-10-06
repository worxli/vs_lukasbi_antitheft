package ch.ethz.inf.vs.android.lukasbi.antitheft;

import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Intent;
import android.os.IBinder;
import android.os.Vibrator;
import android.support.v4.app.NotificationCompat;

public class AntiTheftServiceImpl extends Service implements AntiTheftService {

	private Vibrator vib = null;
	
	/**
	 * notification id
	 */
	private int notificationId = 001;
	
	/**
	 * notificatoin manager
	 */
	private NotificationManager notificatoinMgr;
	
	@Override
	public void onCreate() {
		super.onCreate();
		
		/*
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
		this.notificatoinMgr = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
		
		// Builds the notification and issues it.
		notificatoinMgr.notify(this.notificationId, mBuilder.build());
		
		/*
		vib = (Vibrator) getSystemService(VIBRATOR_SERVICE);
		long[] pattern = {0, 100, 100, 200, 100, 100};
		vib.vibrate(pattern, -1);
		*/
	}
	
	@Override
	public void onDestroy() {
		// clear the ongoing notificatoin
		this.notificatoinMgr.cancel(this.notificationId);
		super.onDestroy();
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
