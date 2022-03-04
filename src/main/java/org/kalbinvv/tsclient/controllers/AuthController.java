package org.kalbinvv.tsclient.controllers;

import java.io.IOException;

import java.net.InetAddress;
import java.net.Socket;
import java.net.URL;
import java.util.ResourceBundle;

import org.kalbinvv.tsclient.Config;
import org.kalbinvv.tsclient.ServerAddress;
import org.kalbinvv.tsclient.TsClient;
import org.kalbinvv.tscore.net.Connection;
import org.kalbinvv.tscore.net.Request;
import org.kalbinvv.tscore.net.RequestType;
import org.kalbinvv.tscore.net.Response;
import org.kalbinvv.tscore.net.ResponseType;
import org.kalbinvv.tscore.user.User;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.TextField;

public class AuthController implements Initializable{

	@FXML
	private TextField loginField;

	@FXML
	private TextField passField;

	@FXML
	private TextField addressField;

	@FXML
	private TextField portField;

	@Override
	public void initialize(URL location, ResourceBundle resources) {
		//
	}

	public void onLogin(ActionEvent event) {
		User user = new User(loginField.getText(), passField.getText());
		authUser(user);
	}

	private void authUser(User user) {
		try {
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
				Alert alert = new Alert(AlertType.INFORMATION);
				alert.setContentText((String)response.getObject());
				alert.showAndWait();
			}
		} catch (NumberFormatException e) {
			Alert alert = new Alert(AlertType.ERROR);
			alert.setContentText("Неправильный формат данных!\n" + e.getMessage());
			alert.showAndWait();
		} catch(IOException e) {
			Alert alert = new Alert(AlertType.ERROR);
			alert.setContentText("Не удалось подключиться!\n" + e.getMessage());
			alert.showAndWait();
		}
	}



}