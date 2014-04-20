package partthree;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

import constants.Constants;

public class Listener extends Thread {
	ServerSocket welcomeSocket = null;
	
	Listener () {
		
		
		try {
		welcomeSocket = new ServerSocket( Constants.port);
		
		} 
		catch (Exception e) {
			System.out.println(e.getLocalizedMessage());
			System.exit(e.hashCode());
		}
	}
	
	void finalise() {
		try {
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
				connectionSocket = welcomeSocket.accept();
				Thread rv = new RecvMessage(connectionSocket);
				rv.start();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}

		}
		
	}
	
	
	
	
}
