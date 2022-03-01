package org.kalbinvv.tsclient.controllers;

import java.net.URL;
import java.util.ResourceBundle;

import org.kalbinvv.tsclient.TsClient;
import org.kalbinvv.tsclient.layout.*;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.layout.VBox;
import javafx.scene.text.Text;

public class PrimaryController implements Initializable{

	@FXML
	private Text loginNameField;

	@FXML
	private VBox mainBox;

	private Layout profileLayout;
	private Layout testsLayout;
	private Layout adminLayout;

	@Override
	public void initialize(URL location, ResourceBundle resources) {
		loginNameField.setText(TsClient.getConfig().getUser().getName());
		profileLayout = new ProfileLayout(mainBox);
		testsLayout = new TestsLayout(mainBox);
		adminLayout = new AdminLayout(mainBox);
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

}
