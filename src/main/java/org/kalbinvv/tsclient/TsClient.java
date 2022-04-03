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
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

import org.kalbinvv.tsclient.controllers.AuthController;


public class TsClient extends Application {

	private static Stage stage;
	private static Config config;
	private static URL styleURL;
	private static Loader loader;
	private static UpdateTask updateTask;

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
	
	public static void setUpdateable(Updateable updateable) {
		updateTask.setUpdateable(updateable);
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
		updateTask = new UpdateTask(new EmptyUpdateable());
		ScheduledExecutorService executor = Executors.newScheduledThreadPool(1);
	    Runnable loop = new Runnable() {
	        public void run() {
	             Platform.runLater(new Runnable() {
	                 @Override 
	                 public void run() {
	                     updateTask.run();
	                 }
	             });
	        }
	    };
	    executor.scheduleAtFixedRate(loop, 0, 5, TimeUnit.SECONDS); //Every 5 seconds
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