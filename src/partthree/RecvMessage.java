package partthree;

import java.io.DataInputStream;
import java.io.IOException;
import java.net.Socket;
import java.security.GeneralSecurityException;
import java.security.InvalidKeyException;
import java.util.Vector;

import constants.Constants;

public class RecvMessage extends Thread {
	
	Socket socket;
	DataInputStream inFromServer;
	Boolean auth = true;
	Vector<String> peers;
	
	StringEncryption se;
	
	RecvMessage(Socket sock) {
		peers = (new PeerList()).getPeers();
		socket = sock;
		try {
			String ipaddr = socket.getInetAddress().getHostAddress();
			if (!peers.contains(ipaddr)) {
				System.out.println("Connection from an invalid host: " + ipaddr);
				auth = false;
			}
			
			inFromServer = new DataInputStream(socket.getInputStream());
			se = new StringEncryption();
			se.loadKey(Constants.keyfile);
		} catch (IOException | GeneralSecurityException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	@Override
	public void run() {
		int length = 0;
		String modifiedSentence;
		byte[] sendData = null;
		if (auth == false) {
			return;
		}
		while (true) {
			try {
				length = 0;
				while (length == 0) {
				length = inFromServer.readInt();
				sendData = new byte[length];
				length = inFromServer.read(sendData, 0, length);
				}
				if (length == -1) {
					//todo: put a nice message to indicate that the person has left
					return;
				}
				modifiedSentence = se.decrypt(sendData);
				System.out.println("<" + socket.getInetAddress() + ">: " + modifiedSentence);
				
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (InvalidKeyException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
					
		}		
	}
}
