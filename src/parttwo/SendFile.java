package parttwo;

import java.io.BufferedInputStream;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.net.Socket;

import constants.Constants;

public class SendFile extends Thread {
	
	Socket socket;
	DataInputStream inFromServer;
	DataOutputStream outToClient;
	
	SendFile(Socket sock) {
		
		socket = sock;
		try {
			 inFromServer = new DataInputStream(socket.getInputStream());
			 outToClient = new DataOutputStream(socket.getOutputStream());
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	@Override
	public void run() {
		int length = 0;
		String modifiedSentence;
		byte[] sendData = new byte[1024];
		byte[] fileBytes = new byte[100];
		while (true) {
			try {
				length = 0;
				while (length == 0) {
				length = inFromServer.readInt();
				length = inFromServer.read(sendData, 0, length);
				}
				if (length == -1) {
					//todo: put a nice message to indicate that the person has left
					return;
				}
				
				modifiedSentence = new String(sendData, 0, length, "UTF-8");
				//http://www.javapractices.com/topic/TopicAction.do?Id=245
				File file = new File(modifiedSentence);
				BufferedInputStream input = new BufferedInputStream(new FileInputStream(modifiedSentence));
				int fileLength = (int)file.length();
				outToClient.writeInt(fileLength);
				while (fileLength != 0) {
					int chunk = Constants.chunkSize;
					if (fileLength < Constants.chunkSize) {
						chunk=fileLength;
					}
					input.read(fileBytes, 0, chunk);
					outToClient.write(fileBytes, 0, chunk);
					fileLength-=chunk;
					
				}
				input.close();
				
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
					
		}		
	}
}
