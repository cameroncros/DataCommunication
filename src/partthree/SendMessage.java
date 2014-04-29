package partthree;

import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;
import java.security.GeneralSecurityException;
import java.security.InvalidKeyException;

import constants.Constants;

public class SendMessage {
	Socket clientSocket = null;
	DataOutputStream outToServer = null;
	Boolean me = false;
	StringEncryption se;
	Boolean connected = true;
	
	SendMessage(String address) {
		try {
			//create socket to address and port given
			clientSocket = new Socket(address, Constants.port);
			//create output stream for socket
			outToServer = new DataOutputStream(clientSocket.getOutputStream());

			//get the address of the current pc
			String local = clientSocket.getLocalAddress().getHostAddress();
			//get the address of the other end
			String other = clientSocket.getInetAddress().getHostAddress();
			//check if we are speaking to ourself?
			if (local.compareTo(other) == 0) {
				me = true;
			}
			//create a string encryption object and load the key
			se = new StringEncryption();
			se.loadKey(Constants.keyfile);
		} catch (IOException | GeneralSecurityException e) {
			connected = false;
			e.printStackTrace();
		}
	}
	
	void send(String message) {
		byte[] utf8Bytes;
		//check if we are talking to ourselves and exit if we are
		if (me) {
			return;
		}
		try {
			//encrypt the message into a string of bytes
			utf8Bytes = se.encrypt(message);
			//write the length to the socket
			outToServer.writeInt(utf8Bytes.length);
			//write the encrypted bytes to the socket'
			outToServer.write(utf8Bytes, 0, utf8Bytes.length);
		} catch (IOException |InvalidKeyException e) {
			connected = false;
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	Boolean isConnected() {
		return connected;
	}
}
