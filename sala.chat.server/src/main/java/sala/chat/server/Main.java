package sala.chat.server;

import java.io.IOException;

public class Main {



	public static boolean server_ON = false;
	/*
	 *
	 * ESTE PROGRAMA HACE USO DE 3 CLASES, 2 DE ELLAS CONTIENEN LA LÓGICA DE SERVIDOR Y CLIENTE Y SE LLAMAN "CLIENT" Y "SERVER" RESPECTIVAMENTE, LA CLASE
	 * LLAMADA "JFCLIENT" CORRESPONDE CON LA INTERFAZ DEL CLIENTE.
	 *
	 * ESTE PROGRAMA TUVO DOS VERSIONES Y ACTUALMENTE ESTÁ EN USO LA PRIMERA VERSIÓN:
	 * 		PRIMERA VERSION: EL CLIENTE HACÍA USO DE DOS SOCKETS, POR UN SOCKET SE RECIBÍA Y ENVIABA EL MENSAJE DE CHAT Y POR EL OTRO SOCKET
	 * 						 SE ENVIABA SOLO EL Nº DE PUERTO DEL CLIENTE CONECTADO AL SERVIDOR.
	 * 						 EL SERVIDOR TAMBIÉN CONTENÍA DOS SOCKETS POR EL CUAL RECIBÍA MENSAJES DE LOS CLIENTES, EL OTRO SOCKET RECIBÍA EL Nº DE PUERTO
	 * 						 DEL CLIENTE.
	 * 		SEGUNDA VERSION: EL SERVIDOR CONTENÍA UN ÚNICO SOCKET MEDIANTE EL CUAL RECIBÍA Y ENVIABA MENSAJES, ASÍ COMO TAMBIÉN RECIBÍA EL Nº DE PUERTO DEL CLIENTE
	 * 						 (A DIFERENCIA DE LA PRIMERA VERSIÓN QUE LO HACIA A TRAVÉS DE OTRO PUERTO).
	 * 						 EL CLIENTE CONTENÍA UN ÚNICO SOCKET MEDIANTE EL CUAL ENVIABA UN DATAGRAMA CON EL MENSAJE.
	 */

	public static void main(String[] args) {

		//int clients = Integer.parseInt(args[0]); //PARA EJECUTAR DESDE CONSOLA.

		int clients = 2;//PARA EJECUTAR DESDE EL ENTORNO DE DESARROLLO.
		Thread ts = new Thread(new Runnable() {
			@Override
			public void run() {
				try {
					Main.server_ON = true;
					Server s = new Server("localhost", 5555);
				} catch (IOException e) {
					e.printStackTrace();
				}

			}
		});
		ts.start();

	}

}
