package com.example.btnear_service;

import java.io.File;
import java.io.PrintWriter;

import android.app.IntentService;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Environment;
import android.widget.ArrayAdapter;
import android.widget.Toast;

public class Scan extends IntentService{

	//private static final String SCAN_ENABLE = "1";
	int scan_enable = 1;
 	BluetoothAdapter myBluetoothAdapter = null;
 	ArrayAdapter<String> BTArrayAdapter = null;
 	int REQUEST_ENABLE_BT = 1;
 	long starttime = System.currentTimeMillis();
	PrintWriter out = null;
	
	public Scan() {
		super("Scan");
		// TODO Auto-generated constructor stub
	}

	@Override
	protected void onHandleIntent(Intent intent) {
		// TODO Auto-generated method stub
		//int scan_enable = Integer.parseInt(intent.getStringExtra(SCAN_ENABLE));
		myBluetoothAdapter = BluetoothAdapter.getDefaultAdapter();
		myBluetoothAdapter.enable();
		System.out.println(System.currentTimeMillis());
		String date = new java.text.SimpleDateFormat("MM//dd//yyyy HH:mm:ss").format(new java.util.Date(System.currentTimeMillis()));
		System.out.println("Current Time: "+date);
		
			if(myBluetoothAdapter == null){
				Toast.makeText(getApplicationContext(), "Service: Bluetooth Radio not found", Toast.LENGTH_LONG).show();
				done();
			}
			else{
					Toast.makeText(getApplicationContext(), "Service: Starting Scan", Toast.LENGTH_SHORT).show();
					System.out.println("Service: Starting Scan at: "+System.currentTimeMillis()/1000);
					myBluetoothAdapter.startDiscovery();
					try{
						String action = intent.getAction();
						if(BluetoothDevice.ACTION_FOUND.equals(action)){
							BluetoothDevice device = intent.getParcelableExtra(BluetoothDevice.EXTRA_DEVICE);
							BTArrayAdapter.add(device.getName() + "\n" + device.getAddress());
							BTArrayAdapter.notifyDataSetChanged();
							System.out.println("Service: Total Devices in Array: "+BTArrayAdapter.getCount());
						}
					}catch(Exception e){
						System.out.println("Error: "+e);
					}
					
				
			}
	}

	private void done() {
		// TODO Auto-generated method stub
			File locationFileDir = Environment.getExternalStoragePublicDirectory("/cusp/health");
			 String locfilename = locationFileDir.getPath() + File.separator + "Bid.txt";
			 File location = new File (locfilename);
			 if (!locationFileDir.exists() && locationFileDir.mkdirs()) {
					System.out.println("Cannot create File/ Folder");
				}
					try{
						System.out.println("Service: Writing Device Information to file");
						out = new PrintWriter(location);
						for(int i = 0; i<=BTArrayAdapter.getCount(); i++)
						out.println(BTArrayAdapter.getItem(i));
						out.flush();
					    out.close();
						}catch(Exception e){System.out.println("Service: Error Flushing to a File " +e);}
	}

}
