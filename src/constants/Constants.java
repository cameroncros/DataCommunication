package constants;

import java.io.File;

/**
 * This class just contains some constants that are used by the other classes. This is all original code
 * enum code inspired from: http://docs.oracle.com/javase/tutorial/java/javaOO/enum.html
 * Accessed 14th May 2014
 * @author Cameron
 * @since 16 April 2014
 */
public class Constants {
	//the size of a chuck to split a file into when sending it.
	public static int chunkSize = 100;
	
	//the default port to use
	public static int port = 4010;
	
	//the path of the file that contains the key
	public static File keyfile = new File("key.pem");

	public static String peerfile = "Peers.txt";
	
	public enum MessageType {
		HELLO(1), //tell the server I am here
		BYE(2), //tell the server i am leaving
		FIND(3), // give me a list of peers with the file
		SEARCH(4), // give me a list of files that match my string
		TELL(5), //Here are the files I have
		GIVE(6); //Give me the files

		public final int value;

		MessageType(int value) {
			this.value = value;
		}
	}

}
