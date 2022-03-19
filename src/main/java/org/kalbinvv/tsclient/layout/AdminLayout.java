package org.kalbinvv.tsclient.layout;

import java.io.IOException;

import org.kalbinvv.tsclient.Config;
import org.kalbinvv.tsclient.TsClient;
import org.kalbinvv.tsclient.alert.AlertError;
import org.kalbinvv.tsclient.alert.AlertInformation;
import org.kalbinvv.tscore.net.Connection;
import org.kalbinvv.tscore.net.Request;
import org.kalbinvv.tscore.net.RequestType;
import org.kalbinvv.tscore.net.Response;
import org.kalbinvv.tscore.net.ResponseType;
import org.kalbinvv.tscore.security.Utils;
import org.kalbinvv.tscore.user.UserEntry;

import javafx.event.ActionEvent;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
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
			new AlertError("Ошибка отображения!", e.getMessage());
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
				if(loginField.getText().isEmpty()) {
					throw new IOException("Имя пользователя не может быть пустым!");
				}
				if(passField.getText().isEmpty()) {
					throw new IOException("Пароль пользователя не может быть пустым!");
				}
				Connection connection = new Connection(config.getServerAddress().toSocket());
				UserEntry userEntry = new UserEntry(loginField.getText(), 
						Utils.convertToSHA256(passField.getText()));
				Response response = connection.sendRequestAndGetResponse(
						new Request(RequestType.AddUser, userEntry, 
								config.getUser()));
				if(response.getType() == ResponseType.Successful) {
					new AlertInformation("Пользователь успешно добавлен!");
				}else {
					new AlertError("Не удалость добавить пользователя!", 
							(String) response.getObject());
				}
			}catch(IOException e) {
				new AlertError("Не удалось добавить пользователя!", e.getMessage());
			}
		});
		addAdminUserButton.setOnAction((ActionEvent event) -> {
			try {
				if(loginField.getText().isEmpty()) {
					throw new IOException("Имя пользователя не может быть пустым!");
				}
				if(passField.getText().isEmpty()) {
					throw new IOException("Пароль пользователя не может быть пустым!");
				}
				Connection connection = new Connection(config.getServerAddress().toSocket());
				UserEntry userEntry = new UserEntry(loginField.getText(), 
						Utils.convertToSHA256(passField.getText()));
				Response response = connection.sendRequestAndGetResponse(
						new Request(RequestType.AddAdminUser, userEntry, 
								config.getUser()));
				if(response.getType() == ResponseType.Successful) {
					new AlertInformation("Пользователь успешно добавлен!");
				}else {
					new AlertError("Не удалость добавить пользователя!", 
							(String) response.getObject());
				}
			}catch(IOException e) {
				new AlertError("Не удалось добавить пользователя!", e.getMessage());
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
			new AlertError("Не удалось получить настройки!", 
					(String) anymousUsersAllowedResponse.getObject());
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
					new AlertError("Не удалось получить настройки!", (String) response.getObject());
				}
			} catch (IOException e) {
				new AlertError("Не удалость изменить настройки!", e.getMessage());
			}
		});
		addNode(button);
	}

}
