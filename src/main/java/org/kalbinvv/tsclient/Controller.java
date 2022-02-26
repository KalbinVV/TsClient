package org.kalbinvv.tsclient;

import java.io.IOException;
import java.io.ObjectOutputStream;
import java.net.InetAddress;
import java.net.Socket;
import java.net.URL;
import java.net.UnknownHostException;
import java.util.ResourceBundle;

import org.kalbinvv.tscore.net.Connection;
import org.kalbinvv.tscore.net.Request;
import org.kalbinvv.tscore.net.RequestType;
import org.kalbinvv.tscore.net.Response;
import org.kalbinvv.tscore.net.ResponseType;
import org.kalbinvv.tscore.user.AnonymousUser;
import org.kalbinvv.tscore.user.AuthorisedUser;
import org.kalbinvv.tscore.user.User;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.TextField;

public class Controller implements Initializable{

	@FXML
	private TextField loginField;
	
	@FXML
	private TextField passField;
	
	@Override
	public void initialize(URL location, ResourceBundle resources) {
		//
	}
	
	public void loginUser(ActionEvent event) {
		User user = new AuthorisedUser(loginField.getText(), passField.getText());
		try {
			user.setAddress(InetAddress.getLocalHost());
		} catch (UnknownHostException e) {
			e.printStackTrace();
		}
		Socket socket = null;
		try {
			socket = new Socket("localhost", 2090);
		} catch (IOException e) {
			e.printStackTrace();
		}
		Connection connection = new Connection(socket);
		Response response = connection.sendRequestAndGetResponse(
				new Request(RequestType.UserConnect, user));
		Alert alert = new Alert(AlertType.INFORMATION);
		if(response.getResponseType() == ResponseType.SuccessfulConnect) {
			alert.setContentText("Успешный вход!");
		}else {
			alert.setContentText((String)response.getObject());
		}
		alert.showAndWait();
	}
	
	public void anonymLogin(ActionEvent event) {
		User user = new AnonymousUser(loginField.getText());
		try {
			user.setAddress(InetAddress.getLocalHost());
		} catch (UnknownHostException e) {
			e.printStackTrace();
		}
		Socket socket = null;
		try {
			socket = new Socket("localhost", 2090);
		} catch (IOException e) {
			e.printStackTrace();
		}
		Connection connection = new Connection(socket);
		Response response = connection.sendRequestAndGetResponse(
				new Request(RequestType.UserConnect, user));
		Alert alert = new Alert(AlertType.INFORMATION);
		if(response.getResponseType() == ResponseType.SuccessfulConnect) {
			alert.setContentText("Успешный вход!");
		}else {
			alert.setContentText((String)response.getObject());
		}
		alert.showAndWait();
	}
	
	

}
