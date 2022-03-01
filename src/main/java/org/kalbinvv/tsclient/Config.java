package org.kalbinvv.tsclient;

import org.kalbinvv.tscore.user.User;

public class Config {

	private User user;
	private ServerAddress serverAddress;
	
	public User getUser() {
		return user;
	}
	
	public void setUser(User user) {
		this.user = user;
	}
	
	public ServerAddress getServerAddress() {
		return serverAddress;
	}
	
	public void setServerAddress(ServerAddress serverAddress) {
		this.serverAddress = serverAddress;
	}
	
}
