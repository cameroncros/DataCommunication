package partthree;

public class PeerChat {
	public static void main(String args[])  {
		System.out.println("Welcome To Peerchat");
		Thread listener = new Listener();
		listener.start();
		new Sender();
	}
}
