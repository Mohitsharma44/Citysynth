
package com.skyrideraj.camerademo;

import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

import javax.crypto.Cipher;
import javax.crypto.CipherOutputStream;
import javax.crypto.spec.SecretKeySpec;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.hardware.Camera;
import android.hardware.Camera.CameraInfo;
import android.hardware.Camera.Parameters;
import android.hardware.Camera.PictureCallback;
import android.hardware.Camera.ShutterCallback;
import android.os.Bundle;
import android.os.Environment;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

@SuppressLint("NewApi")
public class CameraActivity extends Activity implements SurfaceHolder.Callback{

	PictureCallback mCall;
	PictureCallback rawCallback;
	ShutterCallback shutterCallback;
	private SurfaceHolder sHolder;
	// a variable to control the camera
	private Camera mCamera = null;
	// the camera parameters
	private Parameters parameters;
	//surface view
	private SurfaceView sv;
	//boolean to enable safe clicks 
	private boolean safeToTakePicture = false;
	//button for camera clicking
	private Button b;
	
	public static String key="centerforurbanscienceandprogress";
	
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_camera);
		
				safeToTakePicture = true;
				Log.e("safe!", "safe!");
				
		
		sv = (SurfaceView) findViewById(R.id.surfaceView1);
		b= (Button) findViewById(R.id.button1);
		b.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				//started preview to make camera ready for taking pictures
				mCamera.startPreview();
				if(safeToTakePicture) {
					//this is the main function that takes pictures and callbacks onPictureTaken()
					mCamera.takePicture(shutterCallback, rawCallback,
							mCall);
					safeToTakePicture = false;
					Log.e("not Safe!", "not Safe!");
				}
			}
		});
		// Get a surface
		sHolder = sv.getHolder();
		// add the callback interface methods defined below as the SurfaceView callbacks
		sHolder.addCallback(this);
		// tells Android that this surface will have its data constant replaced
		sHolder.setType(SurfaceHolder.SURFACE_TYPE_PUSH_BUFFERS); //This is required for some phones running low android versions

		rawCallback = new PictureCallback() {
			public void onPictureTaken(byte[] data, Camera camera) {
				//Log.d("Log", "onPictureTaken - raw");
				//A dummy callback (dont do anything here)

			}
		};

		/** Handles data for jpeg picture */
		shutterCallback = new ShutterCallback() {
			public void onShutter() {
				//Log.i("Log", "onShutter'd");
				//A dummy callback (dont do anything here)

			}

		};

		mCall = new PictureCallback() {
			public void onPictureTaken(byte[] data, Camera camera) { 
				//this callback needs to be handled
				// decode the data obtained by the camera variable into a Bitmap and do whatever you want with the bitmap!
				Log.e("Picture callback!", "Picture callback!");
				System.out.println("On Picture Taken method");
				
				String sdDir = Environment.getExternalStorageDirectory().toString();
				//File pictureFileDir = sdDir();

				SimpleDateFormat dateFormat = new SimpleDateFormat("yyyyMMdd__HHmmss", Locale.US);
				String date = dateFormat.format(new Date());
				String photoFile = "Picture_" + date + ".jpg";

				String filename = sdDir + File.separator + photoFile;

				File pictureFile = new File(filename);

				try{
				    // Here you read the cleartext.
					ByteArrayInputStream bis = new ByteArrayInputStream(data);
				    //FileInputStream fis = new FileInputStream("/mnt/sdcard/Pictures/CUSP/Realtime/Picture_20140412__110342.jpg");
				    // This stream write the encrypted text. This stream will be wrapped by another stream.
				    FileOutputStream fos = new FileOutputStream(pictureFile);

				    // Length is 16 byte
				    //System.out.println("key: "+key);
				    SecretKeySpec sks = new SecretKeySpec("centerforurbanscienceandprogress".getBytes(), "AES");
				    System.out.println(sks);
				    // Create cipher
				    Cipher cipher = Cipher.getInstance("AES");
				    cipher.init(Cipher.ENCRYPT_MODE, sks);
				    // Wrap the output stream
				    CipherOutputStream cos = new CipherOutputStream(fos, cipher);
				    // Write bytes
				    int b;
				    byte[] d = new byte[8];
				    System.out.println("BIS: "+bis);
				    while((b = bis.read(d)) != -1) {
				        cos.write(d, 0, b);
				    }
				    // Flush and close streams.
				    cos.flush();
				    cos.close();
				    bis.close();
				    System.out.println("done");
				}catch(Exception e){
					System.out.println("Encryption Failed due to: "+e);
				}
				
				//after image has been handles stop preview and start it again just before takePicture() function
				mCamera.stopPreview();
				Log.e("safe","safe");
				
			}
		};

	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.camera, menu);
		return true;
	}

	

	@Override
	public void surfaceCreated(SurfaceHolder holder) {
		// TODO Auto-generated method stub
		Log.v("surface created", "surface created");

		Camera.CameraInfo cameraInfo = new Camera.CameraInfo();
		Log.d("No of cameras", Camera.getNumberOfCameras() + "");
		//Number of cameras and then getting back face camera(back camera has different assigned number on every mobile)
		for (int camNo = 0; camNo < Camera.getNumberOfCameras(); camNo++) {
			CameraInfo camInfo = new CameraInfo();
			Camera.getCameraInfo(camNo, camInfo);
			if (camInfo.facing == (Camera.CameraInfo.CAMERA_FACING_BACK)) {
				
				//Acquiring camera for our application!
				Log.v("acquried camera", "acquried camera");
				mCamera = Camera.open(camNo);
				mCamera.enableShutterSound(false);
			}

		}

		try {
			mCamera.setPreviewDisplay(holder);

		} catch (IOException exception) {
			mCamera.release();
			mCamera = null;
			//Any error handling
		}
		
	}

	@Override
	public void surfaceChanged(SurfaceHolder holder, int format, int width,
			int height) {
		// TODO Auto-generated method stub
		Log.v("surface changed", "surface changed");
		parameters = mCamera.getParameters();
		// set any camera parameters you want here
		mCamera.setParameters(parameters);
		
		
	}

	@Override
	public void surfaceDestroyed(SurfaceHolder holder) {
		// TODO Auto-generated method stub
		Log.v("surface destroyed", "sur destroyed");
		if (mCamera != null) {
			mCamera.stopPreview();
			// release the camera
			Log.v("released camera", "released camera");
			mCamera.release();
			// unbind the camera from this object
			mCamera = null;
		}
	}
}
