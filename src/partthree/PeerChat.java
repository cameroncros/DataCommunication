package partthree;

/**
 * Main class of the P2P chat program
 * @author  Cameron Cross 7193432
 * @since 14 April 2014
 */
public class PeerChat {
	/**
	 * Sets up the listener and sender classes
	 */
	public static void main(String[] args)  {



		System.out.println("Welcome To Peerchat");
		//print all available peers
		PeerList.getInstance().printPeers(); 
		//creates a thread to listen for all new chat messages
		Thread listener = new Listener();
		//starts the listener thread
		listener.start();
		//sends data to the other users
		new Sender();
	}
}
