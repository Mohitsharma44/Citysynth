package com.example.standalone_ssh;

import android.app.Activity;
import android.os.Bundle;

public class MainActivity extends Activity{

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		try{
			System.out.println("Executed Successfully!");
		}catch (Exception e)
		{
			System.out.println("Exception caught: "+e);
		}
	}
	
}
