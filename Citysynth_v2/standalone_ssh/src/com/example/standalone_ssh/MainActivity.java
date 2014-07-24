package com.example.standalone_ssh;

import android.app.Activity;
import android.os.Bundle;
import com.jcraft.jsch.Channel;
import com.jcraft.jsch.ChannelSftp;
import com.jcraft.jsch.JSch;
import com.jcraft.jsch.Session;


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
		try {
            JSch jsch = new JSch();

            String user = "mohitsharma44";
            String host = "shell.cusp.nyu.edu";
            int port = 22;
            String privateKey = ".ssh/id_rsa";

            jsch.addIdentity(privateKey);
            System.out.println("identity added ");

            Session session = jsch.getSession(user, host, port);
            System.out.println("session created.");

            java.util.Properties config = new java.util.Properties();
            config.put("StrictHostKeyChecking", "no");
            session.setConfig(config);

            session.connect();
            System.out.println("session connected.....");

            Channel channel = session.openChannel("sftp");
            channel.setInputStream(System.in);
            channel.setOutputStream(System.out);
            channel.connect();
            System.out.println("shell channel connected....");

            ChannelSftp c = (ChannelSftp) channel;

            String fileName = "test.txt";
            c.put(fileName, "./in/");
            c.exit();
            System.out.println("done");

        } catch (Exception e) {
            System.err.println(e);
        }
	}
	
}
