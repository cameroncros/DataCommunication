package parttwo;

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
		} 
		catch (Exception e) {
			System.out.println(e.getLocalizedMessage());
			try {
				welcomeSocket.close();
			}
			catch (Exception f) {
				System.out.println(f.getLocalizedMessage());
			}
		}
	} 
	}//See more at: http://systembash.com/content/a-simple-java-tcp-server-and-tcp-client/#sthash.TI4X4ulR.dpuf
