package partone;

import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;



/**
 * This code is entirely lifted from: http://systembash.com/content/a-simple-java-udp-server-and-udp-client/
 * @author systemBash
 *
 */

public class UDPServer {
	public static void main(String args[])     
	{
		DatagramSocket serverSocket;
		/**
		 * create new socket on port 9876
		 */
		try {
			serverSocket = new DatagramSocket(4010);
		} 
		catch (Exception e) {
			System.out.println("Failed to open a socket: " + e.getLocalizedMessage());
			return;
		}
		/**
		 * buffers for receiveing and sending data
		 */
		byte[] receiveData = new byte[1024];          
		byte[] sendData = new byte[1024];
		try {
			while(true)               
			{           
				/**
				 * This variable is a class that contains the packet we are about to receive
				 */
				DatagramPacket receivePacket = new DatagramPacket(receiveData, receiveData.length);  
				/**
				 * 
				 */
				serverSocket.receive(receivePacket);     
				String sentence = new String( receivePacket.getData());     
				System.out.println("RECEIVED: " + sentence);            
				InetAddress IPAddress = receivePacket.getAddress();          
				int port = receivePacket.getPort();             
				sentence = "Hello, my name is Cameron's Plagerised UDP Server And my Id is 7193432";         
				sendData = sentence.getBytes();            
				DatagramPacket sendPacket = new DatagramPacket(sendData, sendData.length, IPAddress, port);      
				serverSocket.send(sendPacket);       
			}
		}
		catch (Exception e) {
			serverSocket.close();
		}
	} //See more at: http://systembash.com/content/a-simple-java-udp-server-and-udp-client/#sthash.CAm9v4QA.dpuf

}
