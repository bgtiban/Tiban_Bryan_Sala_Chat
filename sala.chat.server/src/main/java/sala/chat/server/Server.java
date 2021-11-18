package sala.chat.server;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.net.SocketException;
import java.net.UnknownHostException;
import java.util.HashSet;
import java.util.Iterator;

import javax.swing.plaf.basic.BasicInternalFrameTitlePane.MaximizeAction;

/**
 * En esta clase se trabaja con UDP, existen en el servidor dos sockets que reciben paquetes,
 * un socket recibe un paquete con únicamente el nº de puerto del cliente, y otro socket recibe un paquete con el mensaje de texto enviado por el cliente.
 * este servidor muestra por consola mensajes los cuales informan de los clientes que se han conectado y los mensjaes que se est�n transmitiendo. 
 * @author Bryan Ti
 *
 */
public class Server
{
	private byte[] message;
	private final int MAX_LENGTH = 150;
	private DatagramSocket socket;
	private DatagramPacket packet;
	private HashSet<Integer> hashPorts;
	
	
	/**
	 * M�todo que instancia una lista en la cual se almacenan los puertos de los clientes para poder realizar un "mulsticast" de un mensaje entrante.
	 * Tambi�n se instancia el socket mediante el cual se van a recibir mensajes y se crea y activa un hilo para recibir los puertos.
	 * Tambi�n se inicia el hilo mediante el cual se van a recibir los mensajes.
	 * @param host, es el nombre o IP que recibir� el servidor (para recibir mensajes).
	 * @param port, es el puerto por el que escuchar� el servidor (para recibir mensajes).
	 * @throws SocketException
	 * @throws IOException
	 */
	public Server(String host, int port) throws SocketException, IOException
	{
		hashPorts = new HashSet<>();
		message = new byte[MAX_LENGTH];
		socket = new DatagramSocket(new InetSocketAddress(host, port));
		receiverPORT();//Hilo que recibe puertos
		packet = new  DatagramPacket(message, MAX_LENGTH);
		receiverMessages();//Hilo que recibe mensajes
	}
	
	/**
	 * M�todo que crea un hilo el cual contiene un bucle que se encarga de recibir mensajes de los clientes, 
	 * en cuanto recibe un mensjae lo imprime por consola y lo reenv�a a todos los clientes conectados mediante el m�todo llamado send().
	 * @throws IOException
	 */
	private void receiverMessages() throws IOException
	{
		Thread t = new Thread(new Runnable() {
			
			@Override
			public void run() {
				while(true)
				{
					try {
					message = new byte[MAX_LENGTH];
					packet = new  DatagramPacket(message, MAX_LENGTH);
					socket.receive(packet);
					System.out.println("Un cliente dice: " + getMessage());
					send();
					}catch(Exception e)
					{
						e.printStackTrace();
					}
				}
			}
		});
		t.start();
	}
	
	/**
	 * M�todo que retorna el mensaje del cliente en String.
	 * @return, String  que corresponde con el mensaje del cliente.
	 */
	private String getMessage()
	{
		return new String(message).trim();
	}
	
	
	/**
	 * Este m�todo recorre una lista que contiene los puertos de los clientes conectados.
	 * se crea un datagrama nuevo por cada reenv�o.
	 * @throws UnknownHostException
	 */
	private void send() throws UnknownHostException
	{
		for (Iterator<Integer> iterator = hashPorts.iterator(); iterator.hasNext();) {
		    Integer port = iterator.next();
		    DatagramPacket dp = new DatagramPacket(getMessage().getBytes(), getMessage().getBytes().length, InetAddress.getByName("localhost"), port);
		    try {
				socket.send(dp);
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}

		
	}
	
	/**
	 * Este m�todo inicia un hilo el cual recibe el puerto del cliente conectado.
	 */
	private void receiverPORT()
	{
		Thread t = new Thread(new Runnable() {
			
			@Override
			public void run() {
					try {
						DatagramSocket socket2 = new DatagramSocket(new InetSocketAddress("localhost", 1234));
						byte[] msg = new byte[10];
						DatagramPacket packet2 = new DatagramPacket(msg, 10);
						while(true)
						{
							socket2.receive(packet2);
							
							System.out.println("Cliente nuevo con puerto: " + new String(msg).trim());
							Thread.sleep(2000);
							hashPorts.add(Integer.parseInt(new String(msg).trim()));	
							
						}
				}catch(Exception e){
					e.printStackTrace();
				}
				
			}
		});
		t.start();
	}
}









