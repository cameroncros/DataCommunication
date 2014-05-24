package fileshare.client;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.Vector;

import constants.Constants;
import constants.Constants.MessageType;
/**
 * This is the main class that the client uses
 * @author Cameron
 *
 */
public class Client {

	MusicScanner files;
	TCPClient comm;
	BufferedReader br = null;
	String dir = null;

	public Client(String[] argc) {
		if (argc.length != 2) {
			System.out.println("Expects [addr] [dir]");
			System.exit(1);
		}
		dir = argc[1];
		files = new MusicScanner(dir);
		FileServer td = new FileServer(Constants.port, dir);
		td.start();

		comm = new TCPClient(argc[0],Constants.serverport);
		br = new BufferedReader(new InputStreamReader(System.in));
		updateServer();
		inputLoop();

	}

	public static void main(String[] argc) {
		new Client(argc);

	}
/**
 * parses the input from the console and runs the required code
 */
	void inputLoop() {
		String stringinput;
		String[] parts;
		boolean running = true;
		Vector<String> results = null;
		int i;
		while (running) {
			try {
				stringinput = br.readLine();
				parts = stringinput.split(" ");

				Vector<String> inputs = new Vector<String>();
				Vector<String> output = null;

				switch (parts[0]) {
				case "/quit":
					comm.sendRequest(MessageType.BYE, null);
					running = false;
					System.exit(0);
				case "/get":
					try {
						int val = new Integer(parts[1]);
						inputs.add(results.get(val-1));
					}
					catch (Exception e) {
						inputs.add(parts[1]);
					}
					output = comm.sendRequest(MessageType.FIND, inputs);
					i=1;
					for (String peers : output) {
						System.out.println(i+") "+peers);
						i++;
					}
					getFile(inputs.firstElement(), output);
					updateServer();
					break;
				case "/search":
					for (i = 1; i < parts.length; i++) {
						inputs.add(parts[i]);
					}
					results = comm.sendRequest(MessageType.SEARCH, inputs);
					for (String file : files.getFileList()) {
						results.remove(file);
					}
					i=1;
					for (String files : results) {
						System.out.println(i+") "+files);
						i++;
					}
					break;
				case "/update":
					updateServer();
					break;
				case "/help":
				default:
					System.out.println("Available commands:");
					System.out.println("\t/search - Search for files");
					System.out.println("\t/get - get the file either by name or the index of the search");
					System.out.println("\t/update - update the servers list of files");
					System.out.println("\t/quit - quit");
					break;
				}
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	}
	/**
	 * Sends the server all the files that we have
	 */
	private void updateServer() {
		files.scanDirectory();
		comm.sendRequest(MessageType.TELL, files.getFileList());
	}
/**
 * Gets the file from one of the hosts listed. Hosts are chosen at random
 * @param file
 * @param hosts
 */
	private void getFile(String file, Vector<String> hosts) {
		if (files.getFileList().contains(file)) {
			System.out.println("You already have this file");
		}
		if (hosts == null || hosts.size() == 0) {
			System.out.println("Couldnt find any hosts with that file");
			return;
		}
		while (hosts.size() > 0) {
			String host = null;
			try {
				int rand = (int) Math.floor((Math.random()*hosts.size()));
				host = hosts.get(rand);
				hosts.remove(rand);
				if (host.compareTo(comm.myip) != 0) {
					GetFile pr = new GetFile(host, dir);
					pr.getFile(file);
				}
				System.out.println("Successfully got your file: "+file);
				return;
			}
			catch (Exception e) {
				e.printStackTrace();
				System.out.println("Something went wrong with host: "+host+", Trying another...");
			}
		}
		System.out.println("None of the hosts given, actually gave us the file. This is why we cant have nice things");
	}
}
