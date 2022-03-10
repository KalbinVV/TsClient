package org.kalbinvv.tsclient;

import javafx.application.Application;

import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.File;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;

import org.kalbinvv.tsclient.controllers.AuthController;


public class TsClient extends Application {

	private static Stage stage;
	private static Config config;
	private static URL styleURL;

	@Override
	public void start(Stage stage) throws IOException {
		TsClient.stage = stage;
		TsClient.styleURL = loadResource("resources" + File.separator + "style.css");
		setResizable(false);
		setRoot("auth.fxml", new AuthController());
	}

	public static void setResizable(boolean resizeable) {
		stage.setResizable(resizeable);
	}

	public static void setRoot(String path, Initializable controller) {
		FXMLLoader loader = new FXMLLoader();
		loader.setLocation(loadResource("resources" + File.separator + path));
		loader.setController(controller);
		Parent root = null;
		try {
			root = loader.load();
		} catch (IOException e) {
			e.printStackTrace();
		}
		Scene scene = new Scene(root);
		scene.getStylesheets().add(TsClient.getStyleURL().toExternalForm());
		stage.setScene(scene);
		stage.show();
	}

	private static URL loadResource(String path) {
		URL url = null;
		File file = new File(path);
		try {
			url = file.toURI().toURL();
		} catch (MalformedURLException e) {
			e.printStackTrace();
		}
		return url;
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

	public static URL getStyleURL() {
		return styleURL;
	}

}