package org.kalbinvv.tsclient;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;


public class TsClient extends Application {

    private static Stage stage;
    private static Scene scene;
    private static Config config;

    @Override
    public void start(Stage stage) throws IOException {
    	FXMLLoader loader = new FXMLLoader();
		loader.setLocation(getClass().getResource("auth.fxml"));
		loader.setController(new Controller());
		Parent root = loader.load();
		stage.setTitle("Авторизация");
        stage.setScene(new Scene(root));
        stage.show();
    }
    
    public static void setRoot(String url) {
    	scene.setRoot(Utils.loadFXML(url));
    }

    public static void main(String[] args) {
    	config = new Config();
        launch();
    }

	public static Config getConfig() {
		return config;
	}

}