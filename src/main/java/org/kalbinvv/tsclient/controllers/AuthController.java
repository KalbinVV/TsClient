package org.kalbinvv.tsclient.controllers;

import java.io.IOException;



import java.net.InetAddress;
import java.net.Socket;
import java.net.URL;
import java.util.ResourceBundle;

import org.kalbinvv.tsclient.Config;
import org.kalbinvv.tsclient.ServerAddress;
import org.kalbinvv.tsclient.TsClient;
import org.kalbinvv.tsclient.alert.AlertError;
import org.kalbinvv.tscore.net.Connection;
import org.kalbinvv.tscore.net.Request;
import org.kalbinvv.tscore.net.RequestType;
import org.kalbinvv.tscore.net.Response;
import org.kalbinvv.tscore.net.ResponseType;
import org.kalbinvv.tscore.security.Utils;
import org.kalbinvv.tscore.user.User;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;

public class AuthController implements Initializable{

	@FXML
	private TextField loginField;

	@FXML
	private PasswordField passField;

	@FXML
	private TextField addressField;

	@FXML
	private TextField portField;

	@Override
	public void initialize(URL location, ResourceBundle resources) {
		//
	}

	public void onLogin(ActionEvent event) {
		User user;
		try {
			user = new User(loginField.getText(), 
					Utils.convertToSHA256(passField.getText()));
			authUser(user);
		} catch (IOException e) {
			new AlertError("Не удалось авторизироваться!", e.getMessage());
		}
	}
	
	private void authUser(User user) throws IOException {
		String addressStr = addressField.getText().isEmpty() ? "localhost" 
				: addressField.getText();
		String portStr = portField.getText().isEmpty() ? "2090" : portField.getText();
		ServerAddress serverAddress = new ServerAddress(addressStr, 
				Integer.parseInt(portStr));
		user.setAddress(InetAddress.getLocalHost());
		Socket socket = new Socket(serverAddress.getServerAddress(), 
				serverAddress.getServerPort());
		Connection connection = new Connection(socket);
		Response response = connection.sendRequestAndGetResponse(
				new Request(RequestType.UserConnect, user));
		if(response.getType() == ResponseType.Successful) {
			Config config = TsClient.getConfig();
			config.setUser((User)response.getObject());
			config.getUser().setAddress(InetAddress.getLocalHost());
			config.setServerAddress(serverAddress);
			TsClient.setResizable(true);
			TsClient.setRoot("primary.fxml", new PrimaryController());
		}else {
			throw new IOException((String) response.getObject());
		}
	}



}