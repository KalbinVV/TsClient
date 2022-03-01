package org.kalbinvv.tsclient;

import java.io.IOException;
import java.net.Socket;
import java.net.UnknownHostException;

public class ServerAddress {

	private String serverAddress;
	private Integer serverPort;
	
	public ServerAddress(String serverAddress, Integer serverPort){
		this.setServerAddress(serverAddress);
		this.setServerPort(serverPort);
	}

	public String getServerAddress() {
		return serverAddress;
	}

	public void setServerAddress(String serverAddress) {
		this.serverAddress = serverAddress;
	}

	public Integer getServerPort() {
		return serverPort;
	}

	public void setServerPort(Integer serverPort) {
		this.serverPort = serverPort;
	}
	
	public Socket toSocket() throws UnknownHostException, IOException {
		return new Socket(serverAddress, serverPort);
	}
	
	@Override
	public String toString() {
		return serverAddress + ":" + serverPort;
	}
	
}
