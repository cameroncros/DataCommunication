package partthree;

/**
 * Main class of the P2P chat program
 * @author cameron
 */
public class PeerChat {
	public static void main(String args[])  {



		System.out.println("Welcome To Peerchat");
		//creates a thread to listen for all new chat messages
		Thread listener = new Listener();
		//starts the listener thread
		listener.start();
		//sends data to the other users
		new Sender();
	}
}
