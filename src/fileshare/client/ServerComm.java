package fileshare.client;

import java.util.Vector;

public class ServerComm extends Thread {

	TCPClient comm;
	public ServerComm(String string, int port) {
		comm = new TCPClient(string,port);
	}

	public void run() {
		// TODO Auto-generated method stub
		
	}

	public void quit() {
		// TODO Auto-generated method stub
		
	}

	public Vector<String> getHostsWithFile(String file) {
		// TODO Auto-generated method stub
		return null;
	}
	
	public Vector<String> getFilesMatchingString(String part) {
		// TODO Auto-generated method stub
		return null;
	}
	


}
