package partthree;

/**
 * Main class of the P2P chat program
 * @author  Cameron Cross 7193432
 */
public class PeerChat {
	/**
	 * Sets up the listener and sender classes
	 */
	public static void main()  {



		System.out.println("Welcome To Peerchat");
		//creates a thread to listen for all new chat messages
		Thread listener = new Listener();
		//starts the listener thread
		listener.start();
		//sends data to the other users
		new Sender();
	}
}
