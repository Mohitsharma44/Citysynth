package com.example.btnear_service;


import android.app.Activity;
import android.bluetooth.BluetoothAdapter;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;


public class MainActivity extends Activity {

	private BluetoothAdapter myBluetoothAdapter;
	private Button onBtn;
	private Button offBtn;
	private ArrayAdapter<String> BTArrayAdapter;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		myBluetoothAdapter = BluetoothAdapter.getDefaultAdapter();
		//If cannot access Adapter, disable all functions
				if(myBluetoothAdapter == null){
					onBtn.setEnabled(false);
					offBtn.setEnabled(false);
					Toast.makeText(getApplicationContext(), "Activity: Bluetooth Device Not found..", Toast.LENGTH_SHORT).show();
				}else{
					onBtn = (Button) findViewById(R.id.turnOn);
					onBtn.setOnClickListener(new OnClickListener() {
						
						public void onClick(View v){
							on(v);
						}
					});
					offBtn = (Button) findViewById(R.id.turnOff);
					offBtn.setOnClickListener(new OnClickListener(){
						
						public void onClick(View v) {
							off(v);
						}
					});
					
				}

	}
	
	public void off(View v) {
		// TODO Auto-generated method stub
		myBluetoothAdapter.disable();
		Toast.makeText(getApplicationContext(), "Activity: Bluetooth is Off...", Toast.LENGTH_LONG).show();
		stopService(new Intent(this, Trial.class));
	}

	public void on(View v) {
		// TODO Auto-generated method stub
		/*if (!myBluetoothAdapter.isEnabled()) {
			Intent turnOnIntent = new Intent (BluetoothAdapter.ACTION_REQUEST_ENABLE);
			startActivityForResult(turnOnIntent, 1);
			
			Toast.makeText(getApplicationContext(), "Bluetooth On...", Toast.LENGTH_LONG).show();*/
			System.out.println("Activity: Initializing Service...");
			
			//myBluetoothAdapter.enable();
			//myBluetoothAdapter.startDiscovery();
			Intent startIntent = new Intent(MainActivity.this, Trial.class);
			//startService(new Intent(this, Trial.class));
			startService(startIntent);
			Toast.makeText(getApplicationContext(), "Activity: Initializing Service...", Toast.LENGTH_SHORT).show();
		//}
		/*else{
			Toast.makeText(getApplicationContext(), "Activity: Bluetooth is already On...", Toast.LENGTH_LONG).show();
		}*/
	}
}
