package org.kalbinvv.tsclient.alert;

import javafx.scene.control.Alert;

public class AlertInformation extends Alert{

	public AlertInformation(String headerText, String contentText) {
		super(AlertType.INFORMATION);
		setTitle("Информация");
		setHeaderText(headerText);
		setContentText(contentText);
		showAndWait();
	}
	
	public AlertInformation(String headerText) {
		super(AlertType.INFORMATION);
		setTitle("Информация");
		setHeaderText(headerText);
		showAndWait();
	}

}
