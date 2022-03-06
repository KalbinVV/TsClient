package org.kalbinvv.tsclient.controllers;

import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;

import org.kalbinvv.tsclient.TsClient;
import org.kalbinvv.tscore.test.Question;
import org.kalbinvv.tscore.test.QuestionType;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.layout.VBox;
import javafx.scene.text.Text;

public class TestsEditorController implements Initializable{

	@FXML
	private VBox questionsBox;
	
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
		TsClient.setRoot("questionCreateForm.fxml", new QuestionCreateFormController(this, 
				QuestionType.CheckBoxes));
	}
	
	public void onAddNewTextFieldQuestionButton(ActionEvent event) {
		//TODO
	}
	
	public void onSaveToFileButton(ActionEvent event) {
		//TODO
	}
	
	public void onSendToServerButton(ActionEvent event) {
		//TODO
	}
	
	public void onReturnToPrimaryButton(ActionEvent event) {
		//TOOD
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
				if(questionAnswers.contains(variant)) {
					variantsBox.getChildren().add(new Text(variant + " (Это правильный вариант)"));
				}else {
					variantsBox.getChildren().add(new Text(variant));
				}
			}
			Button changeQuestionButton = new Button("Изменить вопрос");
			changeQuestionButton.setOnAction((ActionEvent event) -> {
				answers.remove(answers);
				questions.remove(question);
				TsClient.setRoot("questionCreateForm.fxml", new QuestionCreateFormController(this, 
						question.getType(), question.getVariants(), questionAnswers,
						question.getTitle()));
			});
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
