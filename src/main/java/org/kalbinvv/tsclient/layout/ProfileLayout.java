package org.kalbinvv.tsclient.layout;

import java.io.IOException;


import org.kalbinvv.tsclient.Config;
import org.kalbinvv.tsclient.TsClient;
import org.kalbinvv.tsclient.controllers.AuthController;
import org.kalbinvv.tscore.net.Connection;
import org.kalbinvv.tscore.net.Request;
import org.kalbinvv.tscore.net.RequestType;
import org.kalbinvv.tscore.user.User;
import org.kalbinvv.tscore.user.UserType;

import animatefx.animation.FadeIn;
import de.jensd.fx.glyphs.fontawesome.FontAwesomeIconView;
import javafx.event.ActionEvent;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.VBox;

public class ProfileLayout extends Layout{

	public ProfileLayout(VBox vBox) {
		super(vBox);
	}

	@Override
	public void draw() {
		clearNodes();
		User user = TsClient.getConfig().getUser();
		FontAwesomeIconView profileIcon = new FontAwesomeIconView();
		profileIcon.setGlyphName("USER");
		profileIcon.setGlyphSize(50);
		Label userNameText = new Label("Имя пользователя: " + user.getName());
		Label userAddressText = new Label("Адрес пользователя: " + user.getAddress());
		Label serverAddressText = new Label("Адрес сервера: " 
				+ TsClient.getConfig().getServerAddress());
		addNode(profileIcon);
		if(user.getType() == UserType.Admin) {
			Label adminLabel = new Label("Администратор");
			addNode(adminLabel);
			FontAwesomeIconView adminIcon = new FontAwesomeIconView();
			adminIcon.setGlyphName("STAR");
			addNode(adminIcon);
		}
		addNode(userNameText);
		addNode(userAddressText);
		addNode(serverAddressText);
		Button leaveButton = new Button("Выйти из аккаунта");
		leaveButton.setOnAction((ActionEvent event) -> {
			sendUserExitRequest();
			TsClient.setRoot("auth.fxml", new AuthController());
			TsClient.setResizable(false);
		});
		addNode(leaveButton);
		new FadeIn(getVBox()).play();
	}
	
	private void sendUserExitRequest() {
		try {
			Config config = TsClient.getConfig();
			Connection connection = new Connection(config.getServerAddress().toSocket());
			connection.sendRequest(new Request(RequestType.UserExit, config.getUser()));
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

}
