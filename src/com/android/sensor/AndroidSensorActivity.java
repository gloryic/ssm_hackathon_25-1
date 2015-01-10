package com.android.sensor;

import java.net.MalformedURLException;

import org.json.JSONException;
import org.json.JSONObject;

import io.socket.IOAcknowledge;
import io.socket.IOCallback;
import io.socket.SocketIO;
import io.socket.SocketIOException;
import android.app.Activity;
import android.content.Context;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.Bundle;
import android.os.Vibrator;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

public class AndroidSensorActivity extends Activity implements IOCallback, SensorEventListener, OnClickListener{
    /** Called when the activity is first created. */
	
	SensorManager sm;
	SensorEventListener accL;
	SensorEventListener oriL;

	
	Sensor oriSensor; // 방향
	Sensor accSensor; // 가속도
	Sensor rotSensor; // 회전

	
	float maxAccX;
	float maxAccY;
	float maxAccZ;
	
	private long lastTime;
	private long lastShakingTime;
	private float speed;
	private float lastX;
	private float lastY;
	private float lastZ;
	private float x, y, z;
	private static final int SHAKE_THRESHOLD = 800;
	private int shackeCnt = 0;;
	Vibrator vibe;
	

	private static final int CMD_NON = 0;
	private static final int CMD_LEFT = 1;
	private static final int CMD_RIGHT = 2;
	private static final int CMD_GO = 3;
	private static final int CMD_BACK = 4;
	private static final int CMD_SHAKE = 5;
	private int curCMD = CMD_NON;
	private int nextCMD = CMD_NON;
	private boolean isShaking = false;
	private boolean gameStart = false;
	
	private String userid = "";
	
	
	private long lastCmdActTime = 0;
	
	TextView testT1;
	TextView testT2;
	TextView testT3;
	TextView testT4;	
	TextView testT5;	
	
    private Button btn_Connect;
    private Button btn_id;
    
    private Button btn_start;
    private EditText et;
   
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
		et = (EditText)findViewById(R.id.editText1);
		btn_Connect = (Button)findViewById(R.id.btn_connect);
		btn_Connect.setOnClickListener(this);
		btn_id = (Button)findViewById(R.id.idButton);
		btn_id.setOnClickListener(this);
		btn_start = (Button)findViewById(R.id.btn_start);
		btn_start.setOnClickListener(this);
		
		btn_start.setEnabled(false);

        sm = (SensorManager)getSystemService(SENSOR_SERVICE); 
        
        oriSensor = sm.getDefaultSensor(Sensor.TYPE_ORIENTATION); // 방향
        accSensor = sm.getDefaultSensor(Sensor.TYPE_ACCELEROMETER); // 가속도
        rotSensor = sm.getDefaultSensor(Sensor.TYPE_GYROSCOPE); // 회전

