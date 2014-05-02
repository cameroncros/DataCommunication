package partthree;

import java.io.DataOutputStream;
import java.io.IOException;
import java.net.NoRouteToHostException;
import java.net.Socket;
import java.security.GeneralSecurityException;
import java.security.InvalidKeyException;

import constants.Constants;
/**
 * Handles sending the message to a host
 * @author  Cameron Cross 7193432
 *
 */
public class SendMessage {
	Socket clientSocket = null;
	DataOutputStream outToServer = null;
	Boolean me = false;
	StringEncryption se;
	Boolean connected = true;
	String addr = "";
	/**
	 * Sets up the socket and checks if it is correct
	 * @param address - The address to send the message to
	 */
	SendMessage(String address) {
		addr = address;
		try {
			//create a string encryption object and load the key
			se = new StringEncryption();
			se.loadKey(Constants.keyfile);

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

		} catch (NoRouteToHostException e) {
			connected = false;
		} catch (IOException | GeneralSecurityException e) {
			connected = false;
			e.printStackTrace();
		}
	}
	/**
	 * sends the message to the socket
	 * @param message - The message to be sent
	 */
	void send(String message) {
		byte[] utf8Bytes;
		//check if we are talking to ourselves and exit if we are
		if (me) {
			return;
		}
		if (connected == false) {
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
	/**
	 * Check if the socket is connected correctly and nothing has gone wrong
	 * @return connected - Check to see if the class is connected
	 */
	Boolean isConnected() {
		return connected;
	}
}
