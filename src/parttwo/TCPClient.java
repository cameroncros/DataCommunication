package parttwo;

import java.io.BufferedOutputStream;
import java.io.DataOutputStream;
import java.io.DataInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.net.Socket;

public class TCPClient {
	Socket clientSocket = null;
	DataOutputStream outToServer = null;
	DataInputStream inFromServer = null;

	TCPClient(String address) {
		try {
			clientSocket = new Socket(address, 4010);
			outToServer = new DataOutputStream(clientSocket.getOutputStream());
			inFromServer = new DataInputStream(clientSocket.getInputStream());
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	void send(String message) {
		byte[] utf8Bytes;
		byte[] fileBytes = new byte[100];
		int fileLength;
		int temp;
		BufferedOutputStream output = null;
		try {
			utf8Bytes = message.getBytes("UTF-8");
			outToServer.writeInt(utf8Bytes.length);
			outToServer.write(utf8Bytes, 0, utf8Bytes.length);



			fileLength = inFromServer.readInt();
			temp = fileLength;

			//http://www.javapractices.com/topic/TopicAction.do?Id=245
	        output = new BufferedOutputStream(new FileOutputStream(message));
	        
			while (fileLength != 0) {
				int chunk = 100;
				if (fileLength < 100) {
					chunk=fileLength;
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
		new TCPClient(argv[1]);
	}
}
