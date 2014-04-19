package partthree;

import java.io.DataInputStream;
import java.io.IOException;
import java.net.Socket;

public class RecvMessage extends Thread {
	
	Socket socket;
	DataInputStream inFromServer;
	
	RecvMessage(Socket sock) {
		
		socket = sock;
		try {
			 inFromServer = new DataInputStream(socket.getInputStream());
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	@Override
	public void run() {
		int length = 0;
		String modifiedSentence;
		byte[] sendData = new byte[1024];
		while (true) {
			try {
				length = 0;
				while (length == 0) {
				length = inFromServer.readInt();
				length = inFromServer.read(sendData, 0, length);
				}
				if (length == -1) {
					//todo: put a nice message to indicate that the person has left
					return;
				}
				modifiedSentence = new String(sendData, 0, length, "UTF-8");
				System.out.println("<" + socket.getInetAddress() + ">: " + modifiedSentence);
				
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
					
		}		
	}
}
