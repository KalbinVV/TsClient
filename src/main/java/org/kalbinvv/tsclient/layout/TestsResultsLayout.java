package org.kalbinvv.tsclient.layout;

import java.io.IOException;


import java.util.List;

import org.kalbinvv.tsclient.Config;
import org.kalbinvv.tsclient.TsClient;
import org.kalbinvv.tsclient.controllers.TestResultController;
import org.kalbinvv.tscore.net.Connection;
import org.kalbinvv.tscore.net.Request;
import org.kalbinvv.tscore.net.RequestType;
import org.kalbinvv.tscore.net.Response;
import org.kalbinvv.tscore.net.ResponseType;
import org.kalbinvv.tscore.test.TestResult;

import animatefx.animation.FadeIn;
import de.jensd.fx.glyphs.fontawesome.FontAwesomeIconView;
import javafx.event.ActionEvent;
import javafx.geometry.Insets;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.layout.VBox;
import javafx.scene.text.Text;

public class TestsResultsLayout extends Layout{

	public TestsResultsLayout(VBox vBox) {
		super(vBox);
	}

	@Override
	public void draw() {
		clearNodes();
		FontAwesomeIconView resultsIcon = new FontAwesomeIconView();
		resultsIcon.setGlyphName("STAR");
		resultsIcon.setGlyphSize(50);
		Label headerLabel = new Label("Результаты");
		addNode(resultsIcon);
		addNode(headerLabel);
		ScrollPane resultsPane = new ScrollPane();
		resultsPane.setMaxWidth(Double.MAX_VALUE);
		VBox resultsBox = new VBox();
		resultsBox.setSpacing(20);
		resultsBox.setPadding(new Insets(10, 10, 10, 10));
		Config config = TsClient.getConfig();
		try {
			Connection connection = new Connection(config.getServerAddress().toSocket());
			Response response = connection.sendRequestAndGetResponse(
					new Request(RequestType.GetTestsResults, null, config.getUser()));
			if(response.getType() == ResponseType.Unsuccessful) {
				Text errorText = new Text("Не удалось получить результаты: " 
						+ (String) response.getObject());
				resultsBox.getChildren().add(errorText);
			} else {
				@SuppressWarnings("unchecked")
				List<TestResult> testsResults = (List<TestResult>) response.getObject();
				for(int i = testsResults.size() - 1; i >= 0; i--) {
					TestResult testResult = testsResults.get(i);
					VBox testResultBox = new VBox();
					Button viewTestResultButton = new Button("Посмотреть отчёт");
					viewTestResultButton.setOnAction((ActionEvent event) -> {
						TsClient.setRoot("testResult.fxml", 
								new TestResultController(testResult));
					});
					testResultBox.setSpacing(5);
					Text authorText = new Text("Пользователь: " + 
							testResult.getUser().getName());
					Text resultText = new Text("Результат:\n" + 
							testResult.getAmountOfCorrectAnswers() 
					+ "/" + testResult.getAmountOfAnswers()
					+ "\n" + Math.floor(Double.valueOf(testResult.getAmountOfCorrectAnswers()) 
							/ testResult.getAmountOfAnswers() * 100) + "%");
					Text testDescription = new Text("Тест:\n" + testResult.getTest().getName()
							+ "\n" + testResult.getTest().getDescription());
					testResultBox.getChildren().addAll(authorText, resultText, 
							testDescription, viewTestResultButton);
					resultsBox.getChildren().add(testResultBox);
				}
			}
		} catch (IOException e) {
			Text errorText = new Text("Не удалось получить результаты: " + e.getMessage());
			resultsBox.getChildren().add(errorText);
		}
		resultsPane.setContent(resultsBox);
		addNode(resultsPane);
		new FadeIn(getVBox()).play();
	}

}
