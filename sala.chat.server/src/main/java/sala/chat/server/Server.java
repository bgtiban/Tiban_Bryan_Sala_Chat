package sala.chat.server;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.net.SocketException;
import java.net.UnknownHostException;
import java.util.HashSet;

import sala.chat.comunes.constants.SalaChatInfo;

/**
 * En esta clase se trabaja con UDP, existen en el servidor dos sockets que
 * reciben paquetes, un socket recibe un paquete con únicamente el nº de puerto
 * del cliente, y otro socket recibe un paquete con el mensaje de texto enviado
 * por el cliente. este servidor muestra por consola mensajes los cuales
 * informan de los clientes que se han conectado y los mensjaes que se están
 * transmitiendo.
 *
 * @author Bryan Ti
 *
 */
public class Server {
	private static final int LISTENING_PORT_NEW_CONECTIONS = 1234;
	private DatagramSocket socketServer;
	private HashSet<InetSocketAddress> ports = new HashSet<>();;

	/**
	 * Constructor por defecto, inicia 2 hilos: uno para recibir conexiones nuevas de clientes y otro para envíar mensajes.
	 *
	 * @param host, es el nombre o IP que recibirá el servidor (para recibir
	 *        mensajes).
	 * @param port, es el puerto por el que escuchará el servidor (para recibir
	 *        mensajes).
	 * @throws SocketException
	 * @throws IOException
	 */
	public Server(String host, int port) throws SocketException, IOException {
		socketServer = new DatagramSocket(new InetSocketAddress(host, port));
		ThreadReceivePortNewClientConnected();
		ThreadReceiverMessages();
	}

	/**
	 * Método que crea un hilo el cual contiene un bucle que se encarga de recibir
	 * mensajes de los clientes, en cuanto recibe un mensjae lo imprime por consola
	 * y lo reenvía a todos los clientes conectados mediante el método {@link #sendMulticast(byte[])}
	 *
	 * @throws IOException
	 */
	private void ThreadReceiverMessages() throws IOException {
		Thread t = new Thread(new Runnable() {
			@Override
			public void run() {
				while (true) {
					try {
						byte[] lastMessageReceivedBytes = new byte[SalaChatInfo.MAX_LENGTH];
						DatagramPacket packet = new DatagramPacket(lastMessageReceivedBytes, SalaChatInfo.MAX_LENGTH);
						socketServer.receive(packet);
						System.out.println(new String(lastMessageReceivedBytes).trim());
						sendMulticast(lastMessageReceivedBytes);
					} catch (IOException e) {
						e.printStackTrace();
					}
				}
			}
		});
		t.start();
	}

	/**
	 * Este método recorre una lista que contiene los puertos de los clientes
	 * conectados. se crea un datagrama nuevo por cada reenvío.
	 *
	 * @throws UnknownHostException
	 */
	private void sendMulticast(byte[] message) throws UnknownHostException {
		for (InetSocketAddress port : ports) {
			try {
				DatagramPacket dp = new DatagramPacket(message, message.length, InetAddress.getByName(SalaChatInfo.LOCALHOST), port.getPort());
				socketServer.send(dp);
			} catch (IOException e) {
				e.printStackTrace();
			}
		}

	}

	/**
	 * Este método inicia un hilo el cual recibe el número de puerto del cliente conectado.
	 */
	private void ThreadReceivePortNewClientConnected() {
		Thread t = new Thread(new Runnable() {
			@Override
			public void run() {
				try (DatagramSocket socketReceptor = new DatagramSocket(new InetSocketAddress(SalaChatInfo.SERVER_HOST, LISTENING_PORT_NEW_CONECTIONS))) {
					byte[] msg = new byte[10];
					DatagramPacket receivedPacket = new DatagramPacket(msg, 10);
					while (true) {
						socketReceptor.receive(receivedPacket);

						System.out.println("Cliente nuevo con puerto: " + new String(msg).trim());
						Thread.sleep(2000);
						ports.add(new InetSocketAddress(SalaChatInfo.LOCALHOST, Integer.parseInt(new String(msg).trim())));

					}
				} catch (Exception e) {
					e.printStackTrace();
				}

			}
		});
		t.start();
	}
}
