package fileshare.client;

import constants.Constants;

public class Client {

	void main(String[] argc) {
		FileServer td = new FileServer(Constants.port);
		td.start();
		ServerComm sc = new ServerComm(argc[0], Constants.port);
		sc.start();
		new UserInput(sc);

	}
}
