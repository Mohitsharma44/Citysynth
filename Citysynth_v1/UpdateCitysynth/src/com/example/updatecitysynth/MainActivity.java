package com.example.updatecitysynth;

import java.io.File;
import java.io.IOException;

import com.zehon.FileTransferStatus;
import com.zehon.exception.FileTransferException;
import com.zehon.sftp.SFTP;
import com.zehon.sftp.SFTPClient;

import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.os.StrictMode;
import android.os.SystemClock;
import android.app.Activity;
import android.content.ComponentName;
import android.content.Intent;
import android.view.Menu;

public class MainActivity extends Activity {

	String sftpFromFolder = "/home/cusp/mohitsharma44/citysynth/";	
	File home = Environment.getExternalStorageDirectory();
	String cntrlserv_host = "shell.cusp.nyu.edu";
	String username = "mohitsharma44";
	String cntrlserv_privateKeyPath = "/mnt/sdcard/id_rsa2";
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		try {
			ssh(sftpFromFolder+"configfiles/", home+"/cusp/files/", "Citysynth1.apk");
		} catch (FileTransferException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
public void ssh(String from, String to, String name) throws FileTransferException{
		
		SFTPClient sftpClient = new SFTPClient(cntrlserv_host, username, cntrlserv_privateKeyPath, true );
		try {
			System.out.println("Downloading new version");
			StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
			StrictMode.setThreadPolicy(policy);
			int status = sftpClient.getFile(name, from, to);
			sftpClient.moveFile(name, from, name+"_read.txt", from); 
			if(FileTransferStatus.SUCCESS == status){
				System.out.println(name + " got downloaded successfully to  folder "+to+"\n Renamed the source file");
			}
			else if(FileTransferStatus.FAILURE == status){
				System.out.println("Fail to download  to  folder "+to);
			}
		} catch (FileTransferException e) {
			e.printStackTrace();
			}
		uninstall();
	}

	public void uninstall(){
		try{

    	System.out.println("Uninstalling..");
    	Runtime.getRuntime().exec(new String[] {"su", "-c", "pm uninstall com.example.citysynth"});
		}catch(Exception e){
			System.out.println("Error removing application: "+e);
		}
		SystemClock.sleep(2000);
		updateApk1();
	}
	
	public void updateApk1(){
		System.out.println("Updating the application wait..");
        try {
			Runtime.getRuntime().exec(new String[] {"su", "-c", "pm install /mnt/sdcard/cusp/files/Citysynth1.apk"});
		} catch (IOException e) {
			// TODO Auto-generated catch block
			System.out.println("UpdateApk1 failed: "+e);
		}
        reboot();
	}
	public void updateApk()
    {
            try
            {
            	System.out.println("Installing the application wait..");
                //Runtime.getRuntime().exec(new String[] {"su", "-c", "pm install /mnt/sdcard/cusp/files/Citysynth1.apk"});
            	//Intent intent = new Intent(Intent.ACTION_VIEW);
                //Uri uri = Uri.fromFile(new File(home+"/cusp/files/Citysynth1.apk"));
                //intent.setDataAndType(uri, "application/vnd.android.package-archive");
                //startActivity(intent);
            	System.out.println("Application Updated!");
            }
            catch (Exception e)
            {
                System.out.println(e.toString());
                System.out.println("no root" +e);
            }
            reboot();
    }
	
	 public void reboot(){
	    	try {
	    		File file = new File(home+"/cusp/files/Citysynth1.apk");
	    		
	    		System.out.println("Going for a quick reboot..");
		        SystemClock.sleep(4000);
		        file.delete();
		        final Intent intent = new Intent();
		        final Intent intentb = new Intent();
				ComponentName cName = new ComponentName
				("com.example.citysynth","com.example.citysynth.Constants");
				intent.setComponent(cName);        
				startActivity(intent);
				onBackPressed();
				ComponentName bName = new ComponentName
				("com.example.citysynth","com.example.cistysynth.MainActivity");
				intent.setComponent(bName);        
				//startActivity(intentb);
				//SystemClock.sleep(1000);
				//Runtime.getRuntime().exec(new String[]{"/system/bin/su","-c","reboot now"});
	    	}catch (Exception ex) {
	            System.out.println("Cant Reboot:" +ex);
	        }
	    }
	 
	    public void onBackPressed() {
	        super.onBackPressed();   
	        //    finish();

	    }
}
