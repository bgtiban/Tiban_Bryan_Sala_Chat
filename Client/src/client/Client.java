package client;

import java.awt.EventQueue;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.ObjectOutputStream;
import java.lang.reflect.InvocationTargetException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.net.SocketException;
import java.net.UnknownHostException;

import javax.swing.JTextArea;


/**
 * En esta clase se trabaja sobre UDP, es capaz de recibir y enviar los mensajes
 * provenientes del servidor y mostrarlos por pantalla (la pantalla de la clase
 * JFClient). también se env�a el puerto actual.
 *
 * @author Bryan Ti
 *
 */
public class Client {
	private DatagramSocket socketClient;
	private DatagramPacket packetReceiver, packetSender;
	private ServerSocket server;
	private byte[] msgReceived;

	/**
	 * Este método instancia un DatagramSocket, mediante el cual se van a recibir
	 * los mensajes provenientes del servidor, el puerto de este datagramSocket es
	 * asignado por el S.O. Se llama al m�todo sendPort() para enviar el puerto
	 * asignado por el S.O.
	 *
	 * @throws IOException
	 */
	public Client(ServerSocket server) throws IOException {
		this.server = server;

		socketClient = new DatagramSocket(new InetSocketAddress(getIp(), 0));
		sendMyHostToServer();
		msgReceived = new byte[SalaChatInfo.MAX_LENGTH];
		packetReceiver = new DatagramPacket(msgReceived, SalaChatInfo.MAX_LENGTH);

	}

	private String getIp() throws UnknownHostException, SocketException {
		String ip = null;

		try(final DatagramSocket socket = new DatagramSocket()){
		  socket.connect(InetAddress.getByName("8.8.8.8"), 10002);
		  ip = socket.getLocalAddress().getHostAddress();
		}

		return ip;
	}

	/**
	 * Este método inicia un hilo el cual se encarga de enviar los mensajes creados
	 * por el usuario.
	 *
	 * @param message, mensaje a enviar.
	 * @throws IOException
	 */
	public void sendMessage(String message) throws IOException {
		Thread t = new Thread(new Runnable() {

			@Override
			public void run() {
				try {
					packetSender = new DatagramPacket(message.getBytes(), message.getBytes().length,
							InetAddress.getByName(server.getSocket().getIp()), server.getSocket().getPort());
					socketClient.send(packetSender);
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		});
		t.start();

	}

	/**
	 * M�todo el cual recibe mensajes provenientes del servidor y los muestra a
	 * trav�s de la interfaz.
	 *
	 * @param area, corresponde con el JTextArea por el cual se mostrar� el mensaje
	 *        recibido por el ervidor.
	 */
	public void receiverMessages(JTextArea area) {

		while (true) {
			try {
				msgReceived = new byte[SalaChatInfo.MAX_LENGTH];
				packetReceiver = new DatagramPacket(msgReceived, SalaChatInfo.MAX_LENGTH);
				socketClient.receive(packetReceiver);
				EventQueue.invokeAndWait(new Runnable() {
					@Override
					public void run() {
						area.setText(area.getText() + new String(msgReceived).trim() + "\n");
					}
				});

			} catch (IOException e) {
				e.printStackTrace();
			} catch (InterruptedException ie) {
				ie.printStackTrace();
			} catch (InvocationTargetException e) {
				e.printStackTrace();
			}
		}

	}

	/**
	 * Envíar mensaje
	 *
	 * @throws IOException
	 */
	private void sendMyHostToServer() throws IOException {
		Socket me = new Socket(socketClient.getLocalAddress().getHostAddress(), socketClient.getLocalPort());

		ByteArrayOutputStream meBytesOS = new ByteArrayOutputStream(6400);
		ObjectOutputStream oos = new ObjectOutputStream(meBytesOS);
		oos.writeObject(me);
		oos.flush();

		DatagramPacket datagramPacket = new DatagramPacket(
				meBytesOS.toByteArray(), meBytesOS.toByteArray().length, InetAddress.getByName(server.getSocket().getIp()), server.getListeningNewConnectios());
		socketClient.send(datagramPacket);

		oos.close();
	}
}
