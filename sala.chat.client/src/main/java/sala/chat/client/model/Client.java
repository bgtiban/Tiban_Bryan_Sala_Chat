package sala.chat.client.model;

import java.awt.EventQueue;
import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.InetSocketAddress;

import javax.sound.sampled.Line;
import javax.swing.JFrame;
import javax.swing.JTextArea;

/**
 * En esta clase se trabaja sobre UDP, es capaz de recibir  y enviar los mensajes provenientes del servidor y mostrarlos por pantalla (la pantalla de la clase JFClient).
 * tambi�n se env�a el puerto actual.
 * @author Bryan Ti
 *
 */
public class Client
{
	private DatagramSocket socket;
	private DatagramPacket packetReceiver, packetSender;
	private byte[] msgReceiver;
	private final int MAX_LENGTH = 2000;
	
	/**
	 * Este m�todo instancia un DatagramSocket, mediante el cual se van a recibir los mensajes provenientes del servidor, el puerto de este datagramSocket
	 * es asignado por el S.O.
	 * Se llama al m�todo sendPort() para enviar el puerto asignado por el S.O.
	 * @throws IOException
	 */
	public Client() throws IOException
	{
		//puerto 0 = El S.O asigna un puerto libre.
		socket = new DatagramSocket(new InetSocketAddress("localhost", 0));
		sendPORT();
		msgReceiver = new byte[MAX_LENGTH];
		packetReceiver = new DatagramPacket(msgReceiver,MAX_LENGTH);
		
	}
	
	/**
	 * Este m�todo inicia un hilo el cual se encarga de enviar los mensajes creados por el usuario.
	 * @param message, mensaje a enviar.
	 * @throws IOException
	 */
	public void sendMessage(String message) throws IOException
	{
		Thread t = new Thread(new Runnable() {
			
			@Override
			public void run() {
				try {
					packetSender = new DatagramPacket(message.getBytes(), message.getBytes().length, InetAddress.getByName("localhost"), 5555);
					socket.send(packetSender);
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
		});
		t.start();
		
	}
	
	/**
	 * M�todo el cual recibe mensajes provenientes del servidor y los muestra a trav�s de la interfaz.
	 * @param area, corresponde con el JTextArea por el cual se mostrar� el mensaje recibido por el ervidor.
	 */
	public void receiverMessages(JTextArea area)
	{
	
		while(true) {
			try {
				msgReceiver = new byte[MAX_LENGTH];
				packetReceiver = new DatagramPacket(msgReceiver,MAX_LENGTH);
				socket.receive(packetReceiver);
				EventQueue.invokeAndWait(new Runnable() {
					@Override
					public void run() {
						area.setText(area.getText() + new String(msgReceiver).trim() + "\n");
					}
				});
				
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (InterruptedException ie) {
				ie.printStackTrace();
			} catch (InvocationTargetException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
			
	}
	
	/**
	 * M�todo que envia puertos mediante un nuevo socket2, este socket2 es temporal y s�lo se encarga de enviar el puerto del socket1 por el cual se env�a y recibe
	 * los mensjaes, es decir la variable llamada "socket".
	 * @throws IOException
	 */
	private void sendPORT() throws IOException
	{
		DatagramSocket socket2 = new DatagramSocket();
		String message = String.valueOf(socket.getLocalPort());
		DatagramPacket packetSender2 = new DatagramPacket(message.getBytes(), message.getBytes().length, InetAddress.getByName("localhost"), 1234);
		socket.send(packetSender2);
		socket2.close();
	}
}
