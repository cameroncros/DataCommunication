package parttwo;

import java.io.DataOutputStream;
import java.io.DataInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.net.Socket;

import constants.Constants;

/**
 * Code copied from: http://systembash.com/content/a-simple-java-tcp-server-and-tcp-client/
 * Accessed 29/Apr/14
 * Code copied from: http://www.javapractices.com/topic/TopicAction.do?Id=245
 * Accessed 29/Apr/14
 * @author  Cameron Cross 7193432
 *
 */
public class TCPClient {
	Socket clientSocket = null;
	DataOutputStream outToServer = null;
	DataInputStream inFromServer = null;

	TCPClient(String address, String port) {
		try {
			//convert string to int for port
			int pt = new Integer(port);
			//open a socket to the address and port given
			clientSocket = new Socket(address, pt);
			//create input and output streams for the socket
			outToServer = new DataOutputStream(clientSocket.getOutputStream());
			inFromServer = new DataInputStream(clientSocket.getInputStream());
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
/**
 *  requests the file from the server and writes it to the disk
 * @param message - the message to be send
 */
	void requestFile(String filename) {
		byte[] utf8Bytes;
		byte[] fileBytes = new byte[100];
		long fileLength;
		int temp;
		FileOutputStream output = null;
		try {
			//prepend "Read " to the message as requested by the assignement doc
			String message = "Read " + filename;
			//convert filename to UTF-8 encoded bytes
			utf8Bytes = message.getBytes("UTF-8");
			//write the length of the string to the socket
			outToServer.writeInt(utf8Bytes.length);
			//write the filename to the socket
			outToServer.write(utf8Bytes, 0, utf8Bytes.length);


			//read a long from the server. This is the incoming number of bytes that the file takes
			fileLength = inFromServer.readLong();
			
			System.out.println(fileLength);

			//http://www.javapractices.com/topic/TopicAction.do?Id=245
			//open a file output stream, append "new" to the file so that it doesnt overwrite the file when run on the same machine
			output = new FileOutputStream(filename+"new");

			
			while (fileLength != 0) {
				//determine the amount of file to read this one time. Altering chucksize might make this more efficient
				int chunk = Constants.chunkSize;
				//check if we are near the end and only read the remaining portion of the file if we are
				if (fileLength < Constants.chunkSize) {
					chunk=(int)fileLength;
				}
				//read the file data from the socket, returns amount of data read
				temp = inFromServer.read(fileBytes, 0, chunk);
				//decrement the total length of the file remaining in the socket
				fileLength-=temp;
				//write the file data to the file
				output.write(fileBytes, 0, temp);


			}
			//close file
			output.close();

		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
/**
 * This is the main function
 * @param argv - Array of strings containing [addr] [port] [filename]
 */
	public static void main(String[] argv) {
		if (argv.length == 3) {
			//create and initialise the TCPClient object
			TCPClient tc = new TCPClient(argv[0], argv[1]);
			//request a file
			tc.requestFile(argv[2]);
		} else {
			System.out.println("expected: [addr] [port] [filename]");
		}
	}
}
