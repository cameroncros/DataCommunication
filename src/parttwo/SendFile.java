package parttwo;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.net.Socket;

import constants.Constants;

/**
 * Code Borrowed From: //http://www.javapractices.com/topic/TopicAction.do?Id=245
 * Accessed 14 April 2014
 * @author cameron
 *
 */

public class SendFile extends Thread {

	Socket socket;
	DataInputStream inFromServer;
	DataOutputStream outToClient;

	SendFile(Socket sock) {

		socket = sock;
		try {
			//creates an input stream from the socket passed in
			inFromServer = new DataInputStream(socket.getInputStream());
			//creates an output stream from the socket passed in
			outToClient = new DataOutputStream(socket.getOutputStream());
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	@Override
	public void run() {
		int length = 0;
		String filename;
		byte[] sendData = new byte[1024];
		byte[] fileBytes = new byte[100];
		try {
			length = 0;
			while (length == 0) {
				//read an int from the socket. This is the length of the message
				length = inFromServer.readInt();
				//read the filename from the socket
				length = inFromServer.read(sendData, 0, length);
			}
			if (length == -1) {
				//todo: put a nice message to indicate that the person has left
				return;
			}
			//convert the byte array into the filename
			filename = new String(sendData, 0, length, "UTF-8");
			
			//create a file object that will contain details about the file
			File file = new File(filename);
			//check if the file exists at the server and close the socket if it doesnt
			if (file.exists() == false) {
				socket.close();
				return;
			}
			//open the file into an input stream
			FileInputStream input = new FileInputStream(filename);
			//get the file length
			long fileLength = file.length();
			//send the length out the socket - if this was a true protocol there should be a conversion for endianness, although that is more of a C issue as java is network endian
			outToClient.writeLong(fileLength);
			//loop while reading the file
			while (fileLength != 0) {
				//determine the amount of file to read this one time. Altering chucksize might make this more efficient
				int chunk = Constants.chunkSize;
				//check if we are near the end and only send the remaining portion of the file if we are
				if (fileLength < Constants.chunkSize) {
					chunk=(int)fileLength;
				}
				//read a "chunl" sized portion of the file
				input.read(fileBytes, 0, chunk);
				//write the portion of the file out to the socket
				outToClient.write(fileBytes, 0, chunk);
				//decrement the size of the file left to be sent
				fileLength-=chunk;

			}
			//close the file stream
			input.close();
			//close the socket
			socket.close();

		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}
}
