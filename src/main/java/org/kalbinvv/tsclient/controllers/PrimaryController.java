package org.kalbinvv.tsclient.controllers;

import java.io.IOException;

import java.net.URL;
import java.util.ResourceBundle;

import org.kalbinvv.tsclient.Config;
import org.kalbinvv.tsclient.TsClient;
import org.kalbinvv.tsclient.layout.*;
import org.kalbinvv.tscore.net.Connection;
import org.kalbinvv.tscore.net.Request;
import org.kalbinvv.tscore.net.RequestType;
import org.kalbinvv.tscore.user.UserType;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.layout.VBox;
import javafx.stage.WindowEvent;

public class PrimaryController implements Initializable{
	
	@FXML
	private VBox mainBox;
	
	@FXML
	private Button adminButton;
	
	@FXML
	private Button usersButton;
	
	@FXML
	private Button logsButton;

	private Layout profileLayout;
	private Layout testsLayout;
	private Layout adminLayout;
	private Layout usersLayout;
	private Layout logsLayout;
	private Layout settingsLayout;

	@Override
	public void initialize(URL location, ResourceBundle resources) {
		if(TsClient.getConfig().getUser().getType() != UserType.Admin) {
			adminButton.setVisible(false);
			usersButton.setVisible(false);
			logsButton.setVisible(false);
		}
		TsClient.getStage().setOnCloseRequest((WindowEvent event) -> sendUserExitRequest());
		profileLayout = new ProfileLayout(mainBox);
		testsLayout = new TestsLayout(mainBox);
		adminLayout = new AdminLayout(mainBox);
		usersLayout = new UsersLayout(mainBox);
		logsLayout = new LogsLayout(mainBox);
		settingsLayout = new SettingsLayout(mainBox);
		profileLayout.draw();
	}

	public void onProfileButton(ActionEvent event) {
		profileLayout.draw();
	}

	public void onTestsButton(ActionEvent event) {
		testsLayout.draw();
	}

	public void onSettingsButton(ActionEvent event) {
		settingsLayout.draw();
	}

	public void onAdminButton(ActionEvent event) {
		adminLayout.draw();
	}
	
	public void onUsersButton(ActionEvent event) {
		usersLayout.draw();
	}
	
	public void onQuitButton(ActionEvent event) {
		sendUserExitRequest();
		TsClient.setRoot("auth.fxml", new AuthController());
		TsClient.setResizable(false);
	}
	
	public void onLogsButton(ActionEvent event) {
		logsLayout.draw();
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
