package org.kalbinvv.tsclient.controllers;

import java.io.IOException;

import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;

import org.kalbinvv.tsclient.AlertError;
import org.kalbinvv.tsclient.AlertInformation;
import org.kalbinvv.tsclient.Config;
import org.kalbinvv.tsclient.TsClient;
import org.kalbinvv.tscore.net.Connection;
import org.kalbinvv.tscore.net.Request;
import org.kalbinvv.tscore.net.RequestType;
import org.kalbinvv.tscore.net.Response;
import org.kalbinvv.tscore.net.ResponseType;
import org.kalbinvv.tscore.test.Question;
import org.kalbinvv.tscore.test.QuestionType;
import org.kalbinvv.tscore.test.SimpleTest;
import org.kalbinvv.tscore.test.Test;
import org.kalbinvv.tscore.test.TestData;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.TextArea;
import javafx.scene.layout.VBox;
import javafx.scene.text.Text;

public class TestsEditorController implements Initializable{

	@FXML
	private VBox questionsBox;
	
	@FXML
	private TextArea testTitleTextArea;

	@FXML
	private TextArea testDescriptionTextArea;
	
	private final List<Question> questions;
	private final List<List<String>> answers;


	public TestsEditorController(){
		questions = new ArrayList<Question>();
		answers = new ArrayList<List<String>>();
	}

	@Override
	public void initialize(URL location, ResourceBundle resources) {
		//TODO
	}

	public void onAddNewCheckBoxQuestionButton(ActionEvent event) {
		TsClient.setRoot("questionCreateForm.fxml", new QuestionCheckBoxCreateFormController(this));
	}

	public void onAddNewTextFieldQuestionButton(ActionEvent event) {
		TsClient.setRoot("questionCreateForm.fxml", new QuestionTextFieldCreateFormController(this));
	}

	public void onSaveToFileButton(ActionEvent event) {
		//TODO
	}

	public void onSendToServerButton(ActionEvent event) {
		Config config = TsClient.getConfig();
		try {
			Connection connection = new Connection(config.getServerAddress().toSocket());
			String testTitle = testTitleTextArea.getText();
			String testDescription = testDescriptionTextArea.getText();
			if(testTitle.isEmpty()) {
				throw new IOException("Название теста не может быть пустым!");
			}
			if(testDescription.isEmpty()) {
				throw new IOException("Описание теста не может быть пустым!");
			}
			if(questions.isEmpty()) {
				throw new IOException("Тест должен состоять минимум из 1-го вопроса!");
			}
			Test test = new SimpleTest(testTitle, testDescription, questions);
			TestData testData = new TestData(test, answers);
			Response response = connection.sendRequestAndGetResponse(new Request(
					RequestType.AddTest, testData, config.getUser()));
			if(response.getType() == ResponseType.Unsuccessful) {
				throw new IOException((String) response.getObject());
			}else {
				TsClient.setRoot("primary.fxml", new PrimaryController());
				new AlertInformation("Тест успешно отправлен на сервер!");
			}
		} catch (IOException e) {
			new AlertError("Не удаётся отправить тест на сервер!", e.getMessage());
		}
	}

	public void onReturnToPrimaryButton(ActionEvent event) {
		TsClient.setRoot("primary.fxml", new PrimaryController());
	}

	public void addQuestion(Question question, List<String> answers) {
		questions.add(question);
		this.answers.add(answers);
	}

	public void removeQuestion(String questionTitle) {
		int index = 0;
		for(Question question : questions) {
			if(question.getTitle().equals(questionTitle)) {
				break;
			}
			index++;
		}
		questions.remove(index);
		answers.remove(index);
	}

	public void drawQuestions() {
		questionsBox.getChildren().clear();
		int index = 0;
		for(Question question : questions) {
			VBox testBox = new VBox();
			testBox.setSpacing(5);
			Text questionTitle = new Text("Вопрос: " + question.getTitle());
			Text questionType = new Text();
			if(question.getType() == QuestionType.CheckBoxes) {
				questionType.setText("Тип: Выбор ответа");
			}else {
				questionType.setText("Тип: Ввод ответа");
			}
			Text variantsText = new Text("Варианты ответов: ");
			VBox variantsBox = new VBox();
			List<String> questionAnswers = answers.get(index);
			for(String variant : question.getVariants()) {
				if(questionAnswers.contains(variant) && question.getType() != QuestionType.TextFields) {
					variantsBox.getChildren().add(new Text(variant + " (Это правильный вариант)"));
				}else {
					variantsBox.getChildren().add(new Text(variant));
				}
			}
			Button changeQuestionButton = new Button("Изменить вопрос");
			if(question.getType() == QuestionType.CheckBoxes) {
				changeQuestionButton.setOnAction((ActionEvent event) -> {
					answers.remove(answers);
					questions.remove(question);
					TsClient.setRoot("questionCreateForm.fxml", 
							new QuestionCheckBoxCreateFormController(
									this, 
									question.getVariants(), questionAnswers,
									question.getTitle()));
				});
			}else {
				changeQuestionButton.setOnAction((ActionEvent event) -> {
					answers.remove(answers);
					questions.remove(question);
					TsClient.setRoot("questionCreateForm.fxml", 
							new QuestionTextFieldCreateFormController(
									this, 
									question.getVariants(), questionAnswers,
									question.getTitle()));
				});
			}
			Button deleteQuestionButton = new Button("Удалить вопрос");
			deleteQuestionButton.setOnAction((ActionEvent event) -> {
				questions.remove(question);
				answers.remove(questionAnswers);
				drawQuestions();
			});
			testBox.getChildren().addAll(questionTitle, questionType, variantsText, variantsBox, 
					changeQuestionButton, deleteQuestionButton);
			questionsBox.getChildren().add(testBox);
			index++;
		}
	}



}
