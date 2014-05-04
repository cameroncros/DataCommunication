package partthree;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.HashMap;
import java.util.Map;

/**
 * Code copied from: http://alvinalexander.com/java/edu/pj/pj010005
 * Accessed 16/4/14 - Modified BufferedReader code
 * @author Cameron Cross 7193432
 * @since 16 April 2014
 */
public class Sender {
	PeerList peers = null;
	Map<String, SendMessage> peerSender = new HashMap<String, SendMessage>();
/**
 * Sets up the class and starts the send loop
 */
	Sender() {
		//get all the peers to send to
		peers = PeerList.getInstance();
		peers.readFile();
		SendLoop();
	}
	//http://alvinalexander.com/java/edu/pj/pj010005
	// Accessed 16/4/14 - Modified BufferedReader code
	/**
	 * waits for input from the keyboard and sends it to the peers
	 */
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
				//read a message
				message = br.readLine();
				//check if the message is /quit and exit in that case
				if (message == null || message.compareTo("/quit") == 0) {
					System.exit(0);
				}
				//get all the peers
				peers.readFile();
				//loop through peers
				for (String f : peers.getPeers().keySet()) {
					//check if there is a class to send to this peer and if there is, check that it is connected still
					if (peerSender.containsKey(f) != true) {
						//create a new class to be used to send to that peer
						peerSender.put(f, new SendMessage(f));
					}
					//send the message to that peer
					peerSender.get(f).send(message);
				}

			} catch (IOException e) {
				System.out.println(e.getLocalizedMessage());
			}
		}

	}

}
