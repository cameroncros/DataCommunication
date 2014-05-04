package partthree;

import java.io.DataOutputStream;
import java.io.IOException;
import java.net.NoRouteToHostException;
import java.net.Socket;
import java.net.SocketException;
import java.security.GeneralSecurityException;
import java.security.InvalidKeyException;
import java.util.LinkedList;
import java.util.Queue;

import constants.Constants;
/**
 * Handles sending the message to a host
 * @author  Cameron Cross 7193432
 * @since 14 April 2014
 */
public class SendMessage extends Thread {
	Socket clientSocket = null;
	DataOutputStream outToServer = null;
	Boolean me = false;
	StringEncryption se;
	Boolean connected = false;
	String address;
	Queue<String> messageQueue = new LinkedList<String>();
	/**
	 * Sets up local variables for use later
	 * @param addr - The address to send the message to
	 */
	SendMessage(String addr) {
		address = addr;
	}

	/**
	 * Sets up the socket and checks if it is correct
	 */
	Boolean ConnectSocket() {
		try {
			//create socket to address and port given
			clientSocket = new Socket(address, Constants.port);
			//create output stream for socket
			outToServer = new DataOutputStream(clientSocket.getOutputStream());

			//get the address of the current pc
			String local = clientSocket.getLocalAddress().getHostAddress();
			//get the address of the other end
			String other = clientSocket.getInetAddress().getHostAddress();
			//check if we are speaking to ourself?
			if (local.compareTo(other) == 0) {
				me = true;
			}
			//create a string encryption object and load the key
			se = new StringEncryption();
			se.loadKey(Constants.keyfile);
			connected = true;
		} catch (NoRouteToHostException e) {
			connected = false;
		} catch (IOException | GeneralSecurityException e) {
			connected = false;
			e.printStackTrace();
		}
		return connected;
	}
	/**
	 * Adds message to the queue to be sent
	 * @param message - The message to be sent
	 */
	void send(String message) {
		//add message to queue object - this should be size limited, but its not really worth doing
		messageQueue.add(message);
		//get the current state of the thread, start it if it isnt running

		State st = this.getState();
		if (st == State.NEW) {
			this.start();
			//there is a potential issue here if the thread is running 
			//but about to finish in that the next message might end up in the queue without being sent, 
			//but it will get sent <=5 seconds later
		} else if (st == State.TIMED_WAITING) {
			this.interrupt();
		}

	}
	/**
	 * threaded portion that is run to send the messages
	 */   	
	@Override
	public void run() {
		//loop forever
		while (true) {
			//loop until we run out of messages to send
			while (!messageQueue.isEmpty()) {
				//check if we are connected, and try connect if we arent, 
				//sleep for 5 seconds to avoid rapidly retrying
				while (connected == false) { 
					if (ConnectSocket() == false) {
						try {
							Thread.sleep(5000);
						} catch (InterruptedException e) {

						}
					}
				}
				//check if we are talking to ourselves and exit if we are
				if (me) {
					return;
				}		

				String message = messageQueue.peek();
				byte[] utf8Bytes;			

				try {
					//encrypt the message into a string of bytes
					utf8Bytes = se.encrypt(message);
					//write the length to the socket
					outToServer.writeInt(utf8Bytes.length);
					//write the encrypted bytes to the socket'
					outToServer.write(utf8Bytes, 0, utf8Bytes.length);
					//remove message from queue as we were successful at this point
					messageQueue.remove();
				} catch (SocketException e) {
					connected = false;
					//do nothing, this means tha the other end has disconnected
				}
				catch (IOException |InvalidKeyException e) {
					connected = false;
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
			try {
				Thread.sleep(5000);
			} catch (InterruptedException e) {
				
			}
		}
	}
}
