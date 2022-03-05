package org.kalbinvv.tsclient.controllers;

import java.net.URL;
import java.util.ResourceBundle;

import org.kalbinvv.tsclient.TsClient;
import org.kalbinvv.tscore.test.Question;
import org.kalbinvv.tscore.test.QuestionType;
import org.kalbinvv.tscore.test.Test;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
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
		if(currentQuestion == test.getQuestions().size() - 1) {
			Alert alert = new Alert(AlertType.INFORMATION);
			alert.setContentText("Конец теста!");
			alert.showAndWait();
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
			for(String variant : question.getVariants()) {
				TextField textField = new TextField();
				textField.setPromptText(variant);
				textField.setMaxWidth(Double.MAX_VALUE);
				questionsBox.getChildren().add(textField);
			}
		}
	}

}
