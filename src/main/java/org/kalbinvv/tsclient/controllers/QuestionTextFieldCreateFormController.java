package org.kalbinvv.tsclient.controllers;

import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.ResourceBundle;

import org.kalbinvv.tsclient.TsClient;
import org.kalbinvv.tsclient.alert.AlertError;
import org.kalbinvv.tscore.test.Question;
import org.kalbinvv.tscore.test.QuestionType;
import org.kalbinvv.tscore.test.SimpleQuestion;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextInputDialog;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.VBox;
import javafx.scene.text.Text;

public class QuestionTextFieldCreateFormController implements Initializable {

	private final TestsEditorController testsEditorController;
	private List<String> variants;
	private List<String> answers;
	private String defaultTestTitle;
	
	@FXML
	private TextArea questionTitleTextArea;
	
	@FXML
	private VBox variantsBox;
	
	@FXML
	private Text questionTypeText;
	
	@FXML
	private VBox buttonsBox;
	
	QuestionTextFieldCreateFormController(TestsEditorController testsEditorController){
		this.testsEditorController = testsEditorController;
		variants = new ArrayList<String>();
		answers = new ArrayList<String>();
		defaultTestTitle = "Заголовок";
	}
	
	QuestionTextFieldCreateFormController(TestsEditorController testsEditorController, 
			List<String> variants, List<String> answers,
			String questionTitle){
		this.testsEditorController = testsEditorController;
		this.variants = variants;
		this.answers = answers;
		defaultTestTitle = questionTitle;
	}
	
	@Override
	public void initialize(URL location, ResourceBundle resources) {
		questionTitleTextArea.setText(defaultTestTitle);
		questionTypeText.setText("Вопрос с вводом ответа");
		drawVariants();
		Button addVariantButton = new Button("Добавить ответ");
		addVariantButton.setMaxWidth(Double.MAX_VALUE);
		addVariantButton.setOnAction((ActionEvent event) -> onAddVariantButton(event));
		Button saveQuestionButton = new Button("Удалить вопрос");
		saveQuestionButton.setMaxWidth(Double.MAX_VALUE);
		saveQuestionButton.setOnAction((ActionEvent event) -> onSaveQuestionButton(event));
		Button cancelCreateButton = new Button("Отмена");
		cancelCreateButton.setMaxWidth(Double.MAX_VALUE);
		cancelCreateButton.setOnAction((ActionEvent event) -> onCancelCreateButton(event));
		buttonsBox.getChildren().addAll(addVariantButton, saveQuestionButton, cancelCreateButton);
	}
	
	private void onAddVariantButton(ActionEvent event) {
		TextInputDialog dialog = new TextInputDialog();
		dialog.setHeaderText("Введите ответ: ");
		dialog.setTitle("Создать новый ответ!");
		Optional<String> result = dialog.showAndWait();
		result.ifPresent(variant -> {
			variants.add(variant);
			answers.add(variant);
		});
		drawVariants();
	}
	
	private void onSaveQuestionButton(ActionEvent event) {
		String questionTitle = questionTitleTextArea.getText();
		if(questionTitle.isEmpty()) {
			new AlertError("Не удалось создать вопрос!", "Заголовок вопроса не может быть пустым!");
		}else {
			if(answers.isEmpty()) {
				new AlertError("Не удалось создать вопрос!", 
						"Вопрос должен состоять минимум из одного правильного варианта!");
			}else {
				Question question = new SimpleQuestion(questionTitle, QuestionType.TextFields, variants);
				testsEditorController.addQuestion(question, answers);
				TsClient.setRoot("editor.fxml", testsEditorController);
				testsEditorController.drawQuestions();
			}
		}
	}
	
	public void onCancelCreateButton(ActionEvent event) {
		TsClient.setRoot("editor.fxml", testsEditorController);
		testsEditorController.drawQuestions();
		testsEditorController.drawDetails();
	}
	
	private void drawVariants() {
		variantsBox.getChildren().clear();
		for(String variant : variants) {
			Label variantLabel = null;
			Button deleteVariantButton = new Button("Удалить вариант ответа");
			variantLabel = new Label(variant);
			deleteVariantButton.setOnAction((ActionEvent event) -> {
				variants.remove(variant);
				answers.remove(variant);
				drawVariants();
			});
			variantsBox.getChildren().addAll(variantLabel, deleteVariantButton);
		}
	}
	

}
