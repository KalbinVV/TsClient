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
		drawUserAddInterface();
		try {
			drawChangeUsersAllowedSettingInterface();
		} catch(IOException e) {
			Alert alert = new Alert(AlertType.ERROR);
			alert.setContentText("Не удалость отобразить интерфейс изменения настроек подключения!\n" 
					+ e.getMessage());
			alert.showAndWait();
		}
	}


	private void drawUserAddInterface() {
		Config config = TsClient.getConfig();
		Text addText = new Text("Добавить нового пользователя: ");
		TextField loginField = new TextField();
		loginField.setPromptText("Логин");
		TextField passField = new TextField();
		passField.setPromptText("Пароль");
		Button addUserButton = new Button("Добавить пользователя!");
		addUserButton.setMaxWidth(Double.MAX_VALUE);
		Button addAdminUserButton = new Button("Добавить пользователя с административными правами!");
		addAdminUserButton.setMaxWidth(Double.MAX_VALUE);
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
	}

	private void drawChangeUsersAllowedSettingInterface() throws IOException {
		Config config = TsClient.getConfig();
		Connection connection = new Connection(config.getServerAddress().toSocket());
		Button button = new Button();
		button.setMaxWidth(Double.MAX_VALUE);
		Response anymousUsersAllowedResponse = connection.sendRequestAndGetResponse(new Request(
				RequestType.GetAnonymousUsersAllowedSetting, null, config.getUser()));
		if(anymousUsersAllowedResponse.getType() == ResponseType.Successful) {
			boolean isAnonymousUsersAllowed = (boolean) anymousUsersAllowedResponse.getObject();
			button.setText( (isAnonymousUsersAllowed ? "Запретить" : "Разрешить") 
					+ " подключение анонимным пользователям.");
		}else {
			button.setText("Не удалось получить статус!");
			Alert alert = new Alert(AlertType.ERROR);
			alert.setContentText("Не удалость получить настройки!\n" + 
					(String) anymousUsersAllowedResponse.getObject());
			alert.showAndWait();
		}
		button.setOnAction((ActionEvent event) -> {
			try {
				connection.reconnect();
				Response response = connection.sendRequestAndGetResponse(
						new Request(RequestType.ChangesAnonymousUsersAllowedSetting, null,
								config.getUser()));
				if(response.getType() == ResponseType.Successful) {
					draw();
				}else {
					Alert alert = new Alert(AlertType.ERROR);
					alert.setContentText("Не удалось изменить настройку!\n" 
							+ (String) response.getObject());
					alert.showAndWait();
				}
			} catch (IOException e) {
				Alert alert = new Alert(AlertType.ERROR);
				alert.setContentText("Возникла ошибка при изменение настройки!\n" + e.getMessage());
				alert.showAndWait();
			}
		});
		addNode(button);
	}

}
