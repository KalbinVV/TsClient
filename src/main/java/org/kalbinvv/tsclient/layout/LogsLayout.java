package org.kalbinvv.tsclient.layout;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.List;

import org.kalbinvv.tsclient.Config;
import org.kalbinvv.tsclient.TsClient;
import org.kalbinvv.tsclient.alert.AlertError;
import org.kalbinvv.tsclient.alert.AlertInformation;
import org.kalbinvv.tscore.net.Connection;
import org.kalbinvv.tscore.net.Request;
import org.kalbinvv.tscore.net.RequestType;
import org.kalbinvv.tscore.net.Response;
import org.kalbinvv.tscore.net.ResponseType;

import animatefx.animation.FadeIn;
import de.jensd.fx.glyphs.fontawesome.FontAwesomeIconView;
import javafx.event.ActionEvent;
import javafx.geometry.Insets;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.layout.VBox;
import javafx.stage.FileChooser;

public class LogsLayout extends Layout{

	public LogsLayout(VBox vBox) {
		super(vBox);
	}

	@Override
	public void draw() {
		clearNodes();
		FontAwesomeIconView logsIcon = new FontAwesomeIconView();
		logsIcon.setGlyphName("FILTER");
		logsIcon.setGlyphSize(50);
		Label headerLabel = new Label("Журнал действий");
		addNode(logsIcon);
		addNode(headerLabel);
		ScrollPane logsPane = new ScrollPane();
		logsPane.setMaxWidth(Double.MAX_VALUE);
		logsPane.setMaxHeight(Double.MAX_VALUE);
		VBox logsBox = new VBox();
		logsBox.setPadding(new Insets(10, 10, 10, 10));
		logsBox.setSpacing(5);
		logsBox.setMaxWidth(Double.MAX_VALUE);
		Config config = TsClient.getConfig();
		try {
			Connection connection = new Connection(config.getServerAddress().toSocket());
			Response response = connection.sendRequestAndGetResponse(
					new Request(RequestType.GetLogs, null, config.getUser()));
			if(response.getType() == ResponseType.Successful) {
				@SuppressWarnings("unchecked")
				List<String> logs = (List<String>) response.getObject();
				StringBuilder stringBuilder = new StringBuilder();
				for(int i = logs.size() - 1; i >= 0; i--) {
					stringBuilder.append(logs.get(i)).append("\n");
				}
				logsBox.getChildren().add(new Label(stringBuilder.toString()));
			}else {
				Label errorText = new Label("Не удалость получить журнал действий!\n" 
						+ (String) response.getObject());
				logsBox.getChildren().add(errorText);
			}
		} catch (IOException e) {
			Label errorText = new Label("Не удалость получить журнал действий!\n" 
					+ e.getMessage());
			logsBox.getChildren().add(errorText);
		}
		logsPane.setContent(logsBox);
		Button downloadJournalButton = new Button("Скачать журнал");
		downloadJournalButton.setMaxWidth(Double.MAX_VALUE);
		downloadJournalButton.setOnAction(
				(ActionEvent event) -> onDownloadJournalButton(event));
		addNode(logsPane);
		addNode(downloadJournalButton);
		new FadeIn(getVBox()).play();
	}

	public void onDownloadJournalButton(ActionEvent event) {
		FileChooser fileChooser = new FileChooser();
		File selectedFile = fileChooser.showSaveDialog(TsClient.getStage());
		if(selectedFile == null) {
			new AlertError("Не удалось скачать тест!", "Не указан путь");
		}else {
			BufferedWriter bufferedWriter = null;
			try {
				bufferedWriter = new BufferedWriter(new FileWriter(selectedFile));
				Config config = TsClient.getConfig();
				try {
					Connection connection = new Connection(
							config.getServerAddress().toSocket());
					Response response = connection.sendRequestAndGetResponse(
							new Request(RequestType.GetLogs, null, config.getUser()));
					if(response.getType() == ResponseType.Successful) {
						@SuppressWarnings("unchecked")
						List<String> logs = (List<String>) response.getObject();
						for(String log : logs) {
							bufferedWriter.write(log + "\n");
						}
					}else {
						new AlertError("Не удалость получить журнал действий!\n", 
								(String) response.getObject());
					}
				} catch (IOException e) {
					new AlertError("Не удалость получить журнал действий!\n", 
							e.getMessage());
				}
				new AlertInformation("Журнал успешно загружен!");
			} catch (IOException e) {
				e.printStackTrace();
			} finally {
				try {
					bufferedWriter.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}
	}

}
