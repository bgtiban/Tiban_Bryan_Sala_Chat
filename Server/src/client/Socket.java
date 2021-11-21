package client;

import java.io.Serializable;

public class Socket implements Serializable {


	/**
	 *
	 */
	private static final long serialVersionUID = 1L;


	private String ip;
	private Integer port;

	public Socket(String ip, Integer port) {
		super();
		this.ip = ip;
		this.port = port;
	}

	public String getIp() {
		return ip;
	}
	public void setIp(String ip) {
		this.ip = ip;
	}
	public Integer getPort() {
		return port;
	}
	public void setPort(Integer port) {
		this.port = port;
	}
	@Override
	public String toString() {
		return "Socket = " + ip + ":" + port;
	}

}
