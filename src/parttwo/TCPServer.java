package parttwo;

<<<<<<< HEAD
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.net.ServerSocket;
import java.net.Socket;

import constants.Constants;

class TCPServer {
	public static void main(String argv[])
	{
		ServerSocket welcomeSocket = null;
		try {
		String clientSentence;
		String capitalizedSentence;
		welcomeSocket = new ServerSocket( Constants.port);
		while(true)
		{
			Socket connectionSocket = welcomeSocket.accept();
			DataInputStream inFromClient = new DataInputStream(connectionSocket.getInputStream());
			DataOutputStream outToClient = new DataOutputStream(connectionSocket.getOutputStream());
			clientSentence = inFromClient.readLine();
			System.out.println("Received: " + clientSentence);
			capitalizedSentence = clientSentence.toUpperCase() + '\n';
			outToClient.writeBytes(capitalizedSentence);
		}
=======
import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

class TCPServer extends Thread {

	ServerSocket welcomeSocket = null;

	TCPServer () {
		try {
			welcomeSocket = new ServerSocket(4010);

>>>>>>> dfe905a50f0e804c6508e5398cf044ced7c967e4
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
