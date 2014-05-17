package fileshare.server;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;
import java.util.Vector;

import constants.Constants;
import constants.Constants.MessageType;

public class HandleClient extends Thread {
	Socket clientSocket = null;
	DataOutputStream outToClient = null;
	DataInputStream inFromClient = null;
	boolean connected = false;
	String ipaddr = null;
	FileList files;
	
	public HandleClient(Socket connectionSocket, FileList list) {
		clientSocket = connectionSocket;
		files = list;
		//create input and output streams for the socket
		try {
			ipaddr = clientSocket.getInetAddress().getHostAddress();
			outToClient = new DataOutputStream(clientSocket.getOutputStream());
			inFromClient = new DataInputStream(clientSocket.getInputStream());
			connected = true;
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
	}
	
	
    public void run() {
    	Vector<String> values = null, results;
    	try {
    	while (connected) {
    		int command = inFromClient.readInt();
    		switch (MessageType.get(command)) {
			case HELLO: //tell the server I am here
				files.AddClient(ipaddr);
				break;
			case BYE: //tell the server i am leaving
				files.DeleteClient(ipaddr);
				files.removeClientsFiles(ipaddr);
				break;
			case FIND: // give me a list of peers with the file
				values = Constants.readArrayOfStrings(inFromClient);
				results = files.findPeersForFile(values);
				Constants.sendArrayOfStrings(outToClient, results);
				break;
			case SEARCH: // give me a list of files that match my string
				values = Constants.readArrayOfStrings(inFromClient);
				results = files.search(values);
				Constants.sendArrayOfStrings(outToClient, results);
				break;
			case TELL: //Here are the files I have
				values = Constants.readArrayOfStrings(inFromClient);
				files.addFiles(ipaddr, values);
				break;
			case GIVE:
    			//shouldnt happen here. If it does, close the connection and the the client think about what it did.
				connected = false;
				clientSocket.close();
				return;
    		}
    	}
    	}
    	catch (IOException e) {
    		e.printStackTrace();
    	}
    }



}
