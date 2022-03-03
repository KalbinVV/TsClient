package org.kalbinvv.tsclient.layout;

import java.io.IOException;
import java.util.List;

import org.kalbinvv.tsclient.Config;
import org.kalbinvv.tsclient.TsClient;
import org.kalbinvv.tscore.net.Connection;
import org.kalbinvv.tscore.net.Request;
import org.kalbinvv.tscore.net.RequestType;
import org.kalbinvv.tscore.test.Test;

import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.layout.VBox;
import javafx.scene.text.Text;

public class TestsLayout extends Layout{

	public TestsLayout(VBox vBox) {
		super(vBox);
	}

	@Override
	public void draw() {
		clearNodes();
		Text headerText = new Text("Тесты: ");
		addNode(headerText);
		Config config = TsClient.getConfig();
		try {
			Connection connection = new Connection(config.getServerAddress().toSocket());
			@SuppressWarnings("unchecked")
			List<Test> tests = (List<Test>) connection.sendRequestAndGetResponse(
					new Request(RequestType.GetTestsList, null, config.getUser())).getObject();
			for(Test test : tests) {
				Text testNode = new Text("Название: " + test.getName() + "\n" 
						+ "Описание: " + test.getDescription());
				addNode(testNode);
			}
		} catch (IOException e) {
			Alert alert = new Alert(AlertType.ERROR);
			alert.setContentText("Не удалость получить список тестов!\n" + e.getMessage());
			alert.showAndWait();
		}
	}

}
