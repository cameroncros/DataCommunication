package partone;

import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;

import constants.Constants;

/**
 * This code is stolen from: http://systembash.com/content/a-simple-java-udp-server-and-udp-client/#sthash.CAm9v4QA.dpuf
 * Accessed 13 April 2014
 * @author Cameron Cross 7193432
 * @since 13 April 2014
 */

public class UDPClient {
	/**
	 * Main function, there are no real classes for this limited program
	 * @param args - String array containing: [address] [port] 
	 */
	public static void main(String args[])    
	{  
		//here are 2 variables to store the address and port
		String addr = "localhost";
		Integer port = Constants.port;
		//checks if the correct amount of arguements are supplied and sets the variables accordingly
		if (args.length == 2) {
			addr = args[0];
			try {
				port = new Integer(args[1]);
				if (port < 0 || port > 65535) {
					throw new Exception();
				}
			}
			catch (Exception e) {
				System.out.println("Port is not a valid integer");
				return;
			}
		} else {
			System.out.println("Expects: [address] [port]");
			return;
		}
		try {
			//creates a datagram socket object
			DatagramSocket clientSocket = new DatagramSocket();  
			//creates an inet address object and initialises it by looking up the address given before
			InetAddress IPAddress = InetAddress.getByName(addr);  
			//these two lines declare 2 buffers. They are fixed length as this program is pretty simple
			byte[] sendData = new byte[1024];       
			byte[] receiveData = new byte[1024];  
			//this is the request to the server;
			String sentence = "Hello Server, who are you? I am Cameron Cross, 7193432";
			//places the servers sentence into the buffer
			sendData = sentence.getBytes(); 
			//creates a datagram packet and puts sentence into the packet. It addresses the packet to the IPAddress looked up before, and the port declared before
			DatagramPacket sendPacket = new DatagramPacket(sendData, sendData.length, IPAddress,  port); 
			//actually sends the packet
			clientSocket.send(sendPacket);
			//creates a datagram socket to be filled when we start listening for a packet
			DatagramPacket receivePacket = new DatagramPacket(receiveData, receiveData.length); 
			//start listening for a packet, and blocks until it receives one.
			clientSocket.receive(receivePacket);   
			//interprets the packet payload into a string
			String modifiedSentence = new String(receivePacket.getData(), "UTF-8");
			//prints the payload sentence
			System.out.println("FROM SERVER:" + modifiedSentence);   
			//closes the socket
			clientSocket.close();  
		}
		//this exception is extremely broad as it doesnt need to be particularly complex. It simply prints an error message and ends the program.
		catch (Exception e) {
			System.out.println("Failed to run: "+ e.getLocalizedMessage());
		}
	} //See more at: http://systembash.com/content/a-simple-java-udp-server-and-udp-client/#sthash.CAm9v4QA.dpuf
}
