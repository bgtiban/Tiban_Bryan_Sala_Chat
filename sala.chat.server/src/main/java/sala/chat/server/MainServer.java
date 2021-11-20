package sala.chat.server;

public class MainServer {

	public static void main(String[] args) {

		Thread ts = new Thread(new Runnable() {
			@Override
			public void run() {
				System.out.println("Servidor a la escucha...");
//				new Server(args[0], Integer.parseInt(args[1]), Integer.parseInt(args[2]));
				new Server("127.0.0.1", 5555, 1234);

			}
		});
		ts.start();

	}

}
