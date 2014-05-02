package partthree;

import java.io.DataInputStream;
import java.io.IOException;
import java.net.Socket;
import java.security.GeneralSecurityException;
import java.security.InvalidKeyException;
import java.util.HashMap;
import java.util.Vector;

import constants.Constants;
/**
 * Class that handles receiving messages
 * @author  Cameron Cross 7193432
 *
 */
public class RecvMessage extends Thread {

	Socket socket;
	DataInputStream inFromServer;
	Boolean auth = true;
	HashMap<String,String> peers;
	Vector<String> invalid;
	StringEncryption se;
	/**
	 * 
	 * @param sock - The socket to receive from
	 */
	RecvMessage(Socket sock) {
		//get a list of peers from the peerlist class
		PeerList pl = PeerList.getInstance();
		pl.readFile();
		peers = pl.getPeers();
		//get all the peers that have tried to connect but are not allowed
		invalid = pl.getInvalidPeers();
		socket = sock;
		try {
			//get the ip address of the socket
			String ipaddr = socket.getInetAddress().getHostAddress();
			//check if it is invalid and has tried before
			if (!peers.containsKey(ipaddr)) {
				auth = false;
				//check if it is a first time invalid
				if (invalid.contains(ipaddr)) {
					pl.addInvalid(ipaddr);
					System.out.println("Unauthorized chat request from <IP " + ipaddr + ">");

				}
			}
			//create an input stream for the socket. output is unnecessary
			inFromServer = new DataInputStream(socket.getInputStream());
			//create the string encryption object
			se = new StringEncryption();
			//load the key
			se.loadKey(Constants.keyfile);
		} catch (IOException | GeneralSecurityException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	/**
	 * listens on the socket and receive the message
	 */
	@Override
	public void run() {
		int length = 0;
		String modifiedSentence;
		byte[] sendData = null;
		//check if it was an invalid host. Abort if it was.
		if (auth == false) {
			try {
				socket.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
			return;
		}
		//loop forever as we want to wait for everything
		try {
			while (true) {
				length = 0;
				while (length == 0) {
					//read an int, this will be the length of the next message
					length = inFromServer.readInt();
					//create the buffer for the message
					sendData = new byte[length];
					//read the message;
					length = inFromServer.read(sendData, 0, length);
				}
				if (length == -1) {
					//todo: put a nice message to indicate that the person has left
					return;
				}
				//decrypt the bytes into a string using the StringEncryption class
				modifiedSentence = se.decrypt(sendData);
				//get address
				String address = socket.getInetAddress().toString().substring(1);

				//print message

				System.out.println(peers.get(address) + " <" + address + ">: " + modifiedSentence);
			}
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (InvalidKeyException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
}
