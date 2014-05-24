package fileshare.client;

import java.io.DataOutputStream;
import java.io.DataInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.net.Socket;
import java.util.Vector;

import constants.Constants;
import constants.Constants.MessageType;

/**
 * Code copied from: http://systembash.com/content/a-simple-java-tcp-server-and-tcp-client/
 * Accessed 29/Apr/14
 * Code copied from: http://www.javapractices.com/topic/TopicAction.do?Id=245
 * Accessed 29/Apr/14
 * @author  Cameron Cross 7193432
 * @since 29 April 2014
 */
public class TCPClient {
	Socket clientSocket = null;
	DataOutputStream outToServer = null;
	DataInputStream inFromServer = null;
	String myip;

	
	TCPClient(String address, int port) {
		try {
			//open a socket to the address and port given
			clientSocket = new Socket(address, port);
			//create input and output streams for the socket
			outToServer = new DataOutputStream(clientSocket.getOutputStream());
			inFromServer = new DataInputStream(clientSocket.getInputStream());
			sendRequest(MessageType.HELLO, null);
			System.out.println("Connected To Server");
			myip = clientSocket.getInetAddress().getHostAddress();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	/**
	 * Send the request to the server/peer
	 * @param mess
	 * @param values
	 * @return
	 */
	Vector<String> sendRequest(MessageType mess, Vector<String> values) {
		Vector<String> output = new Vector<String>();
		try {
			//Send request
			switch (mess) {
			case HELLO: //tell the server I am here
			case BYE: //tell the server i am leaving
				outToServer.writeInt(mess.value);
				break;
			case FIND: // give me a list of peers with the file
			case SEARCH: // give me a list of files that match my string
			case TELL: //Here are the files I have
			case GIVE:
				outToServer.writeInt(mess.value);
				Constants.sendArrayOfStrings(outToServer, values);
				break;
			default:
				throw new Exception("Invalid Value");
			}
			
			//get output, for requests that actually have an output
			switch (mess) {
			case HELLO: //tell the server I am here
			case BYE: //tell the server i am leaving
			case TELL: //Here are the files I have
				return null;
			case FIND: // give me a list of peers with the file
			case SEARCH: // give me a list of files that match my string
			case GIVE:
				output = Constants.readArrayOfStrings(inFromServer);
				return output;
			default:
				throw new Exception("Invalid Value");
			}
			
			

		}
		catch (IOException e) {
			System.err.println(e.getMessage());
		}
		catch (Exception e) {
			System.err.println(e.getMessage());
		}
		return values;
	}


	/**
	 *  requests the file from the server and writes it to the disk
	 * @param message - the message to be send
	 */
	void readFile(String filename) {
		FileOutputStream output = null;
		byte[] fileBytes = new byte[100];
		long fileLength;
		int temp;
		try {


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


}
