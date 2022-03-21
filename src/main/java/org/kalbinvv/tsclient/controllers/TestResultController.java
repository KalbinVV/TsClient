package org.kalbinvv.tsclient.controllers;

import java.net.URL;
import java.util.ResourceBundle;

import org.kalbinvv.tsclient.TsClient;
import org.kalbinvv.tscore.test.TestResult;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.chart.PieChart;
import javafx.scene.text.Text;

public class TestResultController implements Initializable{

	private TestResult testResultObject;

	@FXML
	private Text resultPercent;

	@FXML
	private PieChart chart;

	public TestResultController(TestResult testResultObject) {
		this.testResultObject = testResultObject;
	}

	@Override
	public void initialize(URL location, ResourceBundle resources) {
		PieChart.Data correctData = new PieChart.Data("Правильные ответы", 
				testResultObject.getAmountOfCorrectAnswers());
		int notCorrect = testResultObject.getAmountOfAnswers() - testResultObject.getAmountOfCorrectAnswers();
		chart.getData().add(correctData);
		if(notCorrect > 0) {
			PieChart.Data notCorrectData = new PieChart.Data("Неправильные ответы", 
					testResultObject.getAmountOfAnswers() - testResultObject.getAmountOfCorrectAnswers());
			chart.getData().add(notCorrectData);
		}
		resultPercent.setText("Финальный результат: " + Math.floor(Double.valueOf(testResultObject.getAmountOfCorrectAnswers()) 
				/ testResultObject.getAmountOfAnswers() * 100) + "%");
	}

	public void onReturnToPrimaryButton(ActionEvent event) {
		TsClient.setRoot("primary.fxml", new PrimaryController());
	}

}
