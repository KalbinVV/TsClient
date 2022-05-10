package org.kalbinvv.tsclient.layout;

import java.io.IOException;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.kalbinvv.tsclient.Config;
import org.kalbinvv.tsclient.EmptyUpdateable;
import org.kalbinvv.tsclient.TsClient;
import org.kalbinvv.tsclient.Utils;
import org.kalbinvv.tsclient.controllers.TestResultController;
import org.kalbinvv.tscore.net.Connection;
import org.kalbinvv.tscore.net.Request;
import org.kalbinvv.tscore.net.RequestType;
import org.kalbinvv.tscore.test.TestResult;
import org.kalbinvv.tscore.user.User;

import animatefx.animation.FadeIn;
import de.jensd.fx.glyphs.fontawesome.FontAwesomeIconView;
import javafx.event.ActionEvent;
import javafx.geometry.Insets;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.layout.VBox;

public class TestsResultsLayout extends Layout{

	public TestsResultsLayout(VBox vBox) {
		super(vBox);
	}

	@Override
	public void view() {
		draw();
		TsClient.setUpdateable(this);
		new FadeIn(getVBox()).play();
	}
	
	@Override
	public void update() {
		draw();
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
			@SuppressWarnings("unchecked")
			Set<User> users = (Set<User>) connection.sendRequestAndGetResponse(
					new Request(RequestType.GetUsers, null, config.getUser())).getObject();
			Set<String> alreadyViewed = new HashSet<String>();
			for(User user : users) {
				if(alreadyViewed.contains(user.getName())) continue;
				alreadyViewed.add(user.getName());
				List<TestResult> testsResults = Utils.getUsersTestsResult(user);
				if(testsResults.isEmpty()) continue;
				resultsBox.getChildren()
					.add(new Label("Средний балл пользователя '" 
						+ user.getName() + "': "
						+ testsResults.stream().mapToDouble((TestResult result) -> {
							return Math.floor(Double.valueOf(
									result.getAmountOfCorrectAnswers()) 
									/ result.getAmountOfAnswers() * 100);
						}).average().getAsDouble() + "%"));
				for(int i = testsResults.size() - 1; i >= 0; i--) {
					TestResult testResult = testsResults.get(i);
					VBox testResultBox = new VBox();
					Button viewTestResultButton = new Button("Посмотреть отчёт");
					viewTestResultButton.setOnAction((ActionEvent event) -> {
						TsClient.setUpdateable(new EmptyUpdateable());
						TsClient.setRoot("testResult.fxml", 
								new TestResultController(testResult));
					});
					testResultBox.setSpacing(5);
					Label authorText = new Label("Пользователь: " + 
							testResult.getUser().getName());
					Label resultText = new Label("Результат:\n" + 
							testResult.getAmountOfCorrectAnswers() 
					+ "/" + testResult.getAmountOfAnswers()
					+ "\n" + Math.floor(Double.valueOf(testResult.getAmountOfCorrectAnswers()) 
							/ testResult.getAmountOfAnswers() * 100) + "%");
					Label testDescription = new Label(
							"Тест:\n" + testResult.getTest().getName()
							+ "\n" + testResult.getTest().getDescription());
					testResultBox.getChildren().addAll(authorText, resultText, 
							testDescription, viewTestResultButton);
					resultsBox.getChildren().add(testResultBox);
				}
			}
		} catch (IOException e) {
			resultsBox.getChildren().add(
					new Label("Не удалось получить результаты: " + e.getMessage()));
		}
		resultsPane.setContent(resultsBox);
		addNode(resultsPane);
	}

}
