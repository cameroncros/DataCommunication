package fileshare.client;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.Vector;

import constants.Constants;
import constants.Constants.MessageType;

public class Client {

	MusicScanner files;
	TCPClient comm;
	BufferedReader br = null;

	public Client(String[] argc) {
		if (argc.length != 2) {
			System.out.println("Expects [addr] [dir]");
			System.exit(1);
		}
		files = new MusicScanner(argc[1]);
		FileServer td = new FileServer(Constants.port);
		td.start();

		comm = new TCPClient(argc[0],Constants.serverport);
		br = new BufferedReader(new InputStreamReader(System.in));
		updateServer();
		inputLoop();

	}

	public static void main(String[] argc) {
		new Client(argc);

	}

	void inputLoop() {
		String stringinput;
		String[] parts;
		boolean running = true;
		Vector<String> results = null;
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
					break;
				case "/get":
					try {
						int val = new Integer(parts[1]);
						inputs.add(results.get(val-1));
					}
					catch (Exception e) {
						inputs.add(parts[1]);
					}
					output = comm.sendRequest(MessageType.FIND, inputs);
					getFile(parts[1], output);
					updateServer();
					break;
				case "/search":
					for (int i = 1; i < parts.length; i++) {
						inputs.add(parts[i]);
					}
					results = comm.sendRequest(MessageType.SEARCH, inputs);
					int i = 1;
					for (String files : results) {
						System.out.println(i+") "+files);
						i++;
					}
					break;
				default:
					break;
				}
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	}
	
	private void updateServer() {
		files.scanDirectory();
		comm.sendRequest(MessageType.TELL, files.getFileList());
	}

	private void getFile(String file, Vector<String> hosts) {
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
				GetFile pr = new GetFile(host);
				pr.getFile(file);
				System.out.println("Successfully got your file: "+file);
				return;
			}
			catch (Exception e) {
				System.out.println("Something went wrong with host: "+host+", Trying another...");
			}
		}
		System.out.println("None of the hosts given, actually gave us the file. This is why we cant have nice things");
	}
}
