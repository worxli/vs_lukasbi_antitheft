package ch.ethz.inf.vs.android.lukasbi.antitheft;

import java.util.ArrayList;

/**
 * Stores an entire data set. the source of the data set is the accelerometer
 * @author Nico
 *
 */
public class AccelDataSet {
	
	// singleton
	private static volatile AccelDataSet obj = null;
	
	// the data
	private ArrayList<AccelData> data;
	
	// singleton
	public static synchronized AccelDataSet getInstance () {
		if (obj == null) {
			obj = new AccelDataSet();
		}
		
		return obj;
	}
	
	private AccelDataSet () {
		data = new ArrayList<AccelData>();
	}
	
	public void add (AccelData d) {
		data.add(d);
	}
	
	public ArrayList<AccelData> get () {
		return data;
	}
	
	public void clear () {
		data.clear();
	}
	
	public boolean isEmpty () {
		return data.isEmpty();
	}
}
