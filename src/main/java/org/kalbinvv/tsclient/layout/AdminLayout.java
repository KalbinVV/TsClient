package org.kalbinvv.tsclient.layout;

import java.io.IOException;
import java.net.UnknownHostException;

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
		Config config = TsClient.getConfig();
		Text addText = new Text("Добавить нового пользователя: ");
		TextField loginField = new TextField();
		loginField.setPromptText("Логин");
		TextField passField = new TextField();
		passField.setPromptText("Пароль");
		Button addUserButton = new Button("Добавить пользователя!");
		Button addAdminUserButton = new Button("Добавить пользователя с административными правами!");
		Button changeAnonymousUsersSettingButton = new Button();
		try {
			Connection connection = new Connection(config.getServerAddress().toSocket());
			Response anonymousUsersResponse = connection.sendRequestAndGetResponse(new Request(RequestType.GetAnonymousUsersAllowedSetting, 
					null, 
					config.getUser()));
			if(anonymousUsersResponse.getType() == ResponseType.Successful) {
				boolean isAnonymousUsersAllowed = (boolean) anonymousUsersResponse.getObject();
				if(isAnonymousUsersAllowed) {
					changeAnonymousUsersSettingButton.setText(
							"Запретить подключение анонимным пользователям!");
				}else {
					changeAnonymousUsersSettingButton.setText(
							"Разрешить подключение анонимным пользователям!");
				}
				changeAnonymousUsersSettingButton.setOnAction((ActionEvent event) -> {
					Response response = connection.sendRequestAndGetResponse(new Request(
							RequestType.ChangesAnonymousUsersAllowedSetting, null, config.getUser()));
					if(response.getType() == ResponseType.Successful) {
						Alert alert = new Alert(AlertType.INFORMATION);
						alert.setContentText("Настройка успешно изменена!");
						alert.showAndWait();
					}else {
						Alert alert = new Alert(AlertType.ERROR);
						alert.setContentText("Не удалить изменить настройки!\n" 
								+ (String) response.getObject());
						alert.showAndWait();
					}
				});
			}else {
				String errorMsg = (String) anonymousUsersResponse.getObject();
				Alert alert = new Alert(AlertType.ERROR);
				alert.setContentText("Не удалось получить настройки!\n" + errorMsg);
				alert.showAndWait();
			}
		} catch (IOException ex) {
			Alert alert = new Alert(AlertType.ERROR);
			alert.setContentText("Не удалось получить настройки!\n" + ex.getMessage());
			alert.showAndWait();
		}
		addUserButton.setOnAction((ActionEvent event) -> {
			try {
				Connection connection = new Connection(config.getServerAddress().toSocket());
				UserEntry userEntry = new UserEntry(loginField.getText(), passField.getText());
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
			}catch(IOException ex) {
				Alert alert = new Alert(AlertType.ERROR);
				alert.setContentText("Не удалось добавить пользователя!\n" + ex.getMessage());
				alert.showAndWait();
			}
		});
		addAdminUserButton.setOnAction((ActionEvent event) -> {
			try {
				Connection connection = new Connection(config.getServerAddress().toSocket());
				UserEntry userEntry = new UserEntry(loginField.getText(), passField.getText());
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
			}catch(IOException ex) {
				Alert alert = new Alert(AlertType.ERROR);
				alert.setContentText("Не удалось добавить пользователя!\n" + ex.getMessage());
				alert.showAndWait();
			}
		});
		addNode(addText);
		addNode(loginField);
		addNode(passField);
		addNode(addUserButton);
		addNode(addAdminUserButton);
		addNode(changeAnonymousUsersSettingButton);
	}


}
