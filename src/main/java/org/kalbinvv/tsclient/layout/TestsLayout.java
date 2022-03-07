package org.kalbinvv.tsclient.layout;

import java.io.File;

import java.io.IOException;
import java.util.List;

import org.kalbinvv.tsclient.Config;
import org.kalbinvv.tsclient.TsClient;
import org.kalbinvv.tsclient.alert.AlertError;
import org.kalbinvv.tsclient.alert.AlertInformation;
import org.kalbinvv.tsclient.controllers.TestController;
import org.kalbinvv.tsclient.controllers.TestsEditorController;
import org.kalbinvv.tsclient.file.ObjectFileWorker;
import org.kalbinvv.tscore.net.Connection;
import org.kalbinvv.tscore.net.Request;
import org.kalbinvv.tscore.net.RequestType;
import org.kalbinvv.tscore.net.Response;
import org.kalbinvv.tscore.net.ResponseType;
import org.kalbinvv.tscore.test.Test;
import org.kalbinvv.tscore.test.TestData;
import org.kalbinvv.tscore.user.UserType;

import javafx.event.ActionEvent;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Button;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.text.Text;
import javafx.stage.FileChooser;

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
					new Request(RequestType.GetTests, null, config.getUser())).getObject();
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
				Button downloadTestButton = new Button("Скачать тест");
				Button editTestButton = new Button("Редактировать тест");
				downloadTestButton.setOnAction((ActionEvent event) -> 
					onDownloadTestButton(test));
				editTestButton.setOnAction((ActionEvent event) -> 
					onEditTestButton(test));
				if(TsClient.getConfig().getUser().getType() != UserType.Admin) {
					downloadTestButton.setVisible(false);
					editTestButton.setVisible(false);
				}
				HBox hBox = new HBox();
				hBox.setSpacing(5);
				hBox.getChildren().addAll(startTestButton, infoTestButton,
						downloadTestButton, editTestButton);
				addNode(hBox);
			}
		} catch (IOException e) {
			new AlertError("Не удалось получить список тестов", e.getMessage());
		}
	}

	private void onDownloadTestButton(Test test) {

		Config config = TsClient.getConfig();
		try {
			Connection connection = new Connection(config.getServerAddress().toSocket());
			Response response = connection.sendRequestAndGetResponse(new Request(
					RequestType.GetTestData, test, config.getUser()));
			if(response.getType() == ResponseType.Unsuccessful) {
				throw new IOException((String) response.getObject());
			} else {
				TestData testData = (TestData) response.getObject();
				FileChooser fileChooser = new FileChooser();
				File selectedFile = fileChooser.showSaveDialog(TsClient.getStage());
				if(selectedFile == null) {
					throw new IOException("Не указан путь сохранения!");
				}
				ObjectFileWorker objectFileWorker = new ObjectFileWorker();
				objectFileWorker.save(testData, selectedFile);
				new AlertInformation("Файл успешно сохранен!");
			}
		} catch (IOException e) {
			new AlertError("Не удалось скачать тест", e.getMessage());
		}
	}

	private void onEditTestButton(Test test) {
		Config config = TsClient.getConfig();
		try {
			Connection connection = new Connection(config.getServerAddress().toSocket());
			Response response = connection.sendRequestAndGetResponse(
					new Request(RequestType.GetTestData, test, config.getUser()));
			if(response.getType() == ResponseType.Unsuccessful) {
				throw new IOException((String) response.getObject());
			}
			TestData testData = (TestData) response.getObject();
			TsClient.setRoot("editor.fxml", new TestsEditorController(testData));
		} catch (IOException e) {
			new AlertError("Не удалось открыть редактор тестов", e.getMessage());
		}
	}
	
}
