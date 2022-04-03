package org.kalbinvv.tsclient.controllers;

import java.net.URL;

import java.util.Map;
import java.util.ResourceBundle;

import org.kalbinvv.tsclient.TsClient;
import org.kalbinvv.tscore.test.TestResult;

import de.jensd.fx.glyphs.fontawesome.FontAwesomeIconView;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.geometry.Insets;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.layout.VBox;
import javafx.scene.text.Text;

public class TestResultController implements Initializable{

	@FXML
	private Text resultPercent;

	@FXML
	private VBox resultBox;
	
	@FXML
    private Text testTitle;

    @FXML
    private Text userName;
    
	private final TestResult testResultObject;

	public TestResultController(TestResult testResultObject) {
		this.testResultObject = testResultObject;
	}

	@Override
	public void initialize(URL location, ResourceBundle resources) {
		userName.setText(testResultObject.getUser().getName());
		testTitle.setText(testResultObject.getTest().getName());
		FontAwesomeIconView resultsIcon = new FontAwesomeIconView();
		resultsIcon.setGlyphName("FILTER");
		resultsIcon.setGlyphSize(50);
		Label resultHeaderLabel = new Label("Результаты вопросов");
		resultBox.getChildren().addAll(resultsIcon, resultHeaderLabel);
		VBox questionsResultsBox = new VBox();
		questionsResultsBox.setSpacing(10);
		questionsResultsBox.setPadding(new Insets(10, 10, 10, 10));
		ScrollPane scrollPane = new ScrollPane();
		for(Map.Entry<String, Integer> entry 
				: testResultObject.getAnswersResult().entrySet()) {
			VBox questionResultBox = new VBox();
			questionResultBox.getChildren().addAll(
					new Label(entry.getKey()), new Label(entry.getValue().toString()));
			questionsResultsBox.getChildren().add(questionResultBox);
		}
		questionsResultsBox.getChildren().add(new Label("Результат: "
				+ testResultObject.getAmountOfCorrectAnswers() + "/"
				+ testResultObject.getAmountOfAnswers()));
		resultPercent.setText(Math.floor(Double.valueOf(testResultObject.
				getAmountOfCorrectAnswers()) 
				/ testResultObject.getAmountOfAnswers() * 100) + "%");
		scrollPane.setContent(questionsResultsBox);
		resultBox.getChildren().add(scrollPane);
	}

	public void onReturnToPrimaryButton(ActionEvent event) {
		TsClient.setRoot("primary.fxml", new PrimaryController());
	}


}
