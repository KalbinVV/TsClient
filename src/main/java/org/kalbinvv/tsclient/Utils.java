package org.kalbinvv.tsclient;

import java.io.IOException;

import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;

public class Utils {

	public static Parent loadFXML(String url) {
		FXMLLoader loader = new FXMLLoader();
		loader.setLocation(TsClient.class.getResource(url));
		Parent root = null;
		try {
			root = loader.load();
		} catch (IOException e) {
			e.printStackTrace();
		}
		return root;
	}
	
}
