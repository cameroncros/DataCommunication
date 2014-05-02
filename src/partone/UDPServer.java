package partone;

import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;

import constants.Constants;



/**
 * This code is entirely lifted from: http://systembash.com/content/a-simple-java-udp-server-and-udp-client/
 * Accessed 13 April 2014
 * @author  Cameron Cross 7193432
 *
 */

public class UDPServer {
	/**
	 * This program has no real classes, only a main function
	 * @param args - String array containing: [port]
	 */
	public static void main(String args[])     
	{
		//initialises a server port with the default port, and then checks if the user has supplied a real port 
		int serverport = Constants.port;
		try {
			if (args.length == 1) {
				serverport = new Integer(args[0]);
			} else {
				System.out.println("Expects: [port]");
				return;
			}
			if (serverport < 0 || serverport > 65535) {
				throw new Exception();
			}
		}
		catch (Exception e) {
			System.out.println("Port is not a valid number");
			return;
		}
		DatagramSocket serverSocket;
		/**
		 * create new socket on port 9876
		 */
		try {
			//creates a DatagramSocket and opens it on the port specified
			serverSocket = new DatagramSocket(serverport);
		} 
		catch (Exception e) {
			System.out.println("Failed to open a socket: " + e.getLocalizedMessage());
			return;
		}
		//buffers for receiveing and sending data
		byte[] receiveData = new byte[1024];          
		byte[] sendData = new byte[1024];
		try {
			//main loop starts here.
			while(true)               
			{           
				//This variable is a class that contains the packet we are about to receive
				DatagramPacket receivePacket = new DatagramPacket(receiveData, receiveData.length);  
				//listen for a datagram packet. Block until it receives one
				serverSocket.receive(receivePacket);     
				//the following code should be placed into a worker thread, but for a simple program this is not necessary
				//interpret the packet into a string
				//The trim part removes the excess empty space at the end of the string.
				String sentence = (new String( receivePacket.getData(), "UTF-8")).trim();     
				//printout what we received
				System.out.println("RECEIVED: " + sentence);
				//get the users id and name ( everything after 32 character, weak protocol, but it is mine to control)
				String user = sentence.substring(32);
				//get the address of the other end so that we can respond
				InetAddress IPAddress = receivePacket.getAddress(); 
				//get the port as well
				int port = receivePacket.getPort();        
				//the following string is the response from the server
				sentence = "Hello, " + user + ", I am Cameron Crosses server."; 
				//convert it into a byte array
				sendData = sentence.getBytes("UTF-8");      
				//create the datagram packet with the other ends ipaddress and port, and the data the we want to send
				DatagramPacket sendPacket = new DatagramPacket(sendData, sendData.length, IPAddress, port);  
				//send the packet and re begin loop
				serverSocket.send(sendPacket);       
			}
		}
		catch (Exception e) {
			//there is no real end to this code, so the socket doesnt get closed normally
			//if something goes wrong the socket will get closed here
			//also, this prevents eclipse complaining about the memory leak
			serverSocket.close();
		}
	} //See more at: http://systembash.com/content/a-simple-java-udp-server-and-udp-client/#sthash.CAm9v4QA.dpuf

}
