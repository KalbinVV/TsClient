package org.kalbinvv.tsclient.controllers;

import java.net.URL;

import java.util.ResourceBundle;

import org.kalbinvv.tsclient.TsClient;
import org.kalbinvv.tscore.test.TestResult;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.text.Text;

public class TestResultController implements Initializable{

	private TestResult testResultObject;

	@FXML
	private Text resultPercent;


	public TestResultController(TestResult testResultObject) {
		this.testResultObject = testResultObject;
	}

	@Override
	public void initialize(URL location, ResourceBundle resources) {
		resultPercent.setText(Math.floor(Double.valueOf(testResultObject.
				getAmountOfCorrectAnswers()) 
				/ testResultObject.getAmountOfAnswers() * 100) + "%");
	}

	public void onReturnToPrimaryButton(ActionEvent event) {
		TsClient.setRoot("primary.fxml", new PrimaryController());
	}

}
