package com.cameroncros.partthree;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

public class Listener implements Runnable {
	ServerSocket welcomeSocket = null;
	
	Listener () {
		
		try {
		welcomeSocket = new ServerSocket(4010);
		
		} 
		catch (Exception e) {
			System.out.println(e.getLocalizedMessage());
		}
	}
	
	void finalise() {
		try {
			welcomeSocket.close();
		}
		catch (Exception f) {
			System.out.println(f.getLocalizedMessage());
		}
	}

	@Override
	public void run() {
		while(true)
		{
			Socket connectionSocket;
			try {
				connectionSocket = welcomeSocket.accept();
				System.out.println(connectionSocket.getInetAddress().toString());
				new RecvMessage(connectionSocket);
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}

		}
		
	}
	
	
	
	
}
