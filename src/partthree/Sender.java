package partthree;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.HashMap;
import java.util.Map;

public class Sender {
	PeerList peers = null;
	Map<String, SendMessage> peerSender = new HashMap<String, SendMessage>();
	
	Sender() {
		peers = PeerList.getInstance();
		peers.readFile();
		SendLoop();
	}
	//http://alvinalexander.com/java/edu/pj/pj010005
	// Accessed 16/4/14 - Modified BufferedReader code
	void SendLoop() {
		while (true) {
			System.out.print("> ");
		 //  open up standard input
	      BufferedReader br = new BufferedReader(new InputStreamReader(System.in));

	      String message = null;

	      //  read the message from the command-line; need to use try/catch with the
	      //  readLine() method
	      try {
	    	  message = null;
	         message = br.readLine();
	         if (message.compareTo("/quit") == 0) {
	        	 System.exit(0);
	         }
	         for (String f : peers.getPeers()) {
	        	 if (peerSender.containsKey(f) != true) {
	        		 peerSender.put(f, new SendMessage(f));
	        	 }
	        	 peerSender.get(f).send(message);
	         }
	         
	      } catch (IOException e) {
	         System.out.println(e.getLocalizedMessage());
	      }
		}
		
	}

}
