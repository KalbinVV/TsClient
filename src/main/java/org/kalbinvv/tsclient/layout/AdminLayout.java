package org.kalbinvv.tsclient.layout;

import java.io.IOException;


import org.kalbinvv.tsclient.Config;
import org.kalbinvv.tsclient.TsClient;
import org.kalbinvv.tscore.net.Connection;
import org.kalbinvv.tscore.net.Request;
import org.kalbinvv.tscore.net.RequestType;
import org.kalbinvv.tscore.net.Response;
import org.kalbinvv.tscore.net.ResponseType;
import org.kalbinvv.tscore.user.UserEntry;

import javafx.event.ActionEvent;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.layout.VBox;
import javafx.scene.text.Text;

public class AdminLayout extends Layout{

	public AdminLayout(VBox vBox) {
		super(vBox);
	}

	@Override
	public void draw() {
		clearNodes();
		Text addText = new Text("Добавить нового пользователя: ");
		TextField loginField = new TextField();
		loginField.setPromptText("Логин");
		TextField passField = new TextField();
		passField.setPromptText("Пароль");
		Button addUserButton = new Button("Добавить пользователя!");
		Button addAdminUserButton = new Button("Добавить пользователя с административными правами!");
		addUserButton.setOnAction((ActionEvent event) -> {
			UserEntry userEntry = new UserEntry(loginField.getText(), passField.getText());
			try {
				Config config = TsClient.getConfig();
				Connection connection = new Connection(config.getServerAddress()
						.toSocket());
				Response response = connection.sendRequestAndGetResponse(
						new Request(RequestType.AddUser, userEntry, 
						config.getUser()));
				if(response.getType() == ResponseType.Successful) {
					Alert alert = new Alert(AlertType.INFORMATION);
					alert.setContentText("Пользователь успешно добавлен!");
					alert.showAndWait();
				}else {
					Alert alert = new Alert(AlertType.ERROR);
					alert.setContentText("Не удалось добавить пользователя!\n"
							+ (String)response.getObject());
					alert.showAndWait();
				}
			} catch (IOException e) {
				String errorMsg = e.getMessage();
				Alert alert = new Alert(AlertType.ERROR);
				alert.setContentText("Не удалось добавить пользователя!\n" + errorMsg);
				alert.showAndWait();
			}
		});
		addAdminUserButton.setOnAction((ActionEvent event) -> {
			UserEntry userEntry = new UserEntry(loginField.getText(), passField.getText());
			try {
				Config config = TsClient.getConfig();
				Connection connection = new Connection(config.getServerAddress()
						.toSocket());
				Response response = connection.sendRequestAndGetResponse(
						new Request(RequestType.AddAdminUser, userEntry, 
						config.getUser()));
				if(response.getType() == ResponseType.Successful) {
					Alert alert = new Alert(AlertType.INFORMATION);
					alert.setContentText("Пользователь успешно добавлен!");
					alert.showAndWait();
				}else {
					Alert alert = new Alert(AlertType.ERROR);
					alert.setContentText("Не удалось добавить пользователя!\n"
							+ (String)response.getObject());
					alert.showAndWait();
				}
			} catch (IOException e) {
				String errorMsg = e.getMessage();
				Alert alert = new Alert(AlertType.ERROR);
				alert.setContentText("Не удалось добавить пользователя!\n" + errorMsg);
				alert.showAndWait();
			}
		});
		addNode(addText);
		addNode(loginField);
		addNode(passField);
		addNode(addUserButton);
		addNode(addAdminUserButton);
	}
	

}
