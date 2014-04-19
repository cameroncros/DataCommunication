package parttwo;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

class TCPServer extends Thread {

	ServerSocket welcomeSocket = null;

	TCPServer () {
		try {
			welcomeSocket = new ServerSocket(4010);

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
				Thread rv = new SendFile(connectionSocket);
				rv.start();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}

		}

	}
	public static void main(String argv[])
	{
		TCPServer ts = new TCPServer();
		ts.start();
	}
}