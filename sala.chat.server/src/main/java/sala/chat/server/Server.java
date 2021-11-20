package sala.chat.server;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.ObjectInput;
import java.io.ObjectInputStream;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.net.SocketException;
import java.net.UnknownHostException;
import java.util.HashSet;

import sala.chat.comunes.Socket;
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

	private DatagramSocket socketServer;
	private HashSet<InetSocketAddress> clientPorts = new HashSet<>();
	private int listeningConnectionsPort;

	/**
	 * Constructor por defecto, inicia 2 hilos: uno para recibir conexiones nuevas de clientes y otro para envíar mensajes.
	 *
	 * @param serverIP, es el nombre o IP que recibirá el servidor (para recibir
	 *        mensajes).
	 * @param listeningMessagesPort, puerto de esucha para recibir los mensajes enviados por clientes.
	 * @param listeningConnectionsPort, puerto de escucha para nuevas conexiones.
	 * @throws SocketException
	 * @throws IOException
	 */
	public Server(String serverIP, int listeningMessagesPort, int listeningConnectionsPort){

		try {
			this.listeningConnectionsPort = listeningConnectionsPort;
			this.socketServer = new DatagramSocket(new InetSocketAddress(serverIP, listeningMessagesPort));
			ThreadReceivePortNewClientConnected();
			ThreadReceiverMessages();
		} catch (SocketException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}

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
						byte[] messageReceivedBytes = new byte[SalaChatInfo.MAX_LENGTH];
						DatagramPacket messageReceivedDatagram = new DatagramPacket(messageReceivedBytes, SalaChatInfo.MAX_LENGTH);
						socketServer.receive(messageReceivedDatagram);
						System.out.println(new String(messageReceivedBytes).trim());
						sendMulticast(messageReceivedBytes);
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
		for (InetSocketAddress inetSocket : clientPorts) {
			try {
				DatagramPacket dp = new DatagramPacket(message, message.length, InetAddress.getByName(inetSocket.getAddress().getHostAddress()), inetSocket.getPort());
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
				try (DatagramSocket socketReceptor = new DatagramSocket(new InetSocketAddress(socketServer.getLocalAddress(), listeningConnectionsPort))) {
					while (true) {
						byte[] msg = new byte[SalaChatInfo.MAX_LENGTH];
						DatagramPacket receivedPacket = new DatagramPacket(msg, msg.length);
						socketReceptor.receive(receivedPacket);

						ObjectInput objectInput = new ObjectInputStream(new ByteArrayInputStream(msg));
						Object o = objectInput.readObject();

						if(o != null && o instanceof Socket) {
							Socket host= (Socket) o;
							System.out.println("Cliente nuevo: " + host.toString());
							Thread.sleep(SalaChatInfo.MAX_LENGTH);
							clientPorts.add(new InetSocketAddress(host.getIp(), host.getPort()));
						}else {
							System.err.println("El valor recibido no es válido.");
						}
						objectInput.close();
					}
				} catch (Exception e) {
					e.printStackTrace();
				}

			}
		});
		t.start();
	}
}