        testT1 = (TextView)findViewById(R.id.myTest1);
        testT2 = (TextView)findViewById(R.id.myTest2);
        testT3 = (TextView)findViewById(R.id.myTest3);
        testT4 = (TextView)findViewById(R.id.myTest4);
        testT5 = (TextView)findViewById(R.id.myTest5);
        vibe = (Vibrator) getSystemService(Context.VIBRATOR_SERVICE);

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
	public void sendCMD(String msg,int cmd) {
		
		try {
			
			socket.emit("fromclient", new JSONObject().put("msg", msg + "[" +cmd + "]"));
		} catch (Exception e) {
			e.printStackTrace();
		}

	}
	@Override
	public void onSensorChanged(SensorEvent event) {
		// TODO Auto-generated method stub
		synchronized (this) {
			float var0 = event.values[0];
			float var1 = event.values[1];
			float var2 = event.values[2];
			
			long currentTime = System.currentTimeMillis();
			switch (event.sensor.getType()) {
			case Sensor.TYPE_ACCELEROMETER:

				if(maxAccX < var0)
					maxAccX = var0;
				if(maxAccY < var1)
					maxAccY = var1;
				if(maxAccZ < var2)
					maxAccZ = var2;
				

				
	            long gabOfTime = (currentTime - lastTime);
	            if (gabOfTime > 100) {
	                lastTime = currentTime;
	                x = var0;
	                y = var1;
	                z = var2;
	 
	                speed = Math.abs(x + y + z - lastX - lastY - lastZ) / gabOfTime * 10000;
	 
	                if (speed > SHAKE_THRESHOLD || ( isShaking && speed > SHAKE_THRESHOLD-300)) {
	                	lastShakingTime = currentTime;
	                	isShaking = true;
	                	shackeCnt++;
	                	testT3.setText("shaking [" + shackeCnt +"]" );
	                }
	                
	                
	                
	                lastX = var0;
	                lastY = var1;
	                lastZ = var2;
	            }
	            
	            if((currentTime - lastShakingTime )>1000){
	            	shackeCnt = 0;
	            	isShaking = false;
	          
	            	testT3.setText("Not shaking " );
	            
	            }
	            if(shackeCnt>2){
	            	//vibe.vibrate(200);
	            }
				
				break;

			case Sensor.TYPE_ORIENTATION:
				
				if(var2 >=25.0){
					testT1.setText("좌로간다");
					nextCMD = CMD_LEFT;

				}else if(var2 <= -25.0){
					testT1.setText("우로간다");
					nextCMD = CMD_RIGHT;

				}else if(var1 >= -50.0){
					testT2.setText("앞으로간다");
					nextCMD = CMD_GO;

				}else if(var1<= -100.0){
					
					testT2.setText("뒤로간다");
					nextCMD = CMD_BACK;

				}else{
					nextCMD = CMD_NON;
					testT1.setText("---");

					testT2.setText("---");
				}
				
				break;
			case Sensor.TYPE_GYROSCOPE:
	
				break;
			
			}
			long gapCMDTime = currentTime - lastCmdActTime ;
			if(gameStart){
				if(isShaking){
					sendCMD("test",CMD_SHAKE);
					//vibe.vibrate(100);
					testT4.setText("[SSSSHAAAKKIIINNG]");
				}else if(curCMD!=nextCMD){

					curCMD = nextCMD;
					lastCmdActTime = currentTime;
					if(curCMD !=CMD_NON ){
						vibe.vibrate(100);
						sendCMD("test",curCMD);	
					}
					testT4.setText("1[" + curCMD +"]");
				}else if(gapCMDTime>=400 && lastCmdActTime != 0 ){
					lastCmdActTime = currentTime;
					if(curCMD !=CMD_NON ){
						vibe.vibrate(100);
						sendCMD("test",curCMD);	
					}
					testT4.setText("[" + curCMD +"]");
				}
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
				
				//socket.emit("fromclient", new JSONObject().put("msg", "test"));
				//btn_id.setEnabled(true);
				//btn_id.setFocusable(true);
	
			} catch (Exception e) {
				e.printStackTrace();
			}
		}else if(getId==R.id.idButton){
			
			
			try {

				
				this.userid = "id : "+ et.getText().toString();
				

				socket.emit("fromclient", new JSONObject().put("msg",  userid ));
				
		

	
			} catch (Exception e) {
				e.printStackTrace();
			}
			
			
		}else if(getId==R.id.btn_start){
			
			gameStart = true;

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
	//서버에서 emit하면 callback
	public void on(String event, IOAcknowledge ack, Object... args) {
		System.out.println("Server triggered event '" + event + "'");
		JSONObject recvjson = (JSONObject)args[0];

		Log.i("recv  ", recvjson.toString());
		
		
		try {
			Log.i("de 1 ", recvjson.getString("msg"));
			Log.i("de 2 ", this.userid);
			if(recvjson.getString("msg").compareTo(this.userid) ==0 ){
				btn_start.setEnabled(true);

				btn_id.setEnabled(false);

				et.setEnabled(false);

			}else{
				Log.i("de 4 ", this.userid);
			}

			
			
		} catch (JSONException e) {
			e.printStackTrace();
		}



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