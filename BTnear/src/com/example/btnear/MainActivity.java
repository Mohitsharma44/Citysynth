package com.example.btnear;

import java.io.File;
import java.io.PrintWriter;
import java.util.Set;

import android.app.Activity;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.os.Environment;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;


public class MainActivity extends Activity {
	
	private Button onBtn;
	private Button offBtn;
	private Button listBtn;
	private Button findBtn;
	private TextView text;
	private BluetoothAdapter myBluetoothAdapter;
	private Set<BluetoothDevice> pairedDevices;
	private ListView myListView;
	private ArrayAdapter<String> BTArrayAdapter;
	private int REQUEST_ENABLE_BT = 1;
	PrintWriter out = null;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		
		//Create an instance of Bluetooth Radio from Bluetooth Adapter
		myBluetoothAdapter = BluetoothAdapter.getDefaultAdapter();
		
		//If cannot access Adapter, disable all functions
		if(myBluetoothAdapter == null){
			onBtn.setEnabled(false);
			offBtn.setEnabled(false);
			listBtn.setEnabled(false);
			findBtn.setEnabled(false);
			text.setText("Status: Error..No Bluetooth Radio found");
			Toast.makeText(getApplicationContext(), "Bluetooth Device Not found..", Toast.LENGTH_SHORT).show();
		}else{
			text = (TextView) findViewById(R.id.text);
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
			
			listBtn = (Button)findViewById(R.id.paired);
			listBtn.setOnClickListener(new OnClickListener(){

				@Override
				public void onClick(View v) {
					// TODO Auto-generated method stub
					list(v);
				}
				
			});
			
			findBtn = (Button) findViewById(R.id.search);
			findBtn.setOnClickListener(new OnClickListener(){

				@Override
				public void onClick(View v) {
					// TODO Auto-generated method stub
					find(v);
				}
				
			});
			
			myListView = (ListView)findViewById(R.id.listView1);
			
			//Creating an array containing new Devices
			BTArrayAdapter = new ArrayAdapter<String>(this,android.R.layout.simple_list_item_1);
			myListView.setAdapter(BTArrayAdapter);
		}
	
	}
	
	final BroadcastReceiver bReceiver = new BroadcastReceiver(){

		@Override
		public void onReceive(Context context, Intent intent) {
			// TODO Auto-generated method stub
			String action = intent.getAction();
			//When a new device is found
			if(BluetoothDevice.ACTION_FOUND.equals(action)) {
				BluetoothDevice device = intent.getParcelableExtra(BluetoothDevice.EXTRA_DEVICE);
				BTArrayAdapter.add(device.getName() + "\n" + device.getAddress());
				BTArrayAdapter.notifyDataSetChanged();
				System.out.println(BTArrayAdapter.getCount());
				//for (int i=1;i<=BTArrayAdapter.getCount();i++){
				//System.out.println("Contents of Array: "+BTArrayAdapter.getItem(0));
				//}
			}
			if(BluetoothAdapter.ACTION_DISCOVERY_FINISHED.equals(action)){
				File locationFileDir = Environment.getExternalStoragePublicDirectory("/cusp/health");
				locationFileDir.mkdirs();
				 String locfilename = locationFileDir.getPath() + File.separator + "Bid.txt";
				 File location = new File (locfilename);
				 if (!locationFileDir.exists() && locationFileDir.mkdirs()) {
						System.out.println("Cannot create File/ Folder");
					}
						try{
							System.out.println("Writing Device Information to file");
							out = new PrintWriter(location);
							for(int i = 0; i<BTArrayAdapter.getCount(); i++)
							out.println(BTArrayAdapter.getItem(i));
							out.flush();
						    out.close();
							}catch(Exception e){System.out.println("Error Flushing to a File " +e);}
				myBluetoothAdapter.startDiscovery();
				System.out.println("Scanning Again...");
				Toast.makeText(getApplicationContext(), "Scanning Again...", Toast.LENGTH_SHORT).show();
			}
		}
		
	};

	public void find(View v) {
		// TODO Auto-generated method stub
		if (myBluetoothAdapter.isDiscovering()){
			myBluetoothAdapter.cancelDiscovery();
		}else{
			BTArrayAdapter.clear();
			registerReceiver(bReceiver, new IntentFilter(BluetoothAdapter.ACTION_DISCOVERY_FINISHED));
			myBluetoothAdapter.startDiscovery();
			registerReceiver(bReceiver, new IntentFilter(BluetoothDevice.ACTION_FOUND));
		}
	}

	public void list(View v) {
		// TODO Auto-generated method stub
		pairedDevices = myBluetoothAdapter.getBondedDevices();
		
		for(BluetoothDevice device : pairedDevices)
			BTArrayAdapter.add(device.getName()+"\n"+device.getAddress());
		
		Toast.makeText(getApplicationContext(), "Paired Devices...", Toast.LENGTH_SHORT).show();
	}

	public void off(View v) {
		// TODO Auto-generated method stub
		myBluetoothAdapter.disable();
		text.setText("Status: Disconnected");
		Toast.makeText(getApplicationContext(), "Bluetooth is Off...", Toast.LENGTH_LONG).show();
	}

	public void on(View v) {
		// TODO Auto-generated method stub
		if (!myBluetoothAdapter.isEnabled()) {
			Intent turnOnIntent = new Intent (BluetoothAdapter.ACTION_REQUEST_ENABLE);
			startActivityForResult(turnOnIntent, REQUEST_ENABLE_BT);
			
			Toast.makeText(getApplicationContext(), "Bluetooth On...", Toast.LENGTH_LONG).show();
		}
		else{
			Toast.makeText(getApplicationContext(), "Bluetooth is already On...", Toast.LENGTH_LONG).show();
		}
	}
	
	protected void onDestroy(){
		super.onDestroy();
		unregisterReceiver(bReceiver);
	}


}
