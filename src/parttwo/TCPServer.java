package parttwo;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

class TCPServer extends Thread {

	ServerSocket welcomeSocket = null;

	TCPServer (String port) {
		try {
			//convert the port to an int, this is allowed to fail as it is assumed that the user will actually put in a correct port
			int pt = new Integer(port);
			//create a socket on the port
			welcomeSocket = new ServerSocket(pt);

		} 
		catch (Exception e) {
			System.out.println(e.getLocalizedMessage());
			System.exit(e.hashCode());
		}
	}

	void finalise() {
		try {
			//close the socket
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
				//wait for a connection, and then accept it.
				connectionSocket = welcomeSocket.accept();
				//pass off the new socket to a SendFile instance that will handle the file transfer
				Thread rv = new SendFile(connectionSocket);
				//start the SendFile instance and go back to listening
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
		//check if the port has been passed in
		if (argv.length == 1) {
			//create the file server instance
			TCPServer ts = new TCPServer(argv[0]);
			ts.start();
		} else {
			System.out.println("Requires a port as the arguement");
		}
	}
}
