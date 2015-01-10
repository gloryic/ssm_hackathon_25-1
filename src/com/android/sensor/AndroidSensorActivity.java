package com.android.sensor;

import android.app.Activity;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.Bundle;
import android.util.Log;
import android.widget.TextView;

public class AndroidSensorActivity extends Activity implements SensorEventListener{
    /** Called when the activity is first created. */
	
	SensorManager sm;
	SensorEventListener accL;
	SensorEventListener oriL;
	SensorEventListener ligL;
	SensorEventListener magL;
	SensorEventListener preL;
	SensorEventListener proxL;
	SensorEventListener tempL;
	
	Sensor oriSensor; // 방향
	Sensor accSensor; // 가속도
	Sensor rotSensor; // 회전
	Sensor ligSensor; // 밝기
	Sensor magSensor; // 자기
	Sensor preSensor; // 압력
	Sensor proxSensor; // 근접
	Sensor tempSensor; // 온도
	
	float maxAccX;
	float maxAccY;
	float maxAccZ;
	
	TextView ori, acc, rot, lig, mag, pre, prox, temp;
	TextView maxAcc;
    
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);
        
        sm = (SensorManager)getSystemService(SENSOR_SERVICE); 
        
        oriSensor = sm.getDefaultSensor(Sensor.TYPE_ORIENTATION); // 방향
        accSensor = sm.getDefaultSensor(Sensor.TYPE_ACCELEROMETER); // 가속도
        rotSensor = sm.getDefaultSensor(Sensor.TYPE_GYROSCOPE); // 회전
        ligSensor = sm.getDefaultSensor(Sensor.TYPE_LIGHT); // 밝기
        magSensor = sm.getDefaultSensor(Sensor.TYPE_MAGNETIC_FIELD); // 자력
        preSensor = sm.getDefaultSensor(Sensor.TYPE_PRESSURE); // 압력
        proxSensor = sm.getDefaultSensor(Sensor.TYPE_PROXIMITY); // 근접
        tempSensor = sm.getDefaultSensor(Sensor.TYPE_TEMPERATURE); // 온도
        
        ori = (TextView)findViewById(R.id.ori);
        acc = (TextView)findViewById(R.id.acc);
        rot = (TextView)findViewById(R.id.rot);
        lig = (TextView)findViewById(R.id.lig);
        mag = (TextView)findViewById(R.id.mag);
        pre = (TextView)findViewById(R.id.pre);
        prox = (TextView)findViewById(R.id.prox);
        temp = (TextView)findViewById(R.id.temp);
        maxAcc = (TextView)findViewById(R.id.maxAcc);

        maxAccX = -10;
        maxAccY = -10;
        maxAccZ = -10;
        
        
    }
    
    
	@Override
	protected void onResume() {
		// TODO Auto-generated method stub
		super.onResume();
		
		sm.registerListener(this, accSensor, SensorManager.SENSOR_DELAY_NORMAL); // 가속도
		sm.registerListener(this, oriSensor, SensorManager.SENSOR_DELAY_NORMAL); // 방향
		sm.registerListener(this, rotSensor, SensorManager.SENSOR_DELAY_NORMAL); // 회전
		sm.registerListener(this, ligSensor, SensorManager.SENSOR_DELAY_NORMAL); // 밝기
		sm.registerListener(this, magSensor, SensorManager.SENSOR_DELAY_NORMAL); // 자력
		sm.registerListener(this, preSensor, SensorManager.SENSOR_DELAY_NORMAL); // 압력
		sm.registerListener(this, proxSensor, SensorManager.SENSOR_DELAY_NORMAL); // 근접
		sm.registerListener(this, tempSensor, SensorManager.SENSOR_DELAY_NORMAL); // 온도
		
	}
	
	

	@Override
	protected void onPause() {
		// TODO Auto-generated method stub
		super.onPause();
		sm.unregisterListener(this);
	}


	@Override
	public void onAccuracyChanged(Sensor sensor, int accuracy) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void onSensorChanged(SensorEvent event) {
		// TODO Auto-generated method stub
		synchronized (this) {
			float var0 = event.values[0];
			float var1 = event.values[1];
			float var2 = event.values[2];
	        Log.i("aa","a");
			switch (event.sensor.getType()) {
			case Sensor.TYPE_ACCELEROMETER:
				
				acc.setText("x = " + var0 + ", y = " + var1 +" , z = " + var2);
				
				if(maxAccX < var0)
					maxAccX = var0;
				if(maxAccY < var1)
					maxAccY = var1;
				if(maxAccZ < var2)
					maxAccZ = var2;
				
				maxAcc.setText("X : " + maxAccX + ", Y : " + maxAccY + ", Z : " + maxAccZ);
				
				
				break;

			case Sensor.TYPE_ORIENTATION:
				
				ori.setText("x = " + var0 + ", y = " + var1 +" , z = " + var2);
				
				break;
			case Sensor.TYPE_GYROSCOPE:
				
				rot.setText("x = " + var0 + ", y = " + var1 +" , z = " + var2);
				
				break;
			case Sensor.TYPE_LIGHT:
				
				lig.setText("x = " + var0 + ", y = " + var1 +" , z = " + var2);
				
				break;
			case Sensor.TYPE_MAGNETIC_FIELD:
				
				mag.setText("x = " + var0 + ", y = " + var1 +" , z = " + var2);
				
				break;
			case Sensor.TYPE_PRESSURE:
				
				pre.setText("x = " + var0 + ", y = " + var1 +" , z = " + var2);
	
				break;
			case Sensor.TYPE_PROXIMITY:
	
				prox.setText("x = " + var0 + ", y = " + var1 +" , z = " + var2);
	
				break;
			case Sensor.TYPE_TEMPERATURE:
	
				temp.setText("x = " + var0 + ", y = " + var1 +" , z = " + var2);
	
				break;
			
			}
			                          
			                          
		}
	}
    
	
}