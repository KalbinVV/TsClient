package org.kalbinvv.tsclient.layout;

import java.io.IOException;
import java.util.List;

import org.kalbinvv.tsclient.Config;
import org.kalbinvv.tsclient.TsClient;
import org.kalbinvv.tsclient.controllers.TestController;
import org.kalbinvv.tscore.net.Connection;
import org.kalbinvv.tscore.net.Request;
import org.kalbinvv.tscore.net.RequestType;
import org.kalbinvv.tscore.test.Test;

import javafx.event.ActionEvent;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Button;
import javafx.scene.layout.HBox;
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
				Button startTestButton = new Button("Начать тест");
				startTestButton.setOnAction((ActionEvent event) -> {
					config.getUser().setTest(test);
					TsClient.setRoot("test.fxml", new TestController());
				});
				Button infoTestButton = new Button("Информация о тесте");
				infoTestButton.setOnAction((ActionEvent event) -> {
					Alert alert = new Alert(AlertType.INFORMATION);
					alert.setContentText(test.getDescription());
					alert.showAndWait();
				});
				HBox hBox = new HBox();
				hBox.setSpacing(5);
				hBox.getChildren().add(startTestButton);
				hBox.getChildren().add(infoTestButton);
				addNode(hBox);
			}
		} catch (IOException e) {
			Alert alert = new Alert(AlertType.ERROR);
			alert.setContentText("Не удалость получить список тестов!\n" + e.getMessage());
			alert.showAndWait();
		}
	}

}
