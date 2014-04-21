package parttwo;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

class TCPServer extends Thread {

	ServerSocket welcomeSocket = null;

	TCPServer (String port) {
		try {
			int pt = new Integer(port);
			welcomeSocket = new ServerSocket(pt);

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

		Socket connectionSocket;
		try {
			while(true)
			{
				connectionSocket = welcomeSocket.accept();
				Thread rv = new SendFile(connectionSocket);
				rv.start();
			}
		}
		catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	public static void main(String argv[])
	{
		if (argv.length == 1) {
			TCPServer ts = new TCPServer(argv[0]);
			ts.start();
		}
	}
}
