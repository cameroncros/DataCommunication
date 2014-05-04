package constants;

import java.io.File;

/**
 * This class just contains some constants that are used by the other classes. This is all original code
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
}
