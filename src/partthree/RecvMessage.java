package partthree;

import java.io.DataInputStream;
import java.io.IOException;
import java.net.Socket;
import java.util.Vector;

public class RecvMessage extends Thread {
	
	Socket socket;
	DataInputStream inFromServer;
	Boolean auth = true;
	Vector<String> peers;
	
	RecvMessage(Socket sock) {
		peers = (new PeerList()).getPeers();
		socket = sock;
		try {
			String ipaddr = socket.getInetAddress().getHostAddress();
			if (!peers.contains(ipaddr)) {
				System.out.println("Connection from an invalid host: " + ipaddr);
				auth = false;
			}
			
			inFromServer = new DataInputStream(socket.getInputStream());
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
		if (auth == false) {
			return;
		}
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
				System.out.println("<" + socket.getInetAddress() + ">: " + modifiedSentence);
				
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
					
		}		
	}
}
