package org.kalbinvv.tsclient.layout;

import java.io.IOException;
import java.util.List;

import org.kalbinvv.tsclient.Config;
import org.kalbinvv.tsclient.TsClient;
import org.kalbinvv.tscore.net.Connection;
import org.kalbinvv.tscore.net.Request;
import org.kalbinvv.tscore.net.RequestType;
import org.kalbinvv.tscore.net.Response;
import org.kalbinvv.tscore.net.ResponseType;

import javafx.scene.control.ScrollPane;
import javafx.scene.layout.VBox;
import javafx.scene.text.Text;

public class LogsLayout extends Layout{

	public LogsLayout(VBox vBox) {
		super(vBox);
	}

	@Override
	public void draw() {
		clearNodes();
		ScrollPane logsPane = new ScrollPane();
		logsPane.setMaxWidth(Double.MAX_VALUE);
		logsPane.setMaxHeight(Double.MAX_VALUE);
		VBox logsBox = new VBox();
		logsBox.setMaxWidth(Double.MAX_VALUE);
		Config config = TsClient.getConfig();
		try {
			Connection connection = new Connection(config.getServerAddress().toSocket());
			Response response = connection.sendRequestAndGetResponse(
					new Request(RequestType.GetLogs, null, config.getUser()));
			if(response.getType() == ResponseType.Successful) {
				@SuppressWarnings("unchecked")
				List<String> logs = (List<String>) response.getObject();
				for(String log : logs) {
					Text logNode = new Text(log);
					logsBox.getChildren().add(logNode);
				}
			}else {
				Text errorText = new Text("Не удалость получить журнал действий!\n" 
						+ (String) response.getObject());
				logsBox.getChildren().add(errorText);
			}
		} catch (IOException e) {
			Text errorText = new Text("Не удалость получить журнал действий!\n" + e.getMessage());
			logsBox.getChildren().add(errorText);
		}
		logsPane.setContent(logsBox);
		addNode(logsPane);
	}

}
