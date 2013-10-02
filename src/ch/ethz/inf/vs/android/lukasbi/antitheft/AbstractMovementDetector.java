package ch.ethz.inf.vs.android.nethz.antitheft;

import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;

public abstract class AbstractMovementDetector implements SensorEventListener {
	protected AntiTheftService antiTheftService;
	
	protected void setCallbackService(AntiTheftService service) {
		antiTheftService = service;
	}
	
	/**
	 * Implements the sensor logic that is needed to trigger the alarm.
	 * Calls antiTheftService.startAlarm() if a deliberate movement is detected.
	 */
	@Override
	abstract public void onSensorChanged(SensorEvent event);
	
	@Override
	public void onAccuracyChanged(Sensor sensor, int accuracy) {
		// Do Nothing
	}
}
