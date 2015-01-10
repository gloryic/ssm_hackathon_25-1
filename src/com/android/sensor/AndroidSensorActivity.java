package com.android.sensor;

import java.net.MalformedURLException;

import org.json.JSONException;
import org.json.JSONObject;

import io.socket.IOAcknowledge;
import io.socket.IOCallback;
import io.socket.SocketIO;
import io.socket.SocketIOException;
import android.app.Activity;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.TextView;

public class AndroidSensorActivity extends Activity implements IOCallback, SensorEventListener, OnClickListener{
    /** Called when the activity is first created. */
	
	SensorManager sm;
	SensorEventListener accL;
	SensorEventListener oriL;
	SensorEventListener ligL;
	SensorEventListener magL;
	SensorEventListener preL;
	SensorEventListener proxL;
	SensorEventListener tempL;
	
	Sensor oriSensor; // ����
	Sensor accSensor; // ���ӵ�
	Sensor rotSensor; // ȸ��
	Sensor ligSensor; // ���
	Sensor magSensor; // �ڱ�
	Sensor preSensor; // �з�
	Sensor proxSensor; // ����
	Sensor tempSensor; // �µ�
	
	float maxAccX;
	float maxAccY;
	float maxAccZ;
	
	TextView ori, acc, rot, lig, mag, pre, prox, temp;
	TextView maxAcc;
	
    private Button btn_Connect;
    SocketIO socket;
    
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);
        
        
		socket = new SocketIO();
		try {
			socket.connect("http://192.168.0.95:3000/", this);
		} catch (MalformedURLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		btn_Connect = (Button)findViewById(R.id.btn_connect);
		btn_Connect.setOnClickListener(this);
		
        sm = (SensorManager)getSystemService(SENSOR_SERVICE); 
        
        oriSensor = sm.getDefaultSensor(Sensor.TYPE_ORIENTATION); // ����
        accSensor = sm.getDefaultSensor(Sensor.TYPE_ACCELEROMETER); // ���ӵ�
        rotSensor = sm.getDefaultSensor(Sensor.TYPE_GYROSCOPE); // ȸ��
        ligSensor = sm.getDefaultSensor(Sensor.TYPE_LIGHT); // ���
        magSensor = sm.getDefaultSensor(Sensor.TYPE_MAGNETIC_FIELD); // �ڷ�
        preSensor = sm.getDefaultSensor(Sensor.TYPE_PRESSURE); // �з�
        proxSensor = sm.getDefaultSensor(Sensor.TYPE_PROXIMITY); // ����
        tempSensor = sm.getDefaultSensor(Sensor.TYPE_TEMPERATURE); // �µ�
        
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
		
		sm.registerListener(this, accSensor, SensorManager.SENSOR_DELAY_NORMAL); // ���ӵ�
		sm.registerListener(this, oriSensor, SensorManager.SENSOR_DELAY_NORMAL); // ����
		sm.registerListener(this, rotSensor, SensorManager.SENSOR_DELAY_NORMAL); // ȸ��
		sm.registerListener(this, ligSensor, SensorManager.SENSOR_DELAY_NORMAL); // ���
		sm.registerListener(this, magSensor, SensorManager.SENSOR_DELAY_NORMAL); // �ڷ�
		sm.registerListener(this, preSensor, SensorManager.SENSOR_DELAY_NORMAL); // �з�
		sm.registerListener(this, proxSensor, SensorManager.SENSOR_DELAY_NORMAL); // ����
		sm.registerListener(this, tempSensor, SensorManager.SENSOR_DELAY_NORMAL); // �µ�
		
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
	       // Log.i("aa","a");
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


	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		int getId = v.getId();
		
		if(getId==R.id.btn_connect){
			Log.i("socket", "start");
			
			try {
				
				// Sends a string to the server.
				//socket.send("Hello Server");
				// Sends a JSON object to the server.
				//socket.send(new JSONObject().put("key", "value").put("key2","another value"));
				// Emits an event to the server.
				//socket.emit("event", "argument1", "argument2", 13.37);
				
				socket.emit("fromclient", new JSONObject().put("msg", "test"));
				  
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
	}
	
	@Override
	public void onMessage(JSONObject json, IOAcknowledge ack) {
		try {
			System.out.println("Server said:" + json.toString(2));
		} catch (JSONException e) {
			e.printStackTrace();
		}
	}
	
	@Override
	//�������� emit�ϸ� callback
	public void on(String event, IOAcknowledge ack, Object... args) {
		System.out.println("Server triggered event '" + event + "'");
	}	

	@Override
	public void onMessage(String data, IOAcknowledge ack) {
		System.out.println("Server said: " + data);
	}

	@Override
	public void onError(SocketIOException socketIOException) {
		System.out.println("an Error occured");
		socketIOException.printStackTrace();
		
		try {
			socket.connect("http://192.168.0.95:3000/", this);
		} catch (MalformedURLException e) {
			e.printStackTrace();
		}
	}

	@Override
	public void onDisconnect() {
		
		System.out.println("Connection terminated.");
		
		try {
			socket.connect("http://192.168.0.95:3000/", this);
		} catch (MalformedURLException e) {
			e.printStackTrace();
		}
	}

	@Override
	public void onConnect() {
		System.out.println("Connection established");
	}
}