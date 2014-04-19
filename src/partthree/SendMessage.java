package partthree;

import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;

public class SendMessage {
	Socket clientSocket = null;
	DataOutputStream outToServer = null;
	Boolean me = false;
	
	SendMessage(String address) {
		try {
			clientSocket = new Socket(address, 4010);
			outToServer = new DataOutputStream(clientSocket.getOutputStream());
		} catch (IOException e) {
			e.printStackTrace();
		}
		String local = clientSocket.getLocalAddress().getHostAddress();
		String other = clientSocket.getInetAddress().getHostAddress();
		if (local.compareTo(other) == 0) {
			me = true;
		}
	}
	
	void send(String message) {
		byte[] utf8Bytes;
		if (me) {
			return;
		}
		try {
			utf8Bytes = message.getBytes("UTF-8");
			outToServer.writeInt(utf8Bytes.length);
			outToServer.write(utf8Bytes, 0, utf8Bytes.length);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
}
