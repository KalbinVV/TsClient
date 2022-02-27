package org.kalbinvv.tsclient;

public class ServerAddress {

	private String serverAddress;
	private Integer serverPort;
	
	ServerAddress(String serverAddress, Integer serverPort){
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
	
}
