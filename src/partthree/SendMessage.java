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
	
	SendMessage(String address) {
		try {
			clientSocket = new Socket(address, Constants.port);
			outToServer = new DataOutputStream(clientSocket.getOutputStream());

			String local = clientSocket.getLocalAddress().getHostAddress();
			String other = clientSocket.getInetAddress().getHostAddress();
			if (local.compareTo(other) == 0) {
				me = true;
			}
			se = new StringEncryption();
			se.loadKey(Constants.keyfile);
		} catch (IOException | GeneralSecurityException e) {
			e.printStackTrace();
		}
	}

	void send(String message) {
		byte[] utf8Bytes;
		if (me) {
			return;
		}
		try {
			utf8Bytes = se.encrypt(message);
			outToServer.writeInt(utf8Bytes.length);
			outToServer.write(utf8Bytes, 0, utf8Bytes.length);
		} catch (IOException |InvalidKeyException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
}
