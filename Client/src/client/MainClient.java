package client;

import java.io.IOException;


public class MainClient {

	public static void main(String[] args) {
		try {
//			ServerSocket server = new ServerSocket(new Socket(args[0], Integer.parseInt(args[1])), Integer.parseInt(args[2]));
			ServerSocket server = new ServerSocket(new Socket("127.0.0.1", 5555), 1234);
			Client client = new Client(server);

			new JFClient(client).setVisible(true);
		} catch (IOException e) {
			e.printStackTrace();
		}

	}
}
