package server;

public class MainServer {

	public static void main(String[] args) {

		Thread ts = new Thread(new Runnable() {
			@Override
			public void run() {
				if(args == null || args.length < 3) {
					System.out.println("Argumentos esperados: [0]IP servidor, [1]Puerto servidor, [2]Puerto escucha nuevos clientes servidor");
				}else {
					new Server(args[0], Integer.parseInt(args[1]), Integer.parseInt(args[2]));
					System.out.println("Servidor a la escucha...");
//					new Server("127.0.0.1", 5555, 1234);
				}

			}
		});
		ts.start();

	}

}
