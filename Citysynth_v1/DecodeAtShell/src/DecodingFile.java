
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;

import javax.crypto.Cipher;
import javax.crypto.CipherInputStream;
import javax.crypto.NoSuchPaddingException;
import javax.crypto.spec.SecretKeySpec;


public class DecodingFile {

	/**
	 * @param args
	 */
	public static String[] myFiles;
	public static void main(String[] args) {
		// TODO Auto-generated method stub
		try {	
			File file = new File("/home/mohit/Pictures/samsungdemo");
			if(file.isDirectory()){  
				myFiles = file.list();
				System.out.println(myFiles.length);
				for (int i=0; i<myFiles.length; i++) {  
					System.out.println("Opening: "+myFiles[i]);
					File myFile = new File(file, myFiles[i]);   
					decrypt(myFile, i);  
					}  
				}
			}catch(Exception e){
				System.out.println("Error Decrypting Images..." +e);
			}
	}
	static void decrypt(File myFile, int i) throws IOException, NoSuchAlgorithmException, NoSuchPaddingException, InvalidKeyException {
	    FileInputStream fis = new FileInputStream(myFile);
		File decfile = new File("/home/mohit/Pictures/dec/"+myFiles[i]);
	    FileOutputStream fos = new FileOutputStream(decfile);
	    SecretKeySpec sks = new SecretKeySpec("centerforurbanscienceandprogress".getBytes(), "AES");
	    System.out.println("Secret Key Spec: "+sks);
	    Cipher cipher = Cipher.getInstance("AES");
	    cipher.init(Cipher.DECRYPT_MODE, sks);
	    CipherInputStream cis = new CipherInputStream(fis, cipher);
	    int b;
	    byte[] d = new byte[8];
	    while((b = cis.read(d)) != -1) {
	        fos.write(d, 0, b);
	    }
	    fos.flush();
	    fos.close();
	    cis.close();
	    System.out.println("Decoded: "+myFiles[i]+" Successfully!");
	    
	}

}
