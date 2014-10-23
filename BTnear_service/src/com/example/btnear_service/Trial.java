package com.example.btnear_service;

import java.io.File;
import java.io.PrintWriter;

import android.annotation.SuppressLint;
import android.app.IntentService;
import android.app.Service;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothAdapter.LeScanCallback;
import android.bluetooth.BluetoothDevice;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Environment;
import android.os.IBinder;
import android.os.SystemClock;
import android.widget.ArrayAdapter;

public class Trial extends Service{
	private ArrayAdapter<String> BTArrayAdapter;
	private BluetoothAdapter myBluetoothAdapter;
	PrintWriter out = null;
	private boolean discover = false;
	String name;
	String address;
	
	
	public BroadcastReceiver bReceiver = new BroadcastReceiver(){

		@Override
		public void onReceive(Context context, Intent intent) {
			// TODO Auto-generated method stub
			System.out.println("Called onReceive");
			String action = intent.getAction();
			//When a new device is found
			try{
			if(action.equals(BluetoothDevice.ACTION_FOUND)) {
				System.out.println("Called ACTION_FOUND");
				BluetoothDevice device = intent.getParcelableExtra(BluetoothDevice.EXTRA_DEVICE);
				System.out.println("Device Name: "+device.getName());
				System.out.println("Device Addr: "+device.getAddress());
				name = device.getName();
				address = device.getAddress();
				//BTArrayAdapter.add(device.getName() + "\n" + device.getAddress());
				//BTArrayAdapter.notifyDataSetChanged();
				//System.out.println(BTArrayAdapter.getCount());
				//for (int i=1;i<=BTArrayAdapter.getCount();i++){
				//System.out.println("Contents of Array: "+BTArrayAdapter.getItem(0));
				//}
			}
			}catch(Exception e){System.out.println("Exception in ACTION_FOUND: "+e);
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
							out.println("Name: "+name);
							out.println("Address: "+address);
							out.flush();
						    out.close();
						    System.out.println("Wrote to the file..!");
							}catch(Exception e){System.out.println("Error Flushing to a File " +e);}
				//myBluetoothAdapter.startDiscovery();
				//System.out.println("Scanning Again...");
				//Toast.makeText(getApplicationContext(), "Scanning Again...", Toast.LENGTH_SHORT).show();
			}
		}
		
	};


	public void onCreate() {
		// TODO Auto-generated method stub
		System.out.println("This service is called!");
		myBluetoothAdapter= BluetoothAdapter.getDefaultAdapter();
		myBluetoothAdapter.enable();
		SystemClock.sleep(5000);
		if (myBluetoothAdapter.isEnabled()){
			System.out.println("BT is enabled...");
			discover = myBluetoothAdapter.startDiscovery();
		}
		System.out.println(myBluetoothAdapter.getScanMode());
		
		System.out.println("Discovering: "+myBluetoothAdapter.isDiscovering());
		//registerReceiver(bReceiver, new IntentFilter(BluetoothDevice.ACTION_FOUND));
		IntentFilter filter1 = new IntentFilter(BluetoothDevice.ACTION_FOUND);
		IntentFilter filter2 = new IntentFilter(BluetoothAdapter.ACTION_DISCOVERY_FINISHED);
		this.registerReceiver(bReceiver, filter1);
		this.registerReceiver(bReceiver, filter2);
		//registerReceiver(bReceiver, new IntentFilter(BluetoothAdapter.ACTION_DISCOVERY_FINISHED));
	}
	public void onDestroy(){
		super.onDestroy();
		try{
			if(bReceiver != null){
				unregisterReceiver(bReceiver);
			}
		
		}catch(Exception e){System.out.println("Exception Caught..: "+e);}
	}
	@Override
	public IBinder onBind(Intent intent) {
		// TODO Auto-generated method stub
		return null;
	}

}
