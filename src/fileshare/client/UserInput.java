package fileshare.client;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.Vector;

public class UserInput {
	ServerComm sc = null;
	BufferedReader br = null;
	UserInput(ServerComm sc) {
		this.sc = sc;
		br = new BufferedReader(new InputStreamReader(System.in));
		InputLoop();
	}

	void InputLoop() {
		String input;
		String[] parts;
		boolean running = true;
		while (running) {
			try {
				input = br.readLine();

				parts = input.split(" ");
				switch (parts[0]) {
				case "/quit":
					sc.quit();
					running = false;
					break;
				case "/get":
					String file = parts[1];
					getFile(file);
					break;

				}
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	}

	private void getFile(String file) {
		Vector<String> hosts = sc.getHostsWithFile(file);
		if (hosts.size() == 0) {
			System.out.println("Couldnt find any hosts with that file");
			return;
		}
		while (hosts.size() > 0) {
			String host = null;
			try {
				int rand = (int) Math.floor((Math.random()*hosts.size()));
				host = hosts.get(rand);
				hosts.remove(rand);
				Peer pr = new Peer(host);
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
