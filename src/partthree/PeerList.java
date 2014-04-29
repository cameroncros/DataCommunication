package partthree;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.Vector;

import constants.Constants;

public class PeerList {
	private static Vector<String> peers = new Vector<String>();
	private static Vector<String> invalid = new Vector<String>();
	private static PeerList instance = null;
	
	private PeerList () {
		readFile();
	}
	
	static PeerList getInstance() {
		if (instance == null) {
			instance = new PeerList();
		}
		return instance;
	}

	void readFile() {
		try {
			BufferedReader br = new BufferedReader(new FileReader(Constants.peerfile));
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

	public Vector<String> getInvalidPeers() {
		return invalid;
	}

	public void addInvalid(String ipaddr) {
		invalid.add(ipaddr);

	}
}
