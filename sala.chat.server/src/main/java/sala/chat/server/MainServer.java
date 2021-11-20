package sala.chat.server;

import sala.chat.comunes.constants.SalaChatInfo;

public class MainServer {

	public static void main(String[] args) {

		// int clients = Integer.parseInt(args[0]); //PARA EJECUTAR DESDE CONSOLA.

		Thread ts = new Thread(new Runnable() {
			@Override
			public void run() {
				System.out.println("Servidor a la escucha...");
				new Server(SalaChatInfo.SERVER_HOST, SalaChatInfo.SERVER_PORT, SalaChatInfo.LISTENING_PORT_NEW_CONECTIONS);

			}
		});
		ts.start();

	}

}
