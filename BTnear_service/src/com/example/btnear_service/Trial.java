package com.example.btnear_service;

import java.io.File;
import java.io.PrintWriter;

import android.app.IntentService;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Environment;
import android.widget.ArrayAdapter;
import android.widget.Toast;

public class Trial extends IntentService{
	ArrayAdapter<String> BTArrayAdapter = null;
	BluetoothAdapter myBluetoothAdapter = BluetoothAdapter.getDefaultAdapter();
	PrintWriter out = null;
	public Trial() {
		super("Trial");
		// TODO Auto-generated constructor stub
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
				System.out.println("Scanning Done...");
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
				//myBluetoothAdapter.startDiscovery();
				//System.out.println("Scanning Again...");
				//Toast.makeText(getApplicationContext(), "Scanning Again...", Toast.LENGTH_SHORT).show();
			}
		}
		
	};


	@Override
	protected void onHandleIntent(Intent intent) {
		// TODO Auto-generated method stub
		System.out.println("This service is called!");
		String action = intent.getAction();
		myBluetoothAdapter.enable();
		myBluetoothAdapter.startDiscovery();
		if(myBluetoothAdapter.isDiscovering()){
			System.out.println("Discovering...");
		registerReceiver(bReceiver, new IntentFilter(BluetoothDevice.ACTION_FOUND));
		}
		else{
			System.out.println("Finished Discovering...");
			registerReceiver(bReceiver, new IntentFilter(BluetoothAdapter.ACTION_DISCOVERY_FINISHED));
		}
	}
	public void onDestroy(){
		super.onDestroy();
		unregisterReceiver(bReceiver);
	}

}
