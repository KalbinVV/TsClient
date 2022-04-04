package org.kalbinvv.tsclient;

import javafx.application.Application;
import javafx.application.Platform;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;
import java.net.URL;

import org.kalbinvv.tsclient.controllers.AuthController;


public class TsClient extends Application {

	private static Stage stage;
	private static Config config;
	private static URL styleURL;
	private static Loader loader;
	private static UpdateThread updateThread;

	@Override
	public void start(Stage stage) throws IOException {
		loader = new Loader("resources");
		if(!loader.isResourcesFolderExist()) {
			//TODO installer
		}
		TsClient.stage = stage;
		TsClient.styleURL = loader.getFileURL("style.css");
		setRoot("auth.fxml", new AuthController());
	}
	
	@Override
	public void stop(){
		updateThread.interrupt();
		Platform.exit();
		System.exit(0);
	}
	
	public static void setUpdateable(Updateable updateable) {
		updateThread.setUpdateable(updateable);
	}

	public static void setResizable(boolean resizeable) {
		stage.setResizable(resizeable);
	}

	public static void setRoot(String path, Initializable controller) {
		FXMLLoader fxmlLoader = new FXMLLoader();
		fxmlLoader.setLocation(loader.getFileURL(path));
		fxmlLoader.setController(controller);
		Parent root = null;
		try {
			root = fxmlLoader.load();
		} catch (IOException e) {
			e.printStackTrace();
		}
		Scene scene = new Scene(root);
		scene.getStylesheets().add(TsClient.getStyleURL().toExternalForm());
		stage.setScene(scene);
		stage.show();
	}

	public static void main(String[] args) {
		config = new Config();
		updateThread = new UpdateThread();
		updateThread.run();
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