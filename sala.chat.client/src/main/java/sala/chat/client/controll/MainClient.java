package sala.chat.client.controll;

import java.io.IOException;

import sala.chat.client.model.Client;
import sala.chat.client.view.JFClient;
import sala.chat.comunes.Server;
import sala.chat.comunes.Socket;

public class MainClient {

	public static void main(String[] args) {
		try {
			Socket socketClient = new Socket("127.0.0.1", -1);
			Server server = new Server(new Socket("127.0.0.1", 5555), 1234);
			Client client = new Client(socketClient, server);

			new JFClient(client).setVisible(true);
		} catch (IOException e) {
			e.printStackTrace();
		}

	}
}
