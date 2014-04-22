package partthree;

//import java.io.IOException;
//import java.security.GeneralSecurityException;
//
//import constants.Constants;

public class PeerChat {
	public static void main(String args[])  {
//		try {
//			String out = "";
//			StringEncryption se = new StringEncryption();
//			se.loadKey(Constants.keyfile);
//
//			byte[] by = se.encrypt("Hello World");
//			System.out.println(new String(by, "UTF-8"));
//			out = se.decrypt(by);
//			System.out.println(out);
//
//
//		}
//		catch (GeneralSecurityException | IOException e) {
//			System.out.println(e.getLocalizedMessage());
//			e.printStackTrace();
//		}


		System.out.println("Welcome To Peerchat");
		Thread listener = new Listener();
		listener.start();
		new Sender();
	}
}
