package partone;

import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;

public class UDPClient {
	public static void main(String args[])    
	{  
		try {
			DatagramSocket clientSocket = new DatagramSocket();  
			InetAddress IPAddress = InetAddress.getByName("localhost");   
			byte[] sendData = new byte[1024];       
			byte[] receiveData = new byte[1024];  
			String sentence = "Hello Server, who are you? I am 7193432";    
			sendData = sentence.getBytes(); 
			DatagramPacket sendPacket = new DatagramPacket(sendData, sendData.length, IPAddress, 4010); 
			clientSocket.send(sendPacket);  
			DatagramPacket receivePacket = new DatagramPacket(receiveData, receiveData.length); 
			clientSocket.receive(receivePacket);   
			String modifiedSentence = new String(receivePacket.getData());  
			System.out.println("FROM SERVER:" + modifiedSentence);    
			clientSocket.close();  
		}
		catch (Exception e) {
			System.out.println("Failed to run: "+ e.getLocalizedMessage());
		}
	} //See more at: http://systembash.com/content/a-simple-java-udp-server-and-udp-client/#sthash.CAm9v4QA.dpuf
}
