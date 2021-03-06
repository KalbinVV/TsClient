package org.kalbinvv.tsclient.layout;

import java.io.File;

import java.io.IOException;
import java.util.List;

import org.kalbinvv.tsclient.Config;
import org.kalbinvv.tsclient.EmptyUpdateable;
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
import org.kalbinvv.tscore.user.User;
import org.kalbinvv.tscore.user.UserType;

import animatefx.animation.FadeIn;
import de.jensd.fx.glyphs.fontawesome.FontAwesomeIconView;
import javafx.event.ActionEvent;
import javafx.geometry.Insets;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.layout.VBox;
import javafx.scene.text.Text;
import javafx.stage.FileChooser;

public class TestsLayout extends Layout{

	public TestsLayout(VBox vBox) {
		super(vBox);
	}

	@Override
	public void view() {
		draw();
		TsClient.setUpdateable(this);
		new FadeIn(getVBox()).play();
	}

	@Override
	public void draw() {
		clearNodes();
		FontAwesomeIconView testsIcon = new FontAwesomeIconView();
		testsIcon.setGlyphName("LIST");
		testsIcon.setGlyphSize(50);
		Label headerLabel = new Label("Тестирование");
		addNode(testsIcon);
		addNode(headerLabel);
		Config config = TsClient.getConfig();
		User user = config.getUser();
		VBox vBox = new VBox();
		vBox.setSpacing(20);
		vBox.setPadding(new Insets(10, 10, 10, 10));
		ScrollPane scrollPane = new ScrollPane();
		try {
			Connection connection = new Connection(config.getServerAddress().toSocket());
			@SuppressWarnings("unchecked")
			List<Test> tests = (List<Test>) connection.sendRequestAndGetResponse(
					new Request(RequestType.GetTests, null, config.getUser())).getObject();
			for(Test test : tests) {		
				Text testNode = new Text("Название: " + test.getName() + "\n" 
						+ "Описание: " + test.getDescription());
				Button startTestButton = new Button("Начать тест");
				startTestButton.setOnAction((ActionEvent event) -> {
					config.getUser().setTest(test);
					TsClient.setUpdateable(new EmptyUpdateable());
					TsClient.setRoot("test.fxml", new TestController());
				});
				VBox testBox = new VBox();
				testBox.setSpacing(5);
				if(user.getType() == UserType.Admin) {
					Button downloadTestButton = new Button("Скачать тест");
					Button editTestButton = new Button("Редактировать тест");
					Button removeTestButton = new Button("Удалить тест");
					Button showTestResultsButton = new Button("Посмотреть результаты теста");
					downloadTestButton.setOnAction((ActionEvent event) -> 
					onDownloadTestButton(test));
					editTestButton.setOnAction((ActionEvent event) -> 
					onEditTestButton(test));
					removeTestButton.setOnAction((ActionEvent event) ->
					onRemoveTestButton(test));
					showTestResultsButton.setOnAction((ActionEvent event) -> {
						new certainTestResultsLayout(getVBox(), test).view();
					});
					if(TsClient.getConfig().getUser().getType() != UserType.Admin) {
						downloadTestButton.setVisible(false);
						editTestButton.setVisible(false);
					}
					testBox.getChildren().addAll(testNode, startTestButton,
							downloadTestButton, editTestButton, removeTestButton,
							showTestResultsButton);
				}else {
					testBox.getChildren().addAll(testNode, startTestButton);
				}
				vBox.getChildren().add(testBox);
			}
			scrollPane.setContent(vBox);
			addNode(scrollPane);
		} catch (IOException e) {
			new AlertError("Не удалось получить список тестов", e.getMessage());
		}
	}

	@Override
	public void update() {
		draw();
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
		TsClient.setUpdateable(new EmptyUpdateable());
		Config config = TsClient.getConfig();
		try {
			Connection connection = new Connection(config.getServerAddress().toSocket());
			User user = config.getUser();
			Response response = connection.sendRequestAndGetResponse(
					new Request(RequestType.GetTestData, test, user));
			if(response.getType() == ResponseType.Unsuccessful) {
				throw new IOException((String) response.getObject());
			}
			TestData testData = (TestData) response.getObject();
			TsClient.setRoot("editor.fxml", new TestsEditorController(testData));
			connection.reconnect();
			connection.sendRequest(new Request(RequestType.RemoveTest, test, user));
		} catch (IOException e) {
			new AlertError("Не удалось открыть редактор тестов", e.getMessage());
		}
	}

	private void onRemoveTestButton(Test test) {
		TsClient.setUpdateable(new EmptyUpdateable());
		Config config = TsClient.getConfig();
		try {
			Connection connection = new Connection(config.getServerAddress().toSocket());
			Response response = connection.sendRequestAndGetResponse(new Request(
					RequestType.RemoveTest, test, config.getUser()));
			if(response.getType() != ResponseType.Successful) {
				throw new IOException((String) response.getObject());
			}
		} catch (IOException e) {
			new AlertError("Не удалось удалить тест!", e.getMessage());
		}
		draw();
	}

}
