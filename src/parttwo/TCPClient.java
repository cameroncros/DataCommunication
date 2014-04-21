package parttwo;

import java.io.BufferedOutputStream;
import java.io.DataOutputStream;
import java.io.DataInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.net.Socket;

import constants.Constants;

public class TCPClient {
	Socket clientSocket = null;
	DataOutputStream outToServer = null;
	DataInputStream inFromServer = null;

	TCPClient(String address, String port) {
		try {
			int pt = new Integer(port);
			clientSocket = new Socket(address, pt);
			outToServer = new DataOutputStream(clientSocket.getOutputStream());
			inFromServer = new DataInputStream(clientSocket.getInputStream());
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	void send(String message) {
		byte[] utf8Bytes;
		byte[] fileBytes = new byte[100];
		long fileLength;
		long temp;
		FileOutputStream output = null;
		try {
			utf8Bytes = message.getBytes("UTF-8");
			outToServer.writeInt(utf8Bytes.length);
			outToServer.write(utf8Bytes, 0, utf8Bytes.length);



			fileLength = inFromServer.readLong();
			temp = fileLength;

			//http://www.javapractices.com/topic/TopicAction.do?Id=245
			output = new FileOutputStream(message+"new");

			while (fileLength != 0) {
				int chunk = Constants.chunkSize;
				if (fileLength < Constants.chunkSize) {
					chunk=(int)fileLength;
				}
				temp = inFromServer.read(fileBytes, 0, chunk);
				fileLength-=temp;
				output.write(fileBytes, 0, chunk);


			}
			output.close();




		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	public static void main(String[] argv) {
		if (argv.length == 3) {
			new TCPClient(argv[0], argv[1]).send(argv[2]);
		}	
	}
}
