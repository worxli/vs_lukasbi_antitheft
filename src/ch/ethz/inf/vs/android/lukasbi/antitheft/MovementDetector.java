package ch.ethz.inf.vs.android.lukasbi.antitheft;

import android.content.Context;
import android.hardware.Sensor;
import android.hardware.SensorEvent;

public class MovementDetector extends AbstractMovementDetector {
	
	private float threshold = 0.4f;

	// old x, y, z values from the sensor
	private float oldX, oldY, oldZ = 0;

	// timeout
	private int timeout = 5;
	
	//timestamp
	private long timestamp = 0;
	
	/**
	 * You have to pass the context for the systemservices
	 */
	public MovementDetector (Context context, AntiTheftService antiTS) {
		setCallbackService(antiTS);
	}

	/**
	 * We have to threshold the values, otherwise the sensor will always fire
	 */
	@Override
	public void onSensorChanged(SensorEvent event) {
		if (event.sensor.getType() == Sensor.TYPE_ACCELEROMETER) {
			
			//get sensor data
			float x, y, z;
			x = event.values[0];
            y = event.values[1];
            z = event.values[2];
            
            float diffX = Math.abs(Math.abs(oldX) - Math.abs(x));
            float diffY = Math.abs(Math.abs(oldY) - Math.abs(y));
            float diffZ = Math.abs(Math.abs(oldZ) - Math.abs(z));
            
            // only issue when the the sensor values aren't wthin a certain threshold
            if (diffX >= this.threshold || diffY >= this.threshold || diffZ >= this.threshold) {
            	
            	//phone hasn't been moved so far
            	if(timestamp==0){
            		//set timestamp when phone was moved first
            		timestamp = System.currentTimeMillis();
            	} else if((System.currentTimeMillis()-timestamp)>timeout*1000){
            		//TODO wait for timeout seconds and should only fire once
            		this.antiTheftService.startAlarm();
            	}
            	
            	oldX = x;
                oldY = y;
                oldZ = z;
            }
		}
	}

	public void setThreshold (float threshold) {
		this.threshold = threshold;
	}

	public void setTimeout(int timeout) {
		this.timeout = timeout;
	}
}
