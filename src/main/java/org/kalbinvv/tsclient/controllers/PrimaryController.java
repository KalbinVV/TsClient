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

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.layout.VBox;
import javafx.scene.text.Text;
import javafx.stage.WindowEvent;

public class PrimaryController implements Initializable{

	@FXML
	private Text loginNameField;

	@FXML
	private VBox mainBox;

	private Layout profileLayout;
	private Layout testsLayout;
	private Layout adminLayout;
	private Layout usersLayout;

	@Override
	public void initialize(URL location, ResourceBundle resources) {
		TsClient.getStage().setOnCloseRequest((WindowEvent event) -> {
			try {
				Config config = TsClient.getConfig();
				Connection connection = new Connection(config.getServerAddress().toSocket());
				connection.sendRequest(new Request(RequestType.UserExit, config.getUser()));
			} catch (IOException e) {
				e.printStackTrace();
			}
		});
		loginNameField.setText(TsClient.getConfig().getUser().getName());
		profileLayout = new ProfileLayout(mainBox);
		testsLayout = new TestsLayout(mainBox);
		adminLayout = new AdminLayout(mainBox);
		usersLayout = new UsersLayout(mainBox);
		profileLayout.draw();
	}

	public void onProfileButton(ActionEvent event) {
		profileLayout.draw();
	}

	public void onTestsButton(ActionEvent event) {
		testsLayout.draw();
	}

	public void onSettingsButton(ActionEvent event) {
		//
	}

	public void onAdminButton(ActionEvent event) {
		adminLayout.draw();
	}
	
	public void onUsersButton(ActionEvent event) {
		usersLayout.draw();
	}

}
