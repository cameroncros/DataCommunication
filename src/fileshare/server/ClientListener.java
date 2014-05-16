package fileshare.server;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
/**
 * This is the file server class. It listens for the connections and passes new connections off to send fiule
 * @author  Cameron Cross 7193432
 * @since 29 April 2014
 */
class ClientListener {

	ServerSocket welcomeSocket = null;
	/**
	 * This function sets up the TCPServer class
	 * @param port - String containing the port
	 */
	ClientListener (int port) {
		try {
			//create a socket on the port
			welcomeSocket = new ServerSocket(port);
			run();
		} 
		catch (Exception e) {
			System.out.println(e.getLocalizedMessage());
			System.exit(e.hashCode());
		}
	}
	/**
	 * This function is supposed to close the socket when the class is cleaned up by the Garbage collector. I dunno if it is necessary
	 */
	void finalise() {
		try {
			//close the socket
			welcomeSocket.close();
		}
		catch (Exception f) {
			System.out.println(f.getLocalizedMessage());
		}
	}
	/**
	 * This function listens for an incoming connection and passes it off to SendFile
	 */
	public void run() {
		System.out.println("Listening");

		Socket connectionSocket;
		try {
			while(true)
			{
				//wait for a connection, and then accept it.
				connectionSocket = welcomeSocket.accept();
				//pass off the new socket to a SendFile instance that will handle the file transfer
				Thread rv = new HandleClient(connectionSocket);
				//start the SendFile instance and go back to listening
				rv.start();

			}
		}
		catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
}
