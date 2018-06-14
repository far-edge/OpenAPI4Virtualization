package ch.supsi.isteps.fileserverspring.spike;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URL;


public class SpikeFileClientDownload {

	public static void main(String[] args) throws Exception {
		
		URL website = new URL("http://172.20.0.2:8080/download");
		try (InputStream in = website.openStream()) {
			String readLine;
			BufferedReader br = new BufferedReader(new InputStreamReader(in));
			 
			while (((readLine = br.readLine()) != null)) {
			System.out.println(readLine);
			}
		}
	}

}