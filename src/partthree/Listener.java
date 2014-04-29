package partthree;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

import constants.Constants;

public class Listener extends Thread {
	ServerSocket welcomeSocket = null;
	
	Listener () {
		try {
			//opens a socket and listens on it
			welcomeSocket = new ServerSocket(Constants.port);
		} 
		catch (Exception e) {
			System.out.println(e.getLocalizedMessage());
			System.exit(e.hashCode());
		}
	}
	
	void finalise() {
		try {
			//closes the socket
			welcomeSocket.close();
		}
		catch (Exception f) {
			System.out.println(f.getLocalizedMessage());
		}
	}

	@Override
	public void run() {
		System.out.println("Listening");
		while(true)
		{
			Socket connectionSocket;
			try {
				//waits for a connection and creates a new socket for it
				connectionSocket = welcomeSocket.accept();
				//pass the socket to a new class to handle listening on this socket
				Thread rv = new RecvMessage(connectionSocket);
				//start the thread
				rv.start();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}

		}
		
	}
	
	
	
	
}
