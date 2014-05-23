package fileshare.client;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.net.Socket;

import constants.Constants;

public class GetFile {
	Socket clientSocket = null;
	DataOutputStream outToServer = null;
	DataInputStream inFromServer = null;
	String directory = null;
	
	public GetFile(String address, String dir) throws Exception {
		try {
			directory = dir;
			//open a socket to the address and port given
			clientSocket = new Socket(address, Constants.port);
			//create input and output streams for the socket
			outToServer = new DataOutputStream(clientSocket.getOutputStream());
			inFromServer = new DataInputStream(clientSocket.getInputStream());
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public void getFile(String file) throws Exception {
		byte[] utf8Bytes;
		byte[] fileBytes = new byte[100];
		long fileRemaining, fileLength;
		int temp;
		FileOutputStream output = null;
		
		//convert filename to UTF-8 encoded bytes
		utf8Bytes = file.getBytes("UTF-8");
		//write the length of the string to the socket
		outToServer.writeInt(utf8Bytes.length);
		//write the filename to the socket
		outToServer.write(utf8Bytes, 0, utf8Bytes.length);


		//read a long from the server. This is the incoming number of bytes that the file takes
		fileLength = inFromServer.readLong();
		fileRemaining = fileLength;
		System.out.println(fileRemaining);

		//http://www.javapractices.com/topic/TopicAction.do?Id=245
		//open a file output stream, append "new" to the file so that it doesnt overwrite the file when run on the same machine
		output = new FileOutputStream(directory+File.separatorChar+file);

		
		while (fileRemaining != 0) {
			//determine the amount of file to read this one time. Altering chucksize might make this more efficient
			int chunk = Constants.chunkSize;
			//check if we are near the end and only read the remaining portion of the file if we are
			if (fileRemaining < Constants.chunkSize) {
				chunk=(int)fileRemaining;
			}
			//read the file data from the socket, returns amount of data read
			temp = inFromServer.read(fileBytes, 0, chunk);
			//decrement the total length of the file remaining in the socket
			fileRemaining-=temp;
			//write the file data to the file
			output.write(fileBytes, 0, temp);
			printProgress(((float)fileLength-(float)fileRemaining)/(float)fileLength, file);


		}
		//close file
		output.close();
	}
	
	void printProgress(float percentage, String file) {
		System.out.print(file + ": " + percentage*100 + "%\r");
	}

}
