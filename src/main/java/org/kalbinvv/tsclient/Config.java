package org.kalbinvv.tsclient;

import java.net.InetAddress;

import org.kalbinvv.tscore.user.User;

public class Config {

	private User user;
	private InetAddress serverAddress;
	
	public User getUser() {
		return user;
	}
	
	public void setUser(User user) {
		this.user = user;
	}
	
	public InetAddress getServerAddress() {
		return serverAddress;
	}
	
	public void setServerAddress(InetAddress serverAddress) {
		this.serverAddress = serverAddress;
	}
	
}
