package parttwo;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
/**
 * This is the file server class. It listens for the connections and passes new connections off to send fiule
 * @author  Cameron Cross 7193432
 *
 */
class TCPServer extends Thread {

	ServerSocket welcomeSocket = null;
	/**
	 * This function sets up the TCPServer class
	 * @param port - String containing the port
	 */
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
/**
 * main function, checks arguements and passes it off to the TCPServer class
 * @param argv - Array of strings containing 
 */
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
