import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.util.Map;

import org.yaml.snakeyaml.Yaml;


public class First {
	public static void main(String args[]) throws IOException{
	     final Yaml yaml = new Yaml();
	     InputStream reader = null, reader1 = null;
	      Object focus = null;
		Object Trial = null; Object zoom = null;
	      try {
	          reader = new FileInputStream("/home/mohit/Desktop/sample.yml");
	          //reader1 = new FileInputStream("/home/mohit/Desktop/sample.yml");
	          //Map<String,Object> result = (Map<String,Object>)yaml.load(reader);
	          Map<String,Object> trial = (Map<String,Object>)yaml.load(reader);
	          focus = (trial.get("focus"));
	          Trial = (trial.get("trial"));
	          zoom = (trial.get("zoom"));
	          System.out.println("Focus: "+(focus.toString()).substring(1,(focus.toString().length()-1))+
	        		  "\nZoom: "+(zoom.toString()).substring(1,(zoom.toString().length()-1))+
	        		  "\nTrial: "+(Trial.toString()).substring(1,(Trial.toString().length()-1)));
	          
	      } catch (final FileNotFoundException fnfe) {
	          System.err.println("We had a problem reading the YAML from the file because we couldn't find the file." + fnfe);
	      } finally {
	          if (null != reader && null != reader1) {
	              try {
	                  reader.close();
	                  reader1.close();
	              } catch (final IOException ioe) {
	                  System.err.println("We got the following exception trying to clean up the reader: " + ioe);
	              }
	          }
	      }
	       
	  }
}
