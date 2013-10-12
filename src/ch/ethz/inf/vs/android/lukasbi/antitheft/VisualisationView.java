package ch.ethz.inf.vs.android.lukasbi.antitheft;

import java.util.ArrayList;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.view.View;
/**
 * This class draws the data set retrieved from the accelerometer
 * into a canvas placed in the main acitvity under the button.
 * 
 * it draws the way the thief has walked in "top view" (like a bird).
 * horizontal is x-axis, vertical is y-axis
 */
public class VisualisationView extends View {
	private Paint mPaint = new Paint();
	private AccelDataSet data;
	
	public VisualisationView(Context context) {
		super(context);
		data = AccelDataSet.getInstance();
		
		// some sample data, this will draw something like this
		/*
		  \
		   \
		    \
		     \
		     /
		    /
		   /
		  /
		 /
		 \   x(origin) 
		  \  
		   \
		/*
		data.add(new AccelData(0, -10, -10, 5));
		data.add(new AccelData(1, -20, 30, 5));
		data.add(new AccelData(2, 20, 60, 5));
		data.add(new AccelData(3, -15, 120, 5));
		*/
	}
	
	@Override
	protected void onDraw(Canvas canvas) { 
		
		// paint object
		Paint paint = mPaint;

		float w = getWidth() / 2;
		float h = getHeight() / 2;
		
		// set the drawing origin to the middle of the x- and y-axis
		// because we may want to draw negative values
		canvas.translate((int) w, (int) h);
		
		// flip the canvas horizontally
		canvas.scale(1f, -1f);
		
		// default background color
        canvas.drawColor(Color.WHITE);
        
        // draw origin
        paint.setColor(Color.GRAY);
        paint.setStrokeWidth(4);
        canvas.drawPoint(0f, 0f, paint);
        
        // x-axis
        paint.setStrokeWidth(1);
        canvas.drawLine((float) -w, 0f, (float) w, 0f, paint);
        
        // y-axis
        paint.setStrokeWidth(1);
        canvas.drawLine(0f, (float) -h, 0f, (float) h, paint);

        if (!data.isEmpty()) {
        	ArrayList<AccelData> dataSet = data.get();
        	
        	// the size have to be four time as longs because we save [x0, y0, x0, y0, x1, y1, x1, y1,...]
        	float[] points = new float[dataSet.size() * 4];
        	int i = -1;
        	for (AccelData d : dataSet) {
        		points[++i] = (float) d.getX();
        		points[++i] = (float) d.getY();
        		
        		// set the start point of the next line to the end point of the line before
        		points[++i] = (float) d.getX();
        		points[++i] = (float) d.getY();
        	}
        	
        	// draw the way the person walked from the top in red dots
        	paint.setColor(Color.RED);
        	paint.setStrokeWidth(4);
        	
        	// offset of 2 values in the array because the first two are crap
        	canvas.drawLines(points, 2, dataSet.size() * 4 - 2, paint);
        }
	}
}
