package org.kalbinvv.tsclient.alert;

import javafx.scene.control.Alert;

public class AlertError extends Alert{

	public AlertError(String headerText, String contentText) {
		super(AlertType.ERROR);
		setTitle("Ошибка");
		setHeaderText(headerText);
		setContentText(contentText);
		showAndWait();
	}

}
