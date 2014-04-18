package partthree;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.Socket;

public class RecvMessage extends Thread {
	
	Socket socket;
	BufferedReader inFromServer;
	
	RecvMessage(Socket sock) {
		
		socket = sock;
		try {
			inFromServer = new BufferedReader(new InputStreamReader(socket.getInputStream()));
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	@Override
	public void run() {
		String modifiedSentence;
		while (true) {
			try {
				modifiedSentence = inFromServer.readLine();
				System.out.println("<" + socket.getInetAddress() + ">: " + modifiedSentence);	
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
					
		}		
	}
}
