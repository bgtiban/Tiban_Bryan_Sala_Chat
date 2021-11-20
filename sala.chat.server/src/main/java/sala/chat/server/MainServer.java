package sala.chat.server;

import java.io.IOException;

import sala.chat.comunes.constants.SalaChatInfo;

public class MainServer {

	public static void main(String[] args) {

		//int clients = Integer.parseInt(args[0]); //PARA EJECUTAR DESDE CONSOLA.

		Thread ts = new Thread(new Runnable() {
			@Override
			public void run() {
				try {
					System.out.println("Servidor a la escucha...");
					new Server(SalaChatInfo.SERVER_HOST, SalaChatInfo.SERVER_PORT);
				} catch (IOException e) {
					e.printStackTrace();
				}

			}
		});
		ts.start();

	}

}
