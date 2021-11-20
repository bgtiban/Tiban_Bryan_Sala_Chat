package sala.chat.client.model;

import java.awt.EventQueue;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.ObjectOutput;
import java.io.ObjectOutputStream;
import java.io.OutputStream;
import java.lang.reflect.InvocationTargetException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.InetSocketAddress;

import javax.swing.JTextArea;

import sala.chat.comunes.Host;
import sala.chat.comunes.constants.SalaChatInfo;

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
	private byte[] msgReceiver;

	/**
	 * Este método instancia un DatagramSocket, mediante el cual se van a recibir
	 * los mensajes provenientes del servidor, el puerto de este datagramSocket es
	 * asignado por el S.O. Se llama al m�todo sendPort() para enviar el puerto
	 * asignado por el S.O.
	 *
	 * @throws IOException
	 */
	public Client() throws IOException {
		// puerto 0 = El S.O asigna un puerto libre.
		socketClient = new DatagramSocket(new InetSocketAddress(SalaChatInfo.LOCALHOST, SalaChatInfo.FREE_PORT));
		sendMyHostToServer();
		msgReceiver = new byte[SalaChatInfo.MAX_LENGTH];
		packetReceiver = new DatagramPacket(msgReceiver, SalaChatInfo.MAX_LENGTH);

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
							InetAddress.getByName(SalaChatInfo.SERVER_HOST), SalaChatInfo.SERVER_PORT);
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
				msgReceiver = new byte[SalaChatInfo.MAX_LENGTH];
				packetReceiver = new DatagramPacket(msgReceiver, SalaChatInfo.MAX_LENGTH);
				socketClient.receive(packetReceiver);
				EventQueue.invokeAndWait(new Runnable() {
					@Override
					public void run() {
						area.setText(area.getText() + new String(msgReceiver).trim() + "\n");
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
		Host me = new Host();
		me.setIp(socketClient.getLocalAddress().getHostAddress());
		me.setPort(socketClient.getLocalPort());

		ByteArrayOutputStream meBytesOS = new ByteArrayOutputStream(6400);
		ObjectOutputStream oos = new ObjectOutputStream(meBytesOS);
		oos.writeObject(me);
		oos.flush();

		DatagramPacket datagramPacket = new DatagramPacket(meBytesOS.toByteArray(), meBytesOS.toByteArray().length, InetAddress.getByName(SalaChatInfo.SERVER_HOST), SalaChatInfo.LISTENING_PORT_NEW_CONECTIONS);
		socketClient.send(datagramPacket);

		oos.close();
	}
}
