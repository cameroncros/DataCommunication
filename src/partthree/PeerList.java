package partthree;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.Vector;

public class PeerList {
	private Vector<String> peers = new Vector<String>();
	PeerList () {
		try {
		BufferedReader br = new BufferedReader(new FileReader("Peers.txt"));
		String line;
		while ((line = br.readLine()) != null) {
		   peers.add(line);
		}
		br.close();
		}
		catch (FileNotFoundException e) {
			
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
	}
	
	Vector<String> getPeers() {
		return peers;
	}
	
	void printPeers() {
		
	}
}
