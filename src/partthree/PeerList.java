package partthree;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.Vector;

import constants.Constants;
/**
 * Singleton containing the peers and invalid peers - Singleton design is not the best, but seems better than passing it down to everything
 * @author  Cameron Cross 7193432
 *
 */
public class PeerList {
	private static Vector<String> peers = new Vector<String>();
	private static Vector<String> invalid = new Vector<String>();
	private static PeerList instance = null;
	/**
	 * Private constructor that reads the file
	 */
	private PeerList () {
		readFile();
	}
	/**
	 * Singleton getInstance
	 * @return instance - and instance of the PeerList class
	 */
	static PeerList getInstance() {
		if (instance == null) {
			instance = new PeerList();
		}
		return instance;
	}
/**
 * reads the peerfile
 */
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
/**
 * Returns the valid peers
 * @return peers - Vector containing valid peers
 */
	Vector<String> getPeers() {
		return peers;
	}

	void printPeers() {
		//TODO: fill this in
	}
	
	/**
	 * Returns the invalid peers
	 * @return invalid - Vector containing invalid peers
	 */
	public Vector<String> getInvalidPeers() {
		return invalid;
	}

	/**
	 * Adds a new invalid peer
	 * @param ipaddr - String containing the ip address of the peer
	 */
	public void addInvalid(String ipaddr) {
		invalid.add(ipaddr);

	}
}
