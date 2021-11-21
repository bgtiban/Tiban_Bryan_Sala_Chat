package client;

import java.io.Serializable;

public class ServerSocket implements Serializable {

	/**
	 *
	 */
	private static final long serialVersionUID = -6200873504172925351L;


	private Socket socket;
	private Integer listeningNewConnectiosPort;

	public ServerSocket(Socket socket, Integer listeningNewConnectiosPort) {
		super();
		this.socket = socket;
		this.listeningNewConnectiosPort = listeningNewConnectiosPort;
	}

	public Socket getSocket() {
		return socket;
	}
	public void setSocket(Socket socket) {
		this.socket = socket;
	}
	public Integer getListeningNewConnectios() {
		return listeningNewConnectiosPort;
	}
	public void setListeningNewConnectios(Integer listeningNewConnectios) {
		this.listeningNewConnectiosPort = listeningNewConnectios;
	}

	@Override
	public String toString() {
		return "Server [socket=" + socket + ", listeningNewConnectiosPort=" + listeningNewConnectiosPort + "]";
	}


}
