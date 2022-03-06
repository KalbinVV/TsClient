package org.kalbinvv.tsclient.controllers;

import java.io.IOException;

import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;

import org.kalbinvv.tsclient.Config;
import org.kalbinvv.tsclient.TsClient;
import org.kalbinvv.tscore.net.Connection;
import org.kalbinvv.tscore.net.Request;
import org.kalbinvv.tscore.net.RequestType;
import org.kalbinvv.tscore.net.Response;
import org.kalbinvv.tscore.test.Question;
import org.kalbinvv.tscore.test.QuestionType;
import org.kalbinvv.tscore.test.Test;
import org.kalbinvv.tscore.test.TestResult;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.CheckBox;
import javafx.scene.control.TextField;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.layout.VBox;
import javafx.scene.text.Text;

public class TestController implements Initializable{

	@FXML
	private Text testTitle;

	@FXML
	private VBox questionsBox;

	@FXML
	private Button nextQuestionButton;

	@FXML
	private Button prevQuestionButton;

	@Override
	public void initialize(URL location, ResourceBundle resources) {
		drawInterface();
	}

	public void onNextQuestionButton(ActionEvent event) {
		Test test = TsClient.getConfig().getUser().getTest();
		int currentQuestion = test.getCurrentQuestion();
		List<String> answers = new ArrayList<String>();
		Question question = test.getQuestions().get(currentQuestion);
		if(question.getType() == QuestionType.CheckBoxes) {
			for(Node node : questionsBox.getChildren()) {
				CheckBox checkBox = (CheckBox) node;
				if(checkBox.isSelected()) {
					answers.add(checkBox.getText());
				}
			}
		}else if(question.getType() == QuestionType.TextFields) {
			for(Node node : questionsBox.getChildren()) {
				TextField textField = (TextField) node;
				if(!textField.getText().isEmpty()) {
					answers.add(textField.getText());
				}
			}
		}
		question.setUserSelect(answers);
		if(currentQuestion == test.getQuestions().size() - 1) {
			Config config = TsClient.getConfig();
			try {
				Connection connection = new Connection(config.getServerAddress().toSocket());
				Response response = connection.sendRequestAndGetResponse(
						new Request(RequestType.CompleteTest, null, config.getUser()));
				TsClient.setRoot("testResult.fxml",
						new TestResultController((TestResult)response.getObject()));
			} catch (IOException e) {
				Alert alert = new Alert(AlertType.ERROR);
				alert.setContentText("Не удалось завершить тест!\n" + e.getMessage());
				alert.showAndWait();
			}
		}else {
			test.setCurrentQuestion(currentQuestion + 1);
			drawInterface();
		}
	}

	public void onPrevQuestionButton(ActionEvent event	) {
		Test test = TsClient.getConfig().getUser().getTest();
		int currentQuestion = test.getCurrentQuestion();
		test.setCurrentQuestion(currentQuestion - 1);
		drawInterface();
	}

	private void drawInterface() {
		Test test = TsClient.getConfig().getUser().getTest();
		int currentQuestion = test.getCurrentQuestion();
		Question question = test.getQuestions().get(currentQuestion);
		testTitle.setText(question.getTitle());
		prevQuestionButton.setVisible(true);
		if(currentQuestion == 0) {
			prevQuestionButton.setVisible(false);
		}
		nextQuestionButton.setText("Продолжить");
		if(currentQuestion == test.getQuestions().size() - 1) {
			nextQuestionButton.setText("Завершить тестирование");
		}
		questionsBox.getChildren().clear();
		if(question.getType() == QuestionType.CheckBoxes) {
			for(String variant : question.getVariants()) {
				CheckBox checkBox = new CheckBox(variant);
				checkBox.setMaxWidth(Double.MAX_VALUE);
				questionsBox.getChildren().add(checkBox);
			}
		}else if(question.getType() == QuestionType.TextFields) {
			TextField textField = new TextField();
			textField.setMaxWidth(Double.MAX_VALUE);
			questionsBox.getChildren().add(textField);
		}
	}

}
