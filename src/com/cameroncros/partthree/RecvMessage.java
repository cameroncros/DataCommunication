package com.cameroncros.partthree;

import java.net.Socket;

public class RecvMessage implements Runnable {
	
	Socket socket;
	
	RecvMessage(Socket sock) {
		socket = sock;
	}

	@Override
	public void run() {
		// TODO Auto-generated method stub
		
	}

}
