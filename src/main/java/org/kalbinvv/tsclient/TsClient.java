package org.kalbinvv.tsclient;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;

import org.kalbinvv.tsclient.controllers.AuthController;


public class TsClient extends Application {

	private static Stage stage;
	private static Config config;

	@Override
	public void start(Stage stage) throws IOException {
		TsClient.stage = stage;
		setRoot("auth.fxml", new AuthController());
	}

	public static void setRoot(String url, Initializable controller) {
		FXMLLoader loader = new FXMLLoader();
		loader.setLocation(TsClient.class.getResource(url));
		loader.setController(controller);
		Parent root = null;
		try {
			root = loader.load();
		} catch (IOException e) {
			e.printStackTrace();
		}
		Scene scene = new Scene(root);
		stage.setScene(scene);
		stage.show();
	}

	public static void main(String[] args) {
		config = new Config();
		launch();
	}

	public static Config getConfig() {
		return config;
	}

	public static Stage getStage() {
		return stage;
	}

}