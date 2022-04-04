package org.kalbinvv.tsclient;

import java.io.IOException;
import java.net.Socket;
import java.net.UnknownHostException;

public class ServerAddress {

	private final String serverAddress;
	private final Integer serverPort;
	
	public ServerAddress(String serverAddress, Integer serverPort){
		this.serverAddress = serverAddress;;
		this.serverPort = serverPort;
	}

	public String getServerAddress() {
		return serverAddress;
	}

	public Integer getServerPort() {
		return serverPort;
	}
	public Socket toSocket() throws UnknownHostException, IOException {
		return new Socket(serverAddress, serverPort);
	}
	
	@Override
	public String toString() {
		return serverAddress + ":" + serverPort;
	}
	
}
