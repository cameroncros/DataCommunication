package constants;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.Vector;

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
	public static int serverport = 4004;




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

		//code from http://www.xyzws.com/Javafaq/how-to-reverse-lookup-an-enum-from-its-values-in-java/187
		//accessed: 16/May/2014
		private static final Map<Integer, MessageType> lookup = 	new HashMap<Integer, MessageType>();
		static {
			//Create reverse lookup hash map 
			for(MessageType d : MessageType.values())
				lookup.put(d.value, d);
		}

		public static MessageType get(int mt) { 
			//the reverse lookup by simply getting 
			//the value from the lookup HsahMap. 
			return lookup.get(mt); 
		}




	}

	/**
	 * Reads a vector of strings
	 * @return
	 */
	public static Vector<String> readArrayOfStrings(DataInputStream in) {
		Vector<String> array = new Vector<String>();
		try {
			int numStrings = in.readInt();
			for (int i = 0; i < numStrings; i++) {
				int len = in.readInt();
				byte byteArray[] = new byte[len];
				in.read(byteArray, 0, len);
				String value = new String(byteArray);
				array.add(value);


			}
		} 
		catch (IOException e) {
			e.printStackTrace();
		}

		return array;
	}
	public static void sendArrayOfStrings(DataOutputStream out, Vector<String> values) {
		try {
			if (values == null) {
				out.writeInt(0);
				return;
			}
			out.writeInt(values.size());

			for (int i = 0; i < values.size(); i++) {
				byte byteArray[] = values.get(i).getBytes();
				out.writeInt(byteArray.length);
				out.write(byteArray, 0, byteArray.length);
			}

		}

		catch (IOException e) {
			e.printStackTrace();
		}
	}

}
