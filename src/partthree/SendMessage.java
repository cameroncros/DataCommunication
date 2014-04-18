package partthree;

import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;

public class SendMessage {
	Socket clientSocket = null;
	DataOutputStream outToServer = null;
	SendMessage(String address) {
		try {
			clientSocket = new Socket(address, 4010);
			outToServer = new DataOutputStream(clientSocket.getOutputStream());
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	void send(String message) {
		try {
			outToServer.writeBytes(message);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
}
