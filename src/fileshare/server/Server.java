package fileshare.server;

import constants.Constants;

public class Server {
	ClientListener listener = new ClientListener(Constants.port);
}
