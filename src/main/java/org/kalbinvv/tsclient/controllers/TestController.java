package org.kalbinvv.tsclient.controllers;

import java.net.URL;
import java.util.ResourceBundle;

import org.kalbinvv.tsclient.TsClient;
import org.kalbinvv.tscore.test.Test;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
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
		Test test = TsClient.getConfig().getUser().getTest();
		testTitle.setText(test.getQuestions().get(test.getCurrentQuestion()).getTitle());
	}
	
	public void onNextQuestionButton(ActionEvent event) {
		//
	}
	
	public void onPrevQuestionButton(ActionEvent event	) {
		
	}
	

}
